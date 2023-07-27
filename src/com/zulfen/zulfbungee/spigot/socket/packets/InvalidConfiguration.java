package com.zulfen.zulfbungee.spigot.socket.packets;

import com.zulfen.zulfbungee.spigot.interfaces.PacketHandler;
import com.zulfen.zulfbungee.spigot.socket.Connection;
import com.zulfen.zulfbungee.universal.socket.objects.Packet;
import com.zulfen.zulfbungee.universal.socket.objects.PacketTypes;

public class InvalidConfiguration extends PacketHandler {

    public InvalidConfiguration(Connection connectionIn) {
        super(connectionIn, false, PacketTypes.INVALID_CONFIGURATION);

    }

    // maybe will use now, kind of unused.

    @Override
    public void handlePacket(Packet packetIn) {

        getConnection().getPluginInstance().warning("The proxy disconnected you due to a configuration issue!");
        getConnection().getPluginInstance().warning("This client will not try and reconnect until this issue is fixed.");
        getConnection().getPluginInstance().warning("Check the proxy's console for more information.");

        getConnection().getPluginInstance().getConnectionManager().blockConnection(getConnection());

    }
}