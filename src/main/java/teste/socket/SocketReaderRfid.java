package teste.socket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

@RestController
public class SocketReaderRfid {

    @GetMapping("/read")
    public String read() throws IOException {
        Socket socket = new Socket("172.16.254.217", 8081);

        InputStream inputStream = socket.getInputStream();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

        // Envia um comando para a antena RFID
        printWriter.println("read");
        printWriter.flush();

        // Lê a resposta da antena RFID
        byte[] data = new byte[1024];
        int bytesRead = inputStream.read(data);

        // Converte a resposta para uma string
        String response = new String(data, 0, bytesRead);
        System.out.println("Resposta servidor: " + response);
        System.out.println("Resposta servidor sem conversão: " + data);

        // Fecha o socket
        socket.close();

        return response;
    }

}