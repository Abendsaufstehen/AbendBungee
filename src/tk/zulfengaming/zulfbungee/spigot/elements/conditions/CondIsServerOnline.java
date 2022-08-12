package tk.zulfengaming.zulfbungee.spigot.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import tk.zulfengaming.zulfbungee.spigot.managers.ProxyServerInfoManager;
import tk.zulfengaming.zulfbungee.universal.util.skript.ProxyServer;

import java.util.Objects;

@Name("Proxy Server Online")
@Description("Checks if a proxy server is online on the network.")
public class CondIsServerOnline extends Condition {

    private Expression<ProxyServer> server;

    static {
        Skript.registerCondition(CondIsServerOnline.class, "%-proxyserver% (1¦is|2¦is(n't| not)) online");
    }

    @Override
    public boolean check(@NotNull Event event) {

        ProxyServer proxyServer = server.getSingle(event);

        if (proxyServer != null) {
            boolean contains = ProxyServerInfoManager.contains(proxyServer.getName());
            return contains == isNegated();
        }

        return isNegated();

    }

    @Override
    public @NotNull String toString(Event event, boolean b) {
        return "condition proxy server " + server.toString(event, b) + " online";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult) {
        server = (Expression<ProxyServer>) expressions[0];
        setNegated(parseResult.mark == 1);
        return true;
    }
}
