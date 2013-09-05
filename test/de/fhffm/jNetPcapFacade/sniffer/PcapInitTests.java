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

package de.fhffm.jNetPcapFacade.sniffer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jnetpcap.Pcap;
import org.junit.Test;

import de.fhffm.jNetPcapFacade.sniffer.util.NativeLibraryLoader;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;

/**
 * 
 * Simple tests for initializing jnetpcap.
 * 
 * @author Ruediger Gad
 * 
 */
public class PcapInitTests {
	
    @Test
    public void loadPcap() {
        @SuppressWarnings("unused")
        Pcap pcap;
    }

    @Test
    public void loadNativeLibs() {
    	NativeLibraryLoader.loadNativeLibs();
    }

    @Test
    public void testPcapVersion100() {
    	NativeLibraryLoader.loadNativeLibs();
        assertTrue(Pcap.isPcap100Loaded());
    }

    @Test
    public void testPcapVersion080() {
    	NativeLibraryLoader.loadNativeLibs();
        assertTrue(Pcap.isPcap080Loaded());
    }

    @Test
    public void activatePcapTest() {
        assertNotNull(PcapHelper.createAndActivatePcap());
    }
}
