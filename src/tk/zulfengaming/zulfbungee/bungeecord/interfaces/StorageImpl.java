package tk.zulfengaming.zulfbungee.bungeecord.interfaces;

import tk.zulfengaming.zulfbungee.bungeecord.socket.MainServer;
import tk.zulfengaming.zulfbungee.universal.util.skript.NetworkVariable;
import tk.zulfengaming.zulfbungee.universal.util.skript.Value;

import java.util.Optional;

public abstract class StorageImpl {

    private final MainServer mainServer;

    private final String host, port, username, password, database;

    public StorageImpl(MainServer mainServerIn) {
        this.mainServer = mainServerIn;

        this.host = mainServerIn.getPluginInstance().getConfig().getString("storage-host");
        this.port = String.valueOf(mainServerIn.getPluginInstance().getConfig().getInt("storage-port"));

        this.username = mainServerIn.getPluginInstance().getConfig().getString("storage-username");
        this.password = mainServerIn.getPluginInstance().getConfig().getString("storage-password");

        this.database = mainServerIn.getPluginInstance().getConfig().getString("storage-database");
    }
    
    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public abstract void setupDatabase();

    public abstract Optional<NetworkVariable> getVariable(String name);

    public abstract void setVariable(NetworkVariable variable);

    public abstract void addToVariable(String name, Value[] values);

    public abstract void deleteVariable(String name);

    public abstract void removeFromVariable(String name, Value[] values);

    public abstract void shutdown();

    public MainServer getMainServer() {
        return mainServer;
    }
}
