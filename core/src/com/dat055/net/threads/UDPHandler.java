package com.dat055.net.threads;

import com.dat055.net.Client;
import com.dat055.net.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPHandler extends Thread {

    private Client client;
    private Server server;

    public UDPHandler(Client client) {
        this.client = client;
    }


    public UDPHandler(Server server) {
        this.server = server;
    }

    public void run() {
        while(!interrupted()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) { break; }

            receive();
        }

    }

    /**
     * Creates new thread that awaits udp packets
     */
    private void receive() {
        try {
            byte[] data = new byte[1024];
            DatagramPacket current = new DatagramPacket(data, data.length);
            server.getDatagramSocket().receive(current);
            System.out.printf("<=== Received package from %s!\n", current.getAddress());
            server.handlePackets(data);
        } catch (IOException e) { System.out.println(e);}
    }


    /**
     * Sends packet to addr
     */
    public void send(DatagramSocket socket, byte[] data, InetAddress hostname, int port) {
        if(data != null) {
            DatagramPacket packet = new DatagramPacket(data, data.length, hostname, port);
            try {
                System.out.printf("==> Client sent UDPHandler packet to %s:%d \n", hostname.getHostAddress(), port);
                socket.send(packet);
            } catch (IOException e) { System.out.println(e); }
        }
    }

}
