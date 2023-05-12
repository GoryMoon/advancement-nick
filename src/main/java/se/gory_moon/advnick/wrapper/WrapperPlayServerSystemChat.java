package se.gory_moon.advnick.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import net.kyori.adventure.text.Component;

public class WrapperPlayServerSystemChat extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.SYSTEM_CHAT;

	public WrapperPlayServerSystemChat() {
		super(TYPE);
	}

	public WrapperPlayServerSystemChat(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieves the value of field 'adventure$content'
	 * ProtocolLib currently does not provide a wrapper for this type. Access to this type is only provided by an InternalStructure
	 *
	 * @return 'adventure$content'
	 */
	public Component getAdventureContent() {
		return this.handle.getSpecificModifier(Component.class).read(0);
	}

	/**
	 * Sets the value of field 'adventure$content'
	 * ProtocolLib currently does not provide a wrapper for this type. Access to this type is only provided by an InternalStructure
	 *
	 * @param value New value for field 'adventure$content'
	 */
	public void setAdventureContent(Component value) {
		this.handle.getSpecificModifier(Component.class).write(0, value);
	}

	/**
	 * Retrieves the value of field 'content'
	 *
	 * @return 'content'
	 */
	public String getContent() {
		return this.handle.getStrings().read(0);
	}

	/**
	 * Sets the value of field 'content'
	 *
	 * @param value New value for field 'content'
	 */
	public void setContent(String value) {
		this.handle.getStrings().write(0, value);
	}

	/**
	 * Retrieves the value of field 'overlay'
	 *
	 * @return 'overlay'
	 */
	public boolean getOverlay() {
		return this.handle.getBooleans().read(0);
	}

	/**
	 * Sets the value of field 'overlay'
	 *
	 * @param value New value for field 'overlay'
	 */
	public void setOverlay(boolean value) {
		this.handle.getBooleans().write(0, value);
	}

}