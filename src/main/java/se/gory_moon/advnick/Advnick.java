package se.gory_moon.advnick;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import se.gory_moon.advnick.wrapper.WrapperPlayServerChat;

public final class Advnick extends JavaPlugin implements Listener {
    private static Permission perms = null;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("Disabled due to no ProtocolLib dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        reloadConfig();
        saveConfig();

        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();

        Gson gson = new Gson();
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (getConfig().getBoolean("enabled")) {
                    WrapperPlayServerChat chat = new WrapperPlayServerChat(event.getPacket());
                    WrappedChatComponent message = chat.getMessage();
                    if (message != null) {
                        String msg = message.getJson();
                        if (msg.startsWith("{\"translate\":\"chat.type.advancement.")) {
                            JsonObject json = gson.fromJson(msg, JsonObject.class);
                            JsonObject nameBlock = json.getAsJsonArray("with").get(0).getAsJsonObject();
                            String name = nameBlock.get("text").getAsString();
                            Player player = Bukkit.getPlayer(name);
                            if (player != null) {
                                nameBlock.addProperty("text", player.getDisplayName());
                                message.setJson(gson.toJson(json));
                                chat.setMessage(message);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info(String.format("Disabled version %s", getDescription().getVersion()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getLabel().equals("advnickreload")) {
            if (perms.has(sender, "advnick.reload")) {
                reloadConfig();
                TextComponent msg = new TextComponent(String.format("[%s] Reloaded config", getDescription().getName()));
                msg.setColor(ChatColor.YELLOW);
                sender.spigot().sendMessage(msg);
                return true;
            }
        }
        return false;
    }
}
