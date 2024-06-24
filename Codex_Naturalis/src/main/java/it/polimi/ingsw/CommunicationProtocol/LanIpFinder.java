package it.polimi.ingsw.CommunicationProtocol;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * This class provides a utility to find the local LAN IP address.
 */
public class LanIpFinder {

    /**
     * Retrieves the local LAN IP address.
     *
     * @return The local LAN IP address as a String.
     */
    public static String getLAN_IP(){
        try {
            InetAddress lanAddress = null;

            // Get all network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            // Iterate through all network interfaces
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // Check if the interface is not a loopback and is up
                if (!networkInterface.getDisplayName().contains("Ethernet")
                        &&!networkInterface.isLoopback() && networkInterface.isUp() && !networkInterface.isVirtual()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    // Iterate through all addresses associated with the interface
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();

                        // Check if the address is IPv4
                        if (address instanceof Inet4Address) {
                            lanAddress = address;   // Store the IPv4 address
                            break;  // Exit the loop since we found an IPv4 address
                        }
                    }
                    if (lanAddress != null) {
                        break;  // Exit the loop since we found an IPv4 address
                    }
                }
            }

            // If a LAN IP address is found, return it
            if (lanAddress != null) {
                return lanAddress.getHostAddress();
            }
            else {
                // If no LAN IP address is found, throw an exception
                throw new Exception("Could not locate local LAN IP address, returning localhost address");
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        // Return the default localhost address if no LAN IP address is found
        return "127.0.1.1";
    }
}
