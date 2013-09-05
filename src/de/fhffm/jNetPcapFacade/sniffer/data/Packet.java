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

package de.fhffm.jNetPcapFacade.sniffer.data;

import org.jnetpcap.packet.PcapPacket;

/**
 * 
 * Class which wraps {@link PcapPacket} in order to add further data like
 * timestamps.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class Packet implements Cloneable {
    private final PcapPacket pcapPacket;
    private final long timestamp;
    private final long highPrecisionTimestamp;

    /**
     * Performs a deep copy including the underlying {@link PcapPacket}.
     * 
     * @param p
     */
    public Packet(Packet p) {
        pcapPacket = new PcapPacket(p.pcapPacket);
        timestamp = p.timestamp;
        highPrecisionTimestamp = p.highPrecisionTimestamp;
    }

    public Packet(long timestamp, PcapPacket pcapPacket) {
        this(timestamp, 0, pcapPacket);
    }

    public Packet(long timestamp, long highPrecisionTimestamp,
            PcapPacket pcapPacket) {
        this.pcapPacket = pcapPacket;
        this.timestamp = timestamp;
        this.highPrecisionTimestamp = highPrecisionTimestamp;
    }

    public PcapPacket getPcapPacket() {
        return pcapPacket;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Please not that this is a relative timestamp as returned by
     * {@link System#nanoTime()}.
     * 
     * @return
     */
    public long getHighPrecisionTimestamp() {
        return highPrecisionTimestamp;
    }

    /**
     * Well, this is quite redundant... but there it goes.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Packet(this);
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<");
    	sb.append(getTimestamp());
    	sb.append(": ");
    	sb.append(pcapPacket.toDebugString());
    	sb.append(">");
    	return sb.toString();
    }

}
