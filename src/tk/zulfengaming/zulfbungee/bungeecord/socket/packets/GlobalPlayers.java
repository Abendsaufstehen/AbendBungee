package tk.zulfengaming.zulfbungee.bungeecord.socket.packets;

import tk.zulfengaming.zulfbungee.bungeecord.interfaces.PacketHandler;
import tk.zulfengaming.zulfbungee.bungeecord.socket.Server;
import tk.zulfengaming.zulfbungee.universal.socket.Packet;
import tk.zulfengaming.zulfbungee.universal.socket.PacketTypes;
import tk.zulfengaming.zulfbungee.universal.util.skript.ProxyPlayer;
import tk.zulfengaming.zulfbungee.universal.util.skript.ProxyServer;

import java.net.SocketAddress;
import java.util.LinkedList;

public class GlobalPlayers extends PacketHandler {

    public GlobalPlayers(Server serverIn) {
        super(serverIn, PacketTypes.GLOBAL_PLAYERS);

    }

    // not implemented
    @Override
    public Packet handlePacket(Packet packetIn, SocketAddress address) {

        LinkedList<ProxyPlayer> playersOut = new LinkedList<>();

        for (ProxyServer server : getMainServer().getServers().values()) {
            playersOut.addAll(server.getPlayers());
        }

        return new Packet(PacketTypes.GLOBAL_PLAYERS, false, false, playersOut.toArray(new ProxyPlayer[0]));

    }
}
