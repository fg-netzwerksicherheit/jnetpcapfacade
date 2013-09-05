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
import org.jnetpcap.packet.PcapPacketHandler;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;

/**
 * 
 * Basic packet handler for storing received packets in a queue.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public abstract class AbstractQueuePacketHandler implements PcapPacketHandler<Object> {
	private static Logger log = LogManager.getLogger(AbstractQueuePacketHandler.class);
    protected Queue<Packet> queue;

    public AbstractQueuePacketHandler(Queue<Packet> queue) {
        this.queue = queue;
    }

    @Override
    public void nextPacket(PcapPacket p, Object o) {
        long hpTimestamp = System.nanoTime();
        long timestamp = System.currentTimeMillis();
        log.trace("Received packet: " + p.toDebugString());
        if(isPacketUsed(p)){
        	queue.offer(new Packet(timestamp, hpTimestamp, new PcapPacket(p)));
        }
    }
    
    protected abstract boolean isPacketUsed(PcapPacket p);
}
