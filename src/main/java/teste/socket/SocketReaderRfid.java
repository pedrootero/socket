package teste.socket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.nio.charset.StandardCharsets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


@RestController
public class SocketReaderRfid {

    private Socket socket;
    private InputStream inputStream;
    private PrintWriter printWriter;

    @GetMapping("/readtag")
    public void readTag() throws IOException {
        String response;

        while (true) {
            try {
                // Verifica se o socket está nulo ou não conectado
                if (socket == null || !socket.isConnected()) {
                    // Se estiver nulo ou não conectado, cria uma nova conexão
                    socket = new Socket("172.16.254.215", 8081);
                    inputStream = socket.getInputStream();
                    printWriter = new PrintWriter(socket.getOutputStream());

                } else {
                    System.out.println("Reutilizando conexão socket existente.");
                }

                // Envia um comando para a antena RFID
                printWriter.println("read");
                printWriter.flush();

                // Lê a resposta da antena RFID
                byte[] data = new byte[1024];
                int bytesRead = inputStream.read(data);

                // Converte a resposta para uma string
                response = new String(data, 0, bytesRead, StandardCharsets.UTF_8);
                System.out.println("Resposta servidor: " + response);

                String hexString = response;
                String utf8String = hexToUtf8(hexString);
                System.out.println("EPC String: " + utf8String);

                System.out.println("Resposta servidor sem conversão: " + data);

                //return response;
            } catch (IOException e) {
                System.err.println("Erro na conexão socket: " + e.getMessage());
                socket = null; // Força a criação de uma nova conexão na próxima chamada
            }
        }

    }

    public static String hexToUtf8(String hexString) {
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            bytes[i] = (byte) j;
        }
        return new String(bytes, StandardCharsets.UTF_8);

    }

}