package tk.zulfengaming.zulfbungee.universal.managers.transport;

import tk.zulfengaming.zulfbungee.universal.ZulfBungeeProxy;
import tk.zulfengaming.zulfbungee.universal.command.util.ChatColour;
import tk.zulfengaming.zulfbungee.universal.handlers.transport.ProxyChannelCommHandler;
import tk.zulfengaming.zulfbungee.universal.interfaces.MessageCallback;
import tk.zulfengaming.zulfbungee.universal.managers.MainServer;
import tk.zulfengaming.zulfbungee.universal.socket.transport.ChannelServerConnection;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelMainServer<P, T> extends MainServer<P, T> {

    private final ConcurrentHashMap<String, ChannelServerConnection<P, T>> channelConnections = new ConcurrentHashMap<>();

    public ChannelMainServer(ZulfBungeeProxy<P, T> instanceIn) {
        super(instanceIn);
        pluginInstance.registerMessageChannel("zproxy:channel");
        pluginInstance.logInfo(ChatColour.GREEN + "Waiting for a player to join...");
    }

    @Override
    public void end() throws IOException {
        pluginInstance.unregisterMessageChannel("zproxy:channel");
        super.end();
    }

    public void acceptMessagingConnection(SocketAddress addressIn, String serverName, MessageCallback callbackIn) {
        ChannelServerConnection<P, T> connection = new ChannelServerConnection<>(this, callbackIn, addressIn);
        channelConnections.put(serverName, connection);
        startConnection(connection);
    }

    public void proccessPluginMessage(String serverNameIn, byte[] dataIn) {

        if (channelConnections.containsKey(serverNameIn)) {
            ProxyChannelCommHandler<P, T> channelCommHandler = channelConnections.get(serverNameIn).getProxyChannelCommHandler();
            channelCommHandler.provideBytes(dataIn);
        }

    }

    public boolean isChannelConnectionActive(String nameIn) {
        return channelConnections.containsKey(nameIn);
    }

    @Override
    public void removeServerConnection(String name, SocketAddress address) {

        channelConnections.remove(name);
        // we register the channel again just to be sure
        pluginInstance.registerMessageChannel("zproxy:channel");

        if (channelConnections.isEmpty()) {
            pluginInstance.logInfo(ChatColour.GREEN + "Waiting for a player to join...");
        }

        super.removeServerConnection(name, address);

    }

}
