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

package de.fhffm.jNetPcapFacade.sniffer.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;

import de.fhffm.jNetPcapFacade.config.Constants;

/**
 * 
 * Helper class for setting up jnetpcap etc.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 */
public class PcapHelper {
	private static Logger log = LogManager.getLogger(PcapHelper.class);
    
	public static Pcap createAndActivatePcap() {
        return createAndActivatePcap(Constants.ANY_DEVICE);
    }

    /**
     * 
     * Sets a filter for a given {@link Pcap} instance. The filterExpression is
     * a String following the format as explained e.g. in PCAP-FILTER(7).
     * 
     * @param pcap
     * @param filterExpression
     */
    public static void setFilter(Pcap pcap, String filterExpression) {
        PcapBpfProgram filter = new PcapBpfProgram();
        if (pcap.compile(filter, filterExpression, 1, 0) != Pcap.OK) {
        	log.error("Filter not compilable: " + pcap.getErr());
        	throw new RuntimeException("Failed to compile filter: "
                    + pcap.getErr());
        }
        if (pcap.setFilter(filter) != Pcap.OK) {
        	log.error("Filter not settable: " + pcap.getErr());
            throw new RuntimeException("Failed to set filter: " + pcap.getErr());
        }
    }

    public static Pcap createAndActivatePcap(String device) {
    	NativeLibraryLoader.loadNativeLibs();
        Pcap pcap = null;

        List<PcapIf> devices = new ArrayList<PcapIf>();
        StringBuilder err = new StringBuilder();

        if (Pcap.findAllDevs(devices, err) != Pcap.OK || devices.isEmpty()) {
        	log.error("Failed to find network devices");
            throw new RuntimeException("Failed to find network devices!");
        }

        for (PcapIf dev : devices) {
            if (dev.getName().equalsIgnoreCase(device)) {
                pcap = Pcap.create(device, err);
                break;
            }
        }

        if (pcap == null && devices.size() < 1) {
        	log.error("Failed to open device: " + device);
            throw new RuntimeException("Failed to open \"" + device + "\" device!");
        } else if (pcap == null) {
        	log.error("Failed to get requested device: " + device);
            device = devices.get(0).getName();
            log.error("Using first device found: " + device);
            pcap = Pcap.create(device, err);
        }

        pcap.setSnaplen(Constants.SNAPLEN);
        pcap.setPromisc(Constants.FLAGS);
        pcap.setBufferSize(Constants.BUFFER_SIZE);

        if (pcap.activate() != 0) {
        	log.error("Failed to activate device: " + device + " -> " + pcap.getErr());
            throw new RuntimeException("Failed to activate \"" + device + "\" device!\n" + "Pcap error follows:\n" + pcap.getErr());
        }

        return pcap;
    }

    public static String listDevices() {
        String ret = "";
    	NativeLibraryLoader.loadNativeLibs();
        List<PcapIf> devices = new ArrayList<PcapIf>();
        StringBuilder err = new StringBuilder();

        if (Pcap.findAllDevs(devices, err) != Pcap.OK || devices.isEmpty()) {
        	log.error("Failed to find network devices");
            throw new RuntimeException("Failed to find network devices!");
        }

        for (PcapIf dev : devices) {
            ret += dev.getName() + "\n";
        }

        return ret;
    }

}
