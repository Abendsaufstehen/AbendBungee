package tk.zulfengaming.zulfbungee.spigot.handlers;

import net.md_5.bungee.api.ChatColor;
import tk.zulfengaming.zulfbungee.spigot.interfaces.ClientListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketHandler extends ClientListener implements Runnable {

    public SocketHandler(ClientListenerManager clientListenerManagerIn) {
        super(clientListenerManagerIn);
    }

    @Override
    public void run() {

        Socket socket = new Socket();

        try {

            socket.setReuseAddress(true);

            socket.bind(new InetSocketAddress(getClientListenerManager().getClientAddress(), getClientListenerManager().getClientPort()));
            socket.connect(new InetSocketAddress(getClientListenerManager().getServerAddress(), getClientListenerManager().getServerPort()));

            getClientListenerManager().getSocketRetrieve().offer(socket);

        } catch (IOException connecting) {

            getClientListenerManager().getPluginInstance().logDebug(ChatColor.RED + "SocketHandler exception: " + connecting.getMessage());

            try {
                socket.close();
            } catch (IOException closing) {
                getClientListenerManager().getPluginInstance().error("Error closing unused socket:");
                closing.printStackTrace();
            }

        }
    }
}