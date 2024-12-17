package com.example.game.tcp;

import com.example.game.tcp.dto.ClientDto;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServerReceiver extends Thread {
    private Map<Long, ClientDto> clients;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ServerReceiver(Map<Long, ClientDto> clients, Socket socket) {
        this.socket = socket;
        this.clients = clients;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {}
    }
    @Override
    public void run() {
        Long id= null;
        try {
            id = Long.parseLong(in.readUTF());

            if (clients.get(id) == null){
                clients.put(id, new ClientDto(id, socket, in, out));
                log.info("user connect : {} , user count={}", id, clients.size());
            } else {
                log.warn("duplicate user : id={} ip={}:{}",id , socket.getInetAddress(), socket.getPort());
                socket.close();
                id = null;
                return ;
            }
            while (in != null) {
                byte[] b = new byte[1024];
                int length = in.read(b);
                sendToAll(b, length);
            }
        } catch (SocketException e) {
            log.warn("SocketException : {}", e.getMessage());
        } catch (IOException e) {
            log.warn("IOException : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            if(id != null){
                clients.remove(id);
                log.info("user disconnect : id={}, user count={}", id, clients.size());
            }
        }
    }

    void sendToAll(byte[] msg, int length) {
        log.warn("msg : {}", new String(msg, 0, length));
        Iterator it = clients.keySet().iterator();

        while (it.hasNext()) {
            try {
                DataOutputStream out = clients.get(it.next()).getOut();
                out.write(msg, 0, length);
            } catch (IOException e) {
                log.warn("IOException : {}", e.getMessage());
            }
        }
    }
}
