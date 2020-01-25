package utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Contains IP adress utility methods
 * 
 * @author rkm
 *
 */
public class IpAddress {

	/**
	 * Default constructor
	 */
	public IpAddress() {

	}

	/**
	 * Returns the private ip adresse of the executing machine (works on windows and
	 * linux)
	 * 
	 * @return string containing the private ip address
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getPrivateIpAdress() throws UnknownHostException, SocketException {

		Enumeration en = NetworkInterface.getNetworkInterfaces();
		while (en.hasMoreElements()) {
			NetworkInterface i = (NetworkInterface) en.nextElement();
			for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
				InetAddress addr = (InetAddress) en2.nextElement();
				if (!addr.isLoopbackAddress()) {
					if (addr instanceof Inet4Address) {
						return addr.getHostAddress();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Check whether the given ip address (in dotted format) is valid
	 * 
	 * @param ip in dotted format
	 * @return true if ip is valid, false otherwise
	 */
	public static boolean isValid(String ip) {
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
		return ip.matches(PATTERN);
	}
}
