package teste.socket;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.thingmagic.ReaderUtil.hexStringToByteArray;

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

//                String hexString = "0x4358502E4255502D30393835";
//                String utf8String = hexToUtf8(hexString);
//                System.out.println("EPC String: " + utf8String);
                if (response.startsWith("0x")) {
                    String hexString = response;

                    converteHexUtf8(hexString);

                }




                //return response;
            } catch (IOException e) {
                System.err.println("Erro na conexão socket: " + e.getMessage());
                socket = null; // Força a criação de uma nova conexão na próxima chamada
            }
        }

    }

    private static void converteHexUtf8(String hexString) {
        // Encontrar o índice do próximo "0x" após o primeiro "0x"
        int endIndex = hexString.indexOf("0x", 2);
        // Se não houver mais "0x", o valor continua até o final da string
        if (endIndex == -1) {
            endIndex = hexString.length();
        }else{
            endIndex = endIndex;
            System.out.println("Substring extraída: " + hexString.substring(2,endIndex));

            // Converter a sequência hexadecimal em um array de bytes
            byte[] bytes = hexStringToByteArray(hexString.substring(2,endIndex));

            // Decodificar os bytes usando UTF-8
            String utf8String = new String(bytes, StandardCharsets.UTF_8);
            String tratada = utf8String.substring(0, utf8String.length()-1);
            System.out.println("String UTF-8: " + tratada);


            //System.out.println("Resposta servidor sem conversão: " + data);

        }
    }

}