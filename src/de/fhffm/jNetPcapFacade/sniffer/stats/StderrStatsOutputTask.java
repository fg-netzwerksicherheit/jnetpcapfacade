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

package de.fhffm.jNetPcapFacade.sniffer.stats;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapStat;

/**
 * 
 * Timer used for writing packet capture statistics to stderr.
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class StderrStatsOutputTask extends TimerTask {
	private Logger log = LogManager.getLogger(StderrStatsOutputTask.class);
    private Pcap pcap;
    private PcapStat stats = new PcapStat();
    private StringBuilder stringBuilder = new StringBuilder();
    private Timer timer = new Timer();

    public StderrStatsOutputTask(Pcap pcap) {
        this.pcap = pcap;
        timer.schedule(this, 1000, 1000);
    }

    @Override
    public void run() {
        pcap.stats(stats);
        stringBuilder.setLength(0);
        stringBuilder.append("Packets on the network: ");
        stringBuilder.append(stats.getRecv());
        stringBuilder.append(" Packets dropped: ");
        stringBuilder.append(stats.getDrop());
        log.debug(stringBuilder.toString());
        System.err.println(stringBuilder.toString());
    }
    
    @Override
    public boolean cancel() {
        timer.cancel();
        return super.cancel();
    }
    

}
