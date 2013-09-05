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

package de.fhffm.jNetPcapFacade.config;

import java.io.File;

import org.jnetpcap.Pcap;

/**
 * 
 * @author Ruediger Gad
 * 
 */
public class Constants {

	/* NativeLibraryLoader */
	public static final String SYSTEM_PROPERTY_ARCH = "os.arch";
	public static final String SYSTEM_PROPERTY_OS = "os.name";
	public static final String OS_VALUE_WINDOWS = "windows";
	public static final String PREFIX_LIBRARAY_WINDOWS = "";
	public static final String SUFFIX_LIBRARAY_WINDOWS = ".dll";
	public static final String PREFIX_LIBRARAY_LINUX = "lib";
	public static final String SUFFIX_LIBRARAY_LINUX = ".so";
	
	/* General */
    public static final String NAME = "jNetPcapFacade";
    public static final String VERSION = "0.1.0";

    /* Paths */
    public static final String JNETPCAP_PATH = "jnetpcap";
    public static final String NATIVE_JNETPCAP_PATH = JNETPCAP_PATH
            + File.separator + "native";

    /* Library names */
    public static final String PCAP_LIB_080 = "jnetpcap";
    public static final String PCAP_LIB_100 = "jnetpcap-pcap100";

    /* jnetpcap options */
    public static final String ANY_DEVICE = "any";
    public static final String LOOPBACK_DEVICE = "lo";
    public static final int SNAPLEN = 64 * 1024;
    public static final int FLAGS = Pcap.MODE_PROMISCUOUS;
    public static final int DIRECTION = Pcap.INOUT;
    public static final int BUFFER_SIZE = 128 * 1024 * 1024;
    public static final byte[] DEFAULT_MAC_ADDRESS = new byte[] { 0, 0, 0, 0,
            0, 0 };
    
    static{
        System.err.println("Using: " + NAME + " " + VERSION + "\n");
    }

}
