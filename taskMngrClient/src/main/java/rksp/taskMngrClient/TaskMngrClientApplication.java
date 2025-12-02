package rksp.taskMngrClient;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskMngrClientApplication {

	public static void main(String[] args) {
        SpringApplication.run(TaskMngrClientApplication.class, args);
    }
}
