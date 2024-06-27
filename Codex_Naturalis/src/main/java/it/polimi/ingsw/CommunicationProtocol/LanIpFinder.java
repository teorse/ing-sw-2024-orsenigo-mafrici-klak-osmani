package it.polimi.ingsw.CommunicationProtocol;

import it.polimi.ingsw.Client.Model.ClientModel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * This class provides a utility to find the local LAN IP address.
 */
public class LanIpFinder {

    // SINGLETON PATTERN
    private static LanIpFinder INSTANCE;
    public synchronized static LanIpFinder getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LanIpFinder();
        }
        return INSTANCE;
    }


    private String argumentIp = "";

    public void setIp(String argumentIp){
        this.argumentIp = argumentIp;
    }


    /**
     * Retrieves the local LAN IP address.
     *
     * @return The local LAN IP address as a String.
     */
    public String getLAN_IP() {
        if (argumentIp.isEmpty()) {
            try {
                InetAddress lanAddress = null;

                // Get all network interfaces
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

                // Iterate through all network interfaces
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();

                    // Check if the interface is not a loopback and is up
                    if (!networkInterface.getDisplayName().contains("Ethernet")
                            && !networkInterface.isLoopback() && networkInterface.isUp() && !networkInterface.isVirtual()) {
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
                } else {
                    // If no LAN IP address is found, throw an exception
                    throw new Exception("Could not locate local LAN IP address, returning localhost address");
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e);
            }

            // Return the default localhost address if no LAN IP address is found
            return "127.0.1.1";
        }
        else
            return argumentIp;
    }


    /**
     * Validates an IPv4 address.
     * This method checks if the provided string is a valid IPv4 address. A valid IPv4 address
     * consists of four octets, each ranging from 0 to 255, separated by periods (".").
     *
     * @param ip the string representation of the IPv4 address to validate
     * @return {@code true} if the string is a valid IPv4 address, {@code false} otherwise
     */
    public static boolean isValidIPAddress(String ip) {
        String[] parts = ip.split("\\.");

        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
