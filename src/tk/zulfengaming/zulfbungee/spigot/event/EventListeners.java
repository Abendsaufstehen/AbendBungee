package tk.zulfengaming.zulfbungee.spigot.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tk.zulfengaming.zulfbungee.spigot.event.events.EventMessage;
import tk.zulfengaming.zulfbungee.spigot.event.events.EventPlayerServerDisconnect;
import tk.zulfengaming.zulfbungee.spigot.event.events.EventPlayerServerKick;
import tk.zulfengaming.zulfbungee.spigot.event.events.EventPlayerSwitchServer;

public class EventListeners implements Listener {

    @EventHandler
    public void onPlayerSwitchServer(EventPlayerSwitchServer event) {

    }

    @EventHandler
    public void onServerMessage(EventMessage event) {

    }

    @EventHandler
    public void onPlayerDisconnect(EventPlayerServerDisconnect event) {

    }

    @EventHandler
    public void onPlayerKick(EventPlayerServerKick event) {

    }


}
