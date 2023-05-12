package se.gory_moon.advnick;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import se.gory_moon.advnick.wrapper.WrapperPlayServerSystemChat;

public final class Advnick extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("Disabled due to no ProtocolLib dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        reloadConfig();
        saveConfig();

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (getConfig().getBoolean("enabled")) {
					var chat = new WrapperPlayServerSystemChat(event.getPacket());
                    var message = chat.getAdventureContent();
                    if (message instanceof TranslatableComponent msg) {
                        if (msg.key().startsWith("chat.type.advancement.")) {
							if (msg.args().get(0) instanceof TextComponent text) {
								var player = Bukkit.getPlayer(text.content());
								if (player != null) {
									var list = new ArrayList<Component>(msg.args());
									list.set(0, text.content(player.getDisplayName()));
									chat.setAdventureContent(msg.args(list));
								}
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
            if (sender.hasPermission("advnick.reload")) {
                reloadConfig();
                var msg = new net.md_5.bungee.api.chat.TextComponent(String.format("[%s] Reloaded config", getDescription().getName()));
                msg.setColor(ChatColor.YELLOW);
                sender.spigot().sendMessage(msg);
                return true;
            }
        }
        return false;
    }
}
