package tk.zulfengaming.bungeesk.bungeecord.socket.packets;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import tk.zulfengaming.bungeesk.bungeecord.handlers.PacketHandler;
import tk.zulfengaming.bungeesk.bungeecord.socket.Server;
import tk.zulfengaming.bungeesk.universal.socket.Packet;
import tk.zulfengaming.bungeesk.universal.socket.PacketTypes;
import tk.zulfengaming.bungeesk.universal.utilclasses.skript.ProxyPlayer;

import java.net.SocketAddress;
import java.util.Collection;

public class GlobalPlayers extends PacketHandler {

    public GlobalPlayers(Server serverIn) {
        super(serverIn, false, PacketTypes.GLOBAL_PLAYERS);

    }

    // not implemented
    @Override
    public Packet handlePacket(Packet packetIn, SocketAddress address) {

        final Collection<ProxiedPlayer> players = getProxy().getPlayers();

        ProxyPlayer[] convertedPlayers = players.stream()
                .map(proxiedPlayer -> new ProxyPlayer(proxiedPlayer.getName(), proxiedPlayer.getUniqueId()))
                .toArray(ProxyPlayer[]::new);

        return new Packet(PacketTypes.GLOBAL_PLAYERS, true, false, convertedPlayers);

    }
}
