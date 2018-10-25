package com.dtl.myRpc.myRpcserver;

import com.dtl.myRpc.myRpcserver.server.ServerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyRpcServerApplication implements CommandLineRunner {

	@Autowired
	private ServerListener serverListener;

	public static void main(String[] args) {
		SpringApplication.run(MyRpcServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		serverListener.start();
	}
}
