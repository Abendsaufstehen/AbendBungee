package com.zulfen.zulfbungee.spigot.managers;

import com.zulfen.zulfbungee.spigot.interfaces.PacketHandler;
import com.zulfen.zulfbungee.spigot.socket.Connection;
import com.zulfen.zulfbungee.spigot.socket.packets.*;
import com.zulfen.zulfbungee.universal.socket.objects.Packet;
import com.zulfen.zulfbungee.universal.socket.objects.PacketTypes;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;


public class PacketHandlerManager {

    private final TaskManager taskManager;

    private final ArrayList<PacketHandler> handlers = new ArrayList<>();

    public PacketHandlerManager(Connection connectionIn) {
        this.taskManager = connectionIn.getPluginInstance().getTaskManager();
        addHandler(new ConnectionName(connectionIn));
        addHandler(new ServerSwitchEvent(connectionIn));
        addHandler(new ServerMessageEvent(connectionIn));
        addHandler(new PlayerDisconnectEvent(connectionIn));
        addHandler(new PlayerConnectEvent(connectionIn));
        addHandler(new InvalidConfiguration(connectionIn));
        addHandler(new ProxyServerInfo(connectionIn));
        addHandler(new ServerKickEvent(connectionIn));
        addHandler(new GlobalScript(connectionIn));
        addHandler(new PlayerSendMessage(connectionIn));
        addHandler(new ProxyPlayerCommand(connectionIn));
        addHandler(new ConsoleCommand(connectionIn));
    }

    public void addHandler(PacketHandler handlerIn) {
        handlers.add(handlerIn);
    }

    public PacketHandler getHandler(Packet packetIn) {

        for (PacketHandler handler : handlers) {
            for (PacketTypes type : handler.getTypes()) {
                if (packetIn.getType() == type) {
                    return handler;
                }
            }
        }

        return null;
    }

    // ease of use. it's an absolute pain in the arse writing it out fully every time
    public void handlePacket(Packet packetIn) {

        PacketHandler handler = getHandler(packetIn);

        if (handler.isAsync()) {
            taskManager.newAsyncTask(new BukkitRunnable() {
                @Override
                public void run() {
                    handler.handlePacket(packetIn);
                }
            });
        } else {
            handler.handlePacket(packetIn);
        }

    }
}