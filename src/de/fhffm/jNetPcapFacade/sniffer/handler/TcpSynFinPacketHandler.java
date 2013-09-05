/**
 * Copyright (c) 2013 JNetPcapFacade
 * 
 * This file is part of JNetPcapFacade.
 * JNetPcapFacade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNetPcapFacade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JNetPcapFacade. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhffm.jNetPcapFacade.sniffer.handler;


import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;

/**
 * This is a brute force way to do the same as a natively compiled filter like
 * &quot;tcp[tcpflags] & (tcp-syn|tcp-fin) != 0&quot; does. In the unit tests it
 * showed though that there may be some problems with respect to native
 * filtering. 
 * 
 * @see org.jnetpcap.PcapBpfProgram
 * @see PcapHelper#setFilter(org.jnetpcap.Pcap, String)
 * @see de.fhffm.jNetPcapFacade.sniffer.handler.HandlerTests
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class TcpSynFinPacketHandler extends AbstractQueuePacketHandler {
	private static Logger log = LogManager.getLogger(TcpSynFinPacketHandler.class);
    private Tcp tcp = new Tcp();

    public TcpSynFinPacketHandler(Queue<Packet> queue) {
        super(queue);
    }

	@Override
	protected boolean isPacketUsed(PcapPacket p) {
		if(p.hasHeader(tcp) && (tcp.flags_SYN() || tcp.flags_FIN())){
			log.trace("Accepted packet: " + p.toString());
			return true;
		}
		else{
			log.trace("Rejected packet: " + p.toString());
			return false;
		}
	}
}
