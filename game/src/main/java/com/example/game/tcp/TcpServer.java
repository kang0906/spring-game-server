package com.example.game.tcp;

import com.example.game.tcp.dto.ClientDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TcpServer extends Thread{

    private Map<Long, ClientDto> clients = new ConcurrentHashMap<Long, ClientDto>();


    @Override
    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(9996);

            while (true) {
                socket = serverSocket.accept();
                log.info("[new connect = {}:{}]", socket.getInetAddress(), socket.getPort());
                ServerReceiver thread = new ServerReceiver(clients, socket);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
