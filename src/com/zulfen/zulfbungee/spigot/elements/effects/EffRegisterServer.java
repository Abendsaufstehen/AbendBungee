package com.zulfen.zulfbungee.spigot.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.zulfen.zulfbungee.spigot.ZulfBungeeSpigot;
import com.zulfen.zulfbungee.universal.socket.objects.Packet;
import com.zulfen.zulfbungee.universal.socket.objects.PacketTypes;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class EffRegisterServer extends Effect {

    private Expression<String> serverName;
    private Expression<String> address;
    private Expression<Integer> port;

    static {
        Skript.registerEffect(EffRegisterServer.class, "register [new] server [with the proxy] [named] %string% with (IP|address) %string% [and] [with] port %number%");
    }

    @Override
    protected void execute(@NotNull Event event) {

        Integer portOut = port.getSingle(event);
        String addressOut = address.getSingle(event);
        String serverNameOut = serverName.getSingle(event);

        ZulfBungeeSpigot.getPlugin().getConnectionManager().sendDirect(new Packet(PacketTypes.REGISTER_SERVER, false, true,
                new Object[]{serverNameOut, addressOut, portOut}));

    }

    @Override
    public String toString(Event e, boolean debug) {
        return "effect register server";
    }

    // i would check if a valid address was supplied here, but idk how to check the value in init properly. let the proxy handle it ig.
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        serverName = (Expression<String>) exprs[0];
        address = (Expression<String>) exprs[1];
        port = (Expression<Integer>) exprs[2];
        return true;
    }
}
