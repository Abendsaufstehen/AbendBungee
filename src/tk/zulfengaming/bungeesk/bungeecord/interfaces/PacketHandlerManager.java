package tk.zulfengaming.bungeesk.bungeecord.interfaces;

import tk.zulfengaming.bungeesk.bungeecord.handlers.PacketHandler;
import tk.zulfengaming.bungeesk.bungeecord.socket.Server;
import tk.zulfengaming.bungeesk.bungeecord.socket.packets.GlobalPlayers;
import tk.zulfengaming.bungeesk.bungeecord.socket.packets.Handshake;
import tk.zulfengaming.bungeesk.bungeecord.socket.packets.Heartbeat;
import tk.zulfengaming.bungeesk.universal.socket.Packet;
import tk.zulfengaming.bungeesk.universal.socket.PacketTypes;

import java.net.SocketAddress;
import java.util.ArrayList;


public class PacketHandlerManager {

    public ArrayList<PacketHandler> handlers = new ArrayList<>();

    // TODO: Refactor this!

    public PacketHandlerManager(Server serverIn) {
        handlers.add(new Handshake(serverIn));
        handlers.add(new Heartbeat(serverIn));
        handlers.add(new GlobalPlayers(serverIn));
    }

    public PacketHandler getHandler(Packet packetIn) {
        for (PacketHandler packetHandler : handlers)
            for (PacketTypes type : packetHandler.getTypes()) if (type == packetIn.getType()) return packetHandler;
        return null;
    }

    // ease of use. it's an absolute pain in the arse writing it out fully every time
    public Packet handlePacket(Packet packetIn, SocketAddress address) {
        return getHandler(packetIn).handlePacket(packetIn, address);
    }
}