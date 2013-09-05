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

package de.fhffm.jNetPcapFacade.helper;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.processor.PacketProcessor;

public class ProcessorMockup implements PacketProcessor {

	public int packetCount = 0;
	
	@Override
	public void forwardPacket(Packet packet) {
		packetCount++;
	}
}
