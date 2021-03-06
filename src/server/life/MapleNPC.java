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
package server.life;

import server.MapleShopFactory;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import client.MapleClient;

public class MapleNPC extends AbstractLoadedMapleLife {
	private final MapleNPCStats stats;

	public MapleNPC(int id, MapleNPCStats stats) {
		super(id);
		this.stats = stats;
	}

	public boolean hasShop() {
		return MapleShopFactory.getInstance().getShopForNPC(this.getId()) != null;
	}

	public void sendShop(MapleClient c) {
		MapleShopFactory.getInstance().getShopForNPC(this.getId()).sendShop(c);
	}

	@Override
	public void sendSpawnData(MapleClient client) {
		if (!this.isHidden()) {
			if ((this.getId() > 9010010) && (this.getId() < 9010014)) {
				client.announce(MaplePacketCreator.spawnNPCRequestController(
						this, false));
			} else {
				client.announce(MaplePacketCreator.spawnNPC(this));
				client.announce(MaplePacketCreator.spawnNPCRequestController(
						this, true));
			}
		}
	}

	@Override
	public void sendDestroyData(MapleClient client) {
		client.announce(MaplePacketCreator.removeNPC(this.getObjectId()));
	}

	@Override
	public MapleMapObjectType getType() {
		return MapleMapObjectType.NPC;
	}

	public String getName() {
		return this.stats.getName();
	}
}
