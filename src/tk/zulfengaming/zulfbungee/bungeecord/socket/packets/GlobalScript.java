package tk.zulfengaming.zulfbungee.bungeecord.socket.packets;

import tk.zulfengaming.zulfbungee.bungeecord.interfaces.PacketHandler;
import tk.zulfengaming.zulfbungee.bungeecord.socket.BaseServerConnection;
import tk.zulfengaming.zulfbungee.bungeecord.socket.MainServer;
import tk.zulfengaming.zulfbungee.universal.socket.Packet;
import tk.zulfengaming.zulfbungee.universal.socket.PacketTypes;
import tk.zulfengaming.zulfbungee.universal.socket.ScriptAction;

public class GlobalScript extends PacketHandler {

    public GlobalScript(MainServer mainServerIn) {
        super(mainServerIn, PacketTypes.GLOBAL_SCRIPT);

    }

    // used to retrieve all scripts on the proxy, client will never ask for scripts on its own apart from this, server sends it
    // when needed to the client
    @Override
    public Packet handlePacket(Packet packetIn, BaseServerConnection connection) {

        if (getMainServer().getPluginInstance().getConfig().getBoolean("global-scripts")) {

            for (String scriptName : getMainServer().getPluginInstance().getConfig().getScripts()) {

               connection.sendScript(getMainServer().getPluginInstance().getConfig().getScriptPath(scriptName), ScriptAction.NEW, null);

            }

        }

        return null;

    }

}
