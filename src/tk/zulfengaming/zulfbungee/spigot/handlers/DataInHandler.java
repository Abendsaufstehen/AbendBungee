package tk.zulfengaming.zulfbungee.spigot.handlers;

import org.bukkit.scheduler.BukkitRunnable;
import tk.zulfengaming.zulfbungee.spigot.ZulfBungeeSpigot;
import tk.zulfengaming.zulfbungee.spigot.socket.SocketConnection;
import tk.zulfengaming.zulfbungee.universal.socket.objects.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

public class DataInHandler extends BukkitRunnable {

    private final SocketConnection connection;

    private final LinkedBlockingQueue<Optional<Packet>> queueIn = new LinkedBlockingQueue<>();

    private final ZulfBungeeSpigot pluginInstance;

    private final ObjectInputStream inputStream;

    public DataInHandler(SocketConnection connectionIn, Socket socketIn) throws IOException {
        this.connection = connectionIn;
        this.pluginInstance = connection.getPluginInstance();
        this.inputStream = new ObjectInputStream(socketIn.getInputStream());
    }


    @Override
    public void run() {

        Thread.currentThread().setName(String.format("DataIn@%s", connection.getAddress()));

        do {
            try {

                if (connection.isConnected().get()) {

                    Object dataIn = inputStream.readObject();

                    if (dataIn instanceof Packet) {
                        queueIn.put(Optional.of((Packet) dataIn));
                    }

                }

            } catch (EOFException | SocketException | SocketTimeoutException e) {

                pluginInstance.warning("Proxy server appears to have disconnected!");

                connection.shutdown();

            } catch (IOException e) {

                pluginInstance.error("An unexpected error occurred!");
                pluginInstance.error("This likely isn't your fault!");
                pluginInstance.error("Please report this by making an issue on GitHub or contacting one of the devs so we can fix this issue!");
                pluginInstance.error("");

                e.printStackTrace();

                connection.shutdown();

            } catch (InterruptedException e) {
                break;
            } catch (ClassNotFoundException e) {
                pluginInstance.error("Packet received was not recognised!");
                e.printStackTrace();
            }

        } while (connection.isRunning().get());


    }

    public void disconnect() {
        queueIn.offer(Optional.empty());
    }

    public void shutdown() {
        disconnect();
    }

    public LinkedBlockingQueue<Optional<Packet>> getDataQueue() {
        return queueIn;
    }

}
