package tk.zulfengaming.zulfbungee.spigot.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import tk.zulfengaming.zulfbungee.spigot.handlers.ProxyServerInfoManager;
import tk.zulfengaming.zulfbungee.universal.util.skript.ProxyServer;

import java.util.Arrays;

public class ExprProxyServers extends SimpleExpression<ProxyServer> {

    private ProxyServer[] servers = new ProxyServer[0];

    static {
        Skript.registerExpression(ExprProxyServers.class, ProxyServer.class, ExpressionType.SIMPLE, "[(all [[of] the]|the)] [online] [(proxy|bungeecord|bungee)] servers");
    }

    @Override
    protected ProxyServer @NotNull [] get(@NotNull Event event) {
        return servers;
    }

    @Override
    public boolean isSingle() {
       return servers.length == 1;
    }

    @Override
    public @NotNull Class<? extends ProxyServer> getReturnType() {
        return ProxyServer.class;
    }

    @Override
    public @NotNull String toString(Event event, boolean b) {
        return "all online servers: " + Arrays.toString(servers);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult) {
        servers = ProxyServerInfoManager.getServers().toArray(new ProxyServer[0]);
        return true;
    }
}