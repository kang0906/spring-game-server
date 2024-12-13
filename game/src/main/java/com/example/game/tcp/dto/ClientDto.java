package com.example.game.tcp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

@AllArgsConstructor
@Getter
public class ClientDto {
    private Long id;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
}
