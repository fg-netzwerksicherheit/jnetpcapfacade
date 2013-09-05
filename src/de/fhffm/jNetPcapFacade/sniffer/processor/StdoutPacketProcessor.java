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

package de.fhffm.jNetPcapFacade.sniffer.processor;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.util.ToStringHelper;

/**
 * Outputs data about packets to stdout.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class StdoutPacketProcessor implements PacketProcessor {
	private Logger log = LogManager.getLogger(StdoutPacketProcessor.class);
    private Ethernet ethernet = new Ethernet();
    private Ip4 ip4 = new Ip4();
    private Ip6 ip6 = new Ip6();
    private Tcp tcp = new Tcp();
    private Udp udp = new Udp();

    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void forwardPacket(Packet packet) {
        PcapPacket pcapPacket = packet.getPcapPacket();
        
        stringBuilder.setLength(0);

        if (pcapPacket.hasHeader(ethernet)) {
            stringBuilder.append("EthSrc:");
            appendEthernetAddress(ethernet.source());
            stringBuilder.append(" EthDst:");
            appendEthernetAddress(ethernet.destination());
        }

        if (pcapPacket.hasHeader(ip4)) {
            stringBuilder.append(" Src:");
            appendIpAddress(ip4.source());
            stringBuilder.append(" Dst:");
            appendIpAddress(ip4.destination());
        }

        else if (pcapPacket.hasHeader(ip6)) {
            stringBuilder.append("Src:");
            appendIpAddress(ip6.source());
            stringBuilder.append(" Dst:");
            appendIpAddress(ip6.destination());
        }

        if (pcapPacket.hasHeader(tcp)) {
            stringBuilder.append(" SrcPort:");
            stringBuilder.append(tcp.source());
            stringBuilder.append(" DstPort:");
            stringBuilder.append(tcp.destination());
            stringBuilder.append(" ACK:");
            stringBuilder.append(tcp.flags_ACK());
            stringBuilder.append(" SYN:");
            stringBuilder.append(tcp.flags_SYN());
            stringBuilder.append(" FIN:");
            stringBuilder.append(tcp.flags_FIN());
        }

        else if (pcapPacket.hasHeader(udp)) {
            stringBuilder.append(" SrcPort:");
            stringBuilder.append(udp.source());
            stringBuilder.append(" DstPort:");
            stringBuilder.append(udp.destination());
        }
        log.debug("Packet: " + stringBuilder.toString());
        System.out.println(stringBuilder.toString());
    }

    private void appendEthernetAddress(byte[] address) {
        stringBuilder.append(ToStringHelper.EthernetToString(address));
    }

    private void appendIpAddress(byte[] address) {
        stringBuilder.append(ToStringHelper.IpToString(address));
    }

}
