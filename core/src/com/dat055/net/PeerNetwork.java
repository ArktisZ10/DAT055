package com.dat055.net;

import com.dat055.model.entity.Player;
import com.dat055.net.message.JoinMessage;
import com.dat055.net.message.Message;
import com.dat055.net.message.PlayerMessage;
import com.dat055.net.message.Protocol;
import com.dat055.net.threads.Server;
import com.dat055.net.threads.Client;

import java.io.*;
import java.net.*;

public class PeerNetwork extends Thread {
    private Client client;
    private Server server;
    private boolean waitForPeer = true;

    /**
     * Ready-to-join-another-peer-constructor
     * @param port
     * @param destAddr
     */
    public PeerNetwork(Client client, Server server) {
       this.server = server;
       this.client = client;
        start();
    }

    /**
     * Awaits-another-peer-to-join-constructor
     * @param port
     */
    public PeerNetwork(Server server) {
        this.server = server;
        start();
    }

    @Override
    public void run() {
        while(!interrupted()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
            receiveMessage();
        }
    }

    /**
     * Closes socket, client and server then tries to stop all threads within network
     */
    private void close() {
        socket.close(); //TODO: Close socket for server and client
        server.interrupt();
        client.interrupt();
        this.interrupt();
    }

    public void sendJoinRequest(String name) { sendMessage(new JoinMessage(name)); }
    public void sendPlayerUpdate(Player player) {
        sendMessage(new PlayerMessage(player));
    }

    /**
     * Serializes message and tells Client to start send message
     * @param msg
     */
    private void sendMessage(Message msg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut;
        try {
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(msg);
        } catch (IOException ignored) {}

        client.dataToBeSent(out.toByteArray());
        if(!client.isAlive())
            client.start();
    }


    /**
     * Deserializes message and translates op codes to determine what to do
     */
    private void receiveMessage() {
        byte[] data = receiver.getData();
        if(data == null) { return; }
        ObjectInputStream objIn;
        Message msg;
        try {
            objIn =  new ObjectInputStream(new ByteArrayInputStream(data));
            msg = (Message) objIn.readObject();
            objIn.close();

            // Translate messages
            if(msg != null && waitForPeer) {
                switch (msg.getOp()) {
                    case Protocol.OP_JOIN:
                        System.out.println((JoinMessage)msg);
                        if(setClient(server.getCurrent().getAddress()))  // Sets send address to other peer
                            waitForPeer = false;
                        sendJoinRequest("kjelle"); //TODO: Fix this too
                        break;

                    case Protocol.OP_PLAYER: System.out.println((PlayerMessage)msg);break;
                    case Protocol.OP_HOOK: break;
                    case Protocol.OP_LEAVE: close(); break;
                }
            }
        } catch (IOException ignored) {
        } catch (ClassNotFoundException e) {e.printStackTrace();}


    }

    /**
     * Adds a peer to this network
     * @param addr
     * @return true if it worked, false if it did not work
     */
    private boolean setClient (InetAddress addr) {
        return (client = PeerNetworkFactory.getClient(addr)) != null;
    }

    /**
     * Call this to see if another player has joined
     * @return
     */
    public boolean getStatus() {
        return waitForPeer;
    }
}