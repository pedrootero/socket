package teste.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketController {

    public static void main(String[] args) {
        SpringApplication.run(SocketReaderRfid.class, args);
    }

}
