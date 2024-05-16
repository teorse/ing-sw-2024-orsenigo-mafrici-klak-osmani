package Client.Network;

import Network.NetworkConstants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RMIClientSocketFactory implements java.rmi.server.RMIClientSocketFactory {
    @Override
    public Socket createSocket(String ip, int port) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), NetworkConstants.ServerSocketTimeOut);

        return socket;
    }
}
