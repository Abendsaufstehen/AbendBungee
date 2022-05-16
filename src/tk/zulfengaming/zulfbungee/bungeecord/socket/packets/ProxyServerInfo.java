package tk.zulfengaming.zulfbungee.bungeecord.socket.packets;


import tk.zulfengaming.zulfbungee.bungeecord.interfaces.PacketHandler;
import tk.zulfengaming.zulfbungee.bungeecord.socket.BaseServerConnection;
import tk.zulfengaming.zulfbungee.bungeecord.socket.Server;
import tk.zulfengaming.zulfbungee.universal.socket.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;

public class ProxyServerInfo extends PacketHandler {

    public ProxyServerInfo(Server serverIn) {
        super(serverIn, PacketTypes.PROXY_SERVER_INFO);

    }

    @Override
    public Packet handlePacket(Packet packetIn, BaseServerConnection connection) {

        ServerInfo serverInfo = (ServerInfo) packetIn.getDataSingle();

        // potentially update this atomically.
        connection.setClientInfo(serverInfo);

        InetSocketAddress socketAddressIn = (InetSocketAddress) connection.getAddress();

        InetAddress inetAddressIn = socketAddressIn.getAddress();
        int portIn = serverInfo.getMinecraftPort();

        for (Map.Entry<String, net.md_5.bungee.api.config.ServerInfo> info : getProxy().getServersCopy().entrySet()) {

            InetSocketAddress infoSockAddr = (InetSocketAddress) info.getValue().getSocketAddress();

            int infoPort = infoSockAddr.getPort();

            InetAddress infoInetAddr = infoSockAddr.getAddress();

            if (infoInetAddr.equals(inetAddressIn) && portIn == infoPort) {

                String name = info.getKey();
                getMainServer().addActiveConnection(connection, name);

                if (getMainServer().getPluginInstance().getConfig().getBoolean("global-scripts")) {
                    connection.send(new Packet(PacketTypes.GLOBAL_SCRIPT_HEADER, false, true, new ScriptInfo(ScriptAction.NEW,
                            getMainServer().getPluginInstance().getConfig()
                                    .getScriptNames().toArray(new String[0]))));
                }

                return new Packet(PacketTypes.CONNECTION_NAME, false, true, name);

            }
        }

        return null;
    }
}