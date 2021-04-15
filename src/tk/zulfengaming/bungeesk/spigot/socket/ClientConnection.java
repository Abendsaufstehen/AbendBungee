package tk.zulfengaming.bungeesk.spigot.socket;

import org.bukkit.scheduler.BukkitTask;
import tk.zulfengaming.bungeesk.spigot.BungeeSkSpigot;
import tk.zulfengaming.bungeesk.spigot.handlers.DataInHandler;
import tk.zulfengaming.bungeesk.spigot.handlers.DataOutHandler;
import tk.zulfengaming.bungeesk.spigot.interfaces.ClientListenerManager;
import tk.zulfengaming.bungeesk.spigot.task.tasks.HeartbeatTask;
import tk.zulfengaming.bungeesk.universal.socket.Packet;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.*;

public class ClientConnection implements Runnable {

    private final BungeeSkSpigot pluginInstance;

    // threads

    private BukkitTask heartbeatThread;

    private Socket socket;

    // the latest packet from the queue coming in.
    private final BlockingQueue<Packet> skriptPacketQueue = new SynchronousQueue<>();

    private boolean running = true;

    private final PacketHandlerManager packetHandlerManager;

    private final ClientListenerManager clientListenerManager;

    // other tasks

    private DataOutHandler dataOutHandler;

    private DataInHandler dataInHandler;

    public ClientConnection(BungeeSkSpigot pluginInstanceIn) throws UnknownHostException {

        this.pluginInstance = pluginInstanceIn;

        this.packetHandlerManager = new PacketHandlerManager(this);

        this.clientListenerManager = new ClientListenerManager(pluginInstanceIn);

        init();

    }

    private void init() {

        HeartbeatTask heartbeatTask = new HeartbeatTask(this);

        this.heartbeatThread = pluginInstance.getTaskManager().newRepeatingTask(heartbeatTask, "Heartbeat", pluginInstance.getYamlConfig().getInt("heartbeat-ticks"));

        this.dataInHandler = new DataInHandler(clientListenerManager, this);
        this.dataOutHandler = new DataOutHandler(clientListenerManager, this);

        pluginInstance.getTaskManager().newTask(dataInHandler, "DataIn");
        pluginInstance.getTaskManager().newTask(dataOutHandler, "DataOut");


    }


    public void run() {

        do {
            try {

                if (clientListenerManager.isSocketConnected()) {
                    Packet packetIn = dataInHandler.getQueue().take();

                    if (packetIn.shouldHandle()) {
                        packetHandlerManager.handlePacket(packetIn, socket.getRemoteSocketAddress());

                    } else {
                        skriptPacketQueue.put(packetIn);
                    }

                } else {
                    Optional<Socket> optionalSocket = clientListenerManager.getSocket();

                    optionalSocket.ifPresent(value -> socket = value);
                }

            } catch (InterruptedException | ExecutionException | TimeoutException e) {

                e.printStackTrace();
            }

        } while (running);

    }

    public Optional<Packet> read() throws InterruptedException {
        return Optional.ofNullable(skriptPacketQueue.poll(5, TimeUnit.SECONDS));
    }

    public void send_direct(Packet packetIn) {

        try {
            dataOutHandler.getQueue().put(packetIn);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!packetHandlerManager.getHandler(packetIn).shouldHideInDebug()) {
            pluginInstance.log("Sent packet " + packetIn.getType().toString() + "...");
        }

    }

    public Optional<Packet> send(Packet packetIn) throws InterruptedException {

        send_direct(packetIn);

        if (clientListenerManager.isSocketConnected()) {
            return read();
        } else {
            return Optional.empty();
        }

    }

    public boolean isRunning() {
        return running;
    }

    public boolean isConnected() {
        return clientListenerManager.isSocketConnected();
    }

    public void shutdown() throws IOException {

        running = false;

        heartbeatThread.cancel();
        
        clientListenerManager.shutdown();

    }

    public BungeeSkSpigot getPluginInstance() {
        return pluginInstance;
    }

    public ClientListenerManager getClientManager() {
        return clientListenerManager;
    }
}
