package tk.zulfengaming.zulfbungee.spigot.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import tk.zulfengaming.zulfbungee.spigot.ZulfBungeeSpigot;
import tk.zulfengaming.zulfbungee.spigot.socket.ClientConnection;
import tk.zulfengaming.zulfbungee.universal.socket.Packet;
import tk.zulfengaming.zulfbungee.universal.socket.PacketTypes;
import tk.zulfengaming.zulfbungee.universal.util.skript.ProxyServer;
import tk.zulfengaming.zulfbungee.universal.util.skript.ServerMessage;

public class EffServerSendMessage extends Effect {

    private Expression<ProxyServer> servers;
    private Expression<String> message;
    private Expression<String> title;

    static {
        Skript.registerEffect(EffServerSendMessage.class, "message (proxy|bungeecord|bungee) server %-proxyservers% [the message] %string% (named|called|with title) %string%");
    }

    @Override
    protected void execute(Event event) {

        ClientConnection connection = ZulfBungeeSpigot.getPlugin().getConnection();

        if (connection.getServerName().isPresent()) {

            String name = connection.getServerName().get();

            ServerMessage messageOut = new ServerMessage(title.getSingle(event), message.getSingle(event), servers.getArray(event),
                    new ProxyServer(name, connection.getClientInfo()));

            connection.send_direct(new Packet(PacketTypes.SERVER_SEND_MESSAGE_EVENT,
                    false, false, messageOut));

        }

    }

    @Override
    public @NotNull String toString(Event event, boolean b) {
        return "effect server send message to " + servers.toString(event, b) + " with message " + message + " and title " + title;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        servers = (Expression<ProxyServer>) expressions[0];
        message = (Expression<String>) expressions[1];
        title = (Expression<String>) expressions[2];

        return true;
    }
}
