package tk.zulfengaming.zulfbungee.spigot.socket.packets;

import tk.zulfengaming.zulfbungee.spigot.event.events.EventPlayerSwitchServer;
import tk.zulfengaming.zulfbungee.spigot.handlers.PacketHandler;
import tk.zulfengaming.zulfbungee.spigot.socket.ClientConnection;
import tk.zulfengaming.zulfbungee.universal.socket.Packet;
import tk.zulfengaming.zulfbungee.universal.socket.PacketTypes;
import tk.zulfengaming.zulfbungee.universal.utilclasses.skript.ProxyPlayer;

import java.net.SocketAddress;

public class SwitchServerEvent extends PacketHandler {

    public SwitchServerEvent(ClientConnection connection) {
        super(connection, PacketTypes.SWITCH_SERVER_EVENT);

    }

    @Override
    public Packet handlePacket(Packet packetIn, SocketAddress address) {

        ProxyPlayer player = (ProxyPlayer) packetIn.getDataSingle();

        getConnection().getPluginInstance().getServer().getPluginManager().callEvent(
                new EventPlayerSwitchServer(player)
        );

        return null;

    }
}
