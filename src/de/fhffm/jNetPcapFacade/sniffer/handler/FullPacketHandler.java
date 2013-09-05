/**
 * Copyright (c) 2013 JNetPcapFacade
 * 
 * This file is part of JNetPcapFacade.
 * JNetPcapFacade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNetPcapFacade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JNetPcapFacade. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhffm.jNetPcapFacade.sniffer.handler;


import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.packet.PcapPacket;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;

/**
 * 
 * @author Robin Müller-Bady
 *
 */
public class FullPacketHandler extends AbstractQueuePacketHandler {
	private static Logger log = LogManager.getLogger(FullPacketHandler.class);
	public FullPacketHandler(Queue<Packet> queue) {
		super(queue);
	}

	@Override
	protected boolean isPacketUsed(PcapPacket p) {
		log.trace("Accepted packet: " + p.toString());
		return true;
	}

}
