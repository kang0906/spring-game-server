package com.example.game;

import com.example.game.tcp.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class GameApplication {
	public static void main(String[] args) {
		new TcpServer().start();
		SpringApplication.run(GameApplication.class, args);
	}

}
