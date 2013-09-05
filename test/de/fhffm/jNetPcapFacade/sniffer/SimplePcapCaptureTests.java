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

package de.fhffm.jNetPcapFacade.sniffer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhffm.jNetPcapFacade.config.Constants;
import de.fhffm.jNetPcapFacade.config.TestConstants;
import de.fhffm.jNetPcapFacade.sniffer.util.NativeLibraryLoader;

/**
 * 
 * Simple tests for capturing data. From here on we need root privileges or at
 * least permissions to access the network devices.
 * 
 * @author Ruediger Gad
 * 
 */
public class SimplePcapCaptureTests {
    private Pcap pcap = null;

    @BeforeClass
    public static void init() {
        NativeLibraryLoader.loadNativeLibs();
    }

    @Test
    public void getDeviceList() {
        List<PcapIf> devices = new ArrayList<PcapIf>();
        StringBuilder err = new StringBuilder();

        if (Pcap.findAllDevs(devices, err) != Pcap.OK || devices.isEmpty()) {
            throw new RuntimeException("Failed to find network devices!");
        }

        for (PcapIf dev : devices) {
            System.out.println(dev.getName() + " " + dev.getDescription());
        }
    }

    @Test
    public void openAnyDevice() {
        List<PcapIf> devices = new ArrayList<PcapIf>();
        StringBuilder err = new StringBuilder();

        if (Pcap.findAllDevs(devices, err) != Pcap.OK || devices.isEmpty()) {
            throw new RuntimeException("Failed to find network devices!");
        }

        Pcap pcap = null;
        for (PcapIf dev : devices) {
            if (dev.getName().equalsIgnoreCase(Constants.ANY_DEVICE)) {
                pcap = Pcap.create(Constants.ANY_DEVICE, err);
            }
        }

        if (pcap == null) {
            throw new RuntimeException("Failed to open \""
                    + Constants.ANY_DEVICE + "\" device!");
        }
        
        pcap.close();
    }

    @Test
    public void activateAnyDevice() {
        List<PcapIf> devices = new ArrayList<PcapIf>();
        StringBuilder err = new StringBuilder();

        if (Pcap.findAllDevs(devices, err) != Pcap.OK || devices.isEmpty()) {
            throw new RuntimeException("Failed to find network devices!");
        }

        for (PcapIf dev : devices) {
            if (dev.getName().equalsIgnoreCase(Constants.ANY_DEVICE)) {
                pcap = Pcap.create(Constants.ANY_DEVICE, err);
            }
        }

        if (pcap == null) {
            throw new RuntimeException("Failed to open \""
                    + Constants.ANY_DEVICE + "\" device!");
        }

        pcap.setSnaplen(Constants.SNAPLEN);
        pcap.setPromisc(Constants.FLAGS);
        pcap.setBufferSize(Constants.BUFFER_SIZE);

        if (pcap.activate() != 0) {
            throw new RuntimeException("Failed to activate \""
                    + Constants.ANY_DEVICE + "\" device!");
        }

        /*
         * The pcap.loop is blocking so we use a thread as timer to stop
         * capturing.
         */
        Thread stopper = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(TestConstants.CAPTURE_DELAY);
                    pcap.breakloop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        stopper.start();

        int ret = pcap.loop(Pcap.LOOP_INFINITE,
                new PcapPacketHandler<Object>() {
                    @Override
                    public void nextPacket(PcapPacket arg0, Object arg1) {
                    }
                }, null);

        /*
         * As we break the loop we check the return value if this was correctly
         * done.
         */
        assertEquals(Pcap.ERROR_BREAK, ret);
        pcap.close();
    }

}
