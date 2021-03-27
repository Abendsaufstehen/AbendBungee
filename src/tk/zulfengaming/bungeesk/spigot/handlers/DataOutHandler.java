package tk.zulfengaming.bungeesk.spigot.handlers;

import tk.zulfengaming.bungeesk.spigot.interfaces.ClientListener;
import tk.zulfengaming.bungeesk.spigot.interfaces.ClientManager;
import tk.zulfengaming.bungeesk.spigot.socket.ClientConnection;
import tk.zulfengaming.bungeesk.universal.socket.Packet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class DataOutHandler extends ClientListener implements Runnable {

    private final ClientConnection connection;

    private final ClientManager clientManager;

    private final BlockingQueue<Packet> queueOut;

    private ObjectOutputStream outputStream;

    public DataOutHandler(ClientManager clientManagerIn, ClientConnection connectionIn) {
        super(clientManagerIn);

        this.connection = connectionIn;
        this.clientManager = clientManagerIn;

        this.queueOut = connectionIn.getQueueOut();

    }


    @Override
    public void run() {
        do {
            try {

                if (clientManager.isSocketConnected()) {

                    Packet packetOut = queueOut.take();

                    outputStream.writeObject(packetOut);
                    outputStream.flush();

                } else {

                    Optional<Socket> optionalSocket = clientManager.getSocket();

                    if (optionalSocket.isPresent()) {
                        Socket socket = optionalSocket.get();

                        outputStream = new ObjectOutputStream(socket.getOutputStream());
                    }

                }

            } catch (IOException | ExecutionException | TimeoutException | InterruptedException e) {
                clientManager.getPluginInstance().error("There was an error running the server! Disconnecting");

                clientManager.disconnect();
                e.printStackTrace();
            }

        } while (connection.isRunning());
    }

    @Override
    public void onDisconnect() {

        try {
            outputStream.close();

        } catch (IOException e) {

            clientManager.getPluginInstance().error("Error closing input stream:");

            e.printStackTrace();
        }

    }

    @Override
    public void onShutdown() {

    }

}
