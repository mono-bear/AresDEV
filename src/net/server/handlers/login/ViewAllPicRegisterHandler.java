package net.server.handlers.login;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.AbstractMaplePacketHandler;
import net.server.Server;
import tools.MaplePacketCreator;
import tools.Randomizer;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleClient;

public final class ViewAllPicRegisterHandler extends AbstractMaplePacketHandler { // Gey
																					// class
																					// name
																					// lol

	@Override
	public final void handlePacket(SeekableLittleEndianAccessor slea,
			MapleClient c) {
		slea.readByte();
		final int charId = slea.readInt();
		c.setWorld(slea.readInt()); // world
		final int channel = Randomizer.rand(0,
				Server.getInstance().getWorld(c.getWorld()).getChannels()
						.size());
		c.setChannel(channel);
		final String mac = slea.readMapleAsciiString();
		c.updateMacs(mac);
		if (c.hasBannedMac()) {
			c.getSession().close(true);
			return;
		}
		slea.readMapleAsciiString();
		final String pic = slea.readMapleAsciiString();
		c.setPic(pic);
		if (c.getIdleTask() != null) {
			c.getIdleTask().cancel(true);
		}
		c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
		final String[] socket = Server.getInstance()
				.getIP(c.getWorld(), channel).split(":");
		try {
			c.announce(MaplePacketCreator.getServerIP(
					InetAddress.getByName(socket[0]),
					Integer.parseInt(socket[1]), charId));
		} catch (final UnknownHostException e) {
		}
	}
}
