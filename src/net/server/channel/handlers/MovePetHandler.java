/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.channel.handlers;

import java.util.List;

import server.movement.LifeMovementFragment;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleCharacter;
import client.MapleClient;

public final class MovePetHandler extends AbstractMovementPacketHandler {
	@Override
	public final void handlePacket(SeekableLittleEndianAccessor slea,
			MapleClient c) {
		final int petId = slea.readInt();
		slea.readLong();
		// Point startPos = StreamUtil.readShortPoint(slea);
		final List<LifeMovementFragment> res = this.parseMovement(slea);
		if (res.isEmpty()) {
			return;
		}
		final MapleCharacter player = c.getPlayer();
		final byte slot = player.getPetIndex(petId);
		if (slot == -1) {
			return;
		}
		player.getPet(slot).updatePosition(res);
		player.getMap().broadcastMessage(player,
				MaplePacketCreator.movePet(player.getId(), petId, slot, res),
				false);
	}
}