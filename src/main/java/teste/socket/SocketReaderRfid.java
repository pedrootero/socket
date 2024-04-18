package teste.socket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.thingmagic.ReaderUtil.hexStringToByteArray;

@RestController
public class SocketReaderRfid {

    private Socket socket;
    private InputStream inputStream;
    private PrintWriter printWriter;
//    private String response;

    public static void converteHexUtf8(String hexString, String leitor) {
        try{
            //System.out.println("Valor passado " + hexString);
            if(hexString != null || hexString != "") {

                // Encontrar o índice do próximo "0x" após o primeiro "0x"
                int endIndex = hexString.indexOf("0x", 1 );
                String[]  tag = hexString.split("0x");
                //System.out.println("TAG: " + tag);

                for(String tag2 : tag){
                    if(!tag2.isEmpty() && !tag2.isBlank()) {
                       // System.out.println("TAG2: " + tag2);
                        byte[] bytes = hexStringToByteArray(tag2.replace("\r\n", ""));

                        // Decodificar os bytes usando UTF-8
                        String utf8String = new String(bytes, StandardCharsets.UTF_8);
                        String tratada = utf8String.substring(0, utf8String.length());
                        System.out.println("EPC UTF-8: " + tratada + "  Leitor: " + leitor + "  Thread: " + Thread.currentThread().getName());
                    }
                }
//                System.out.println("endIndex primeiro if: " + endIndex);
//                // Se não houver mais "0x", o valor continua até o final da string
//                if (endIndex == -1) {
//                    endIndex = hexString.length();
//                    System.out.println("Substring extraída if:  " + hexString);
//                } else {
//                    endIndex = endIndex;
//                    System.out.println("Substring extraída: " + hexString.substring(2, endIndex));

                    // Converter a sequência hexadecimal em um array de bytes
//                    byte[] bytes = hexStringToByteArray(hexString.substring(2, endIndex));
//
//                    // Decodificar os bytes usando UTF-8
//                    String utf8String = new String(bytes, StandardCharsets.UTF_8);
//                    String tratada = utf8String.substring(0, utf8String.length() - 1);
//                    System.out.println("String UTF-8: " + tratada);

                    //System.out.println("Resposta servidor sem conversão: " + data);


            }
    }catch (Exception e){
            System.out.println("Exception Conversão: " + e + " valor " + hexString);
        }
    }

    @GetMapping("/readtag")
    public void readTag() throws IOException, InterruptedException {
        List<String> urls = new ArrayList<>();
        urls.add("172.16.254.215");
        urls.add("172.16.254.216");
        urls.add("172.16.243.49");
        urls.add("172.16.254.218");

        String response = null;


        Thread thread = new Thread(new ConexaoRun("172.16.254.215"));
        thread.start();

        Thread thread2 = new Thread(new ConexaoRun("172.16.254.216"));
        thread2.start();

        Thread thread3 = new Thread(new ConexaoRun("172.16.243.49"));
        thread3.start();

        Thread thread4 = new Thread(new ConexaoRun("172.16.254.218"));
        thread4.start();

    while (true) {
        Thread.sleep(250);
        if (!thread.isAlive()) {
            thread = new Thread(new ConexaoRun("172.16.254.215"));
            thread.start();
            System.out.println("Reinstanciando a Thread " + Thread.currentThread().getName() + "IP:  172.16.254.215");
        }

        if (!thread2.isAlive()) {
            thread2 = new Thread(new ConexaoRun("172.16.254.216"));
            System.out.println("Reinstanciando a Thread " + Thread.currentThread().getName() + "IP: 172.16.254.216");
            thread2.start();
        }

        if (!thread3.isAlive()) {
            thread3 = new Thread(new ConexaoRun("172.16.243.49"));
            thread3.start();
            System.out.println("Reinstanciando a Thread " + Thread.currentThread().getName() + "IP: 172.16.243.49");
        }

        if (!thread4.isAlive()) {
            thread4 = new Thread(new ConexaoRun("172.16.254.218"));
            System.out.println("Reinstanciando a Thread " + Thread.currentThread().getName() + "IP: 172.16.254.218");
            thread4.start();

        }

    }


//        while (true) {
//            for (String uri : urls) {
//                try {
//                // Verifica se o socket está nulo ou não conectado
//                if (socket == null || !socket.isConnected()) {
//                    System.out.println("passou aqui 1");
//                    // Se estiver nulo ou não conectado, cria uma nova conexão
//                    socket = new Socket(uri, 8081);
//                    System.out.println("passou aqui 2");
//                    inputStream = socket.getInputStream();
//                    System.out.println("passou aqui 3");
//                    printWriter = new PrintWriter(socket.getOutputStream());
//                    System.out.println("Conexão bem sucedida. " + uri);
//
//                } else {
//                    System.out.println("Reutilizando conexão socket existente.");
//                }
//
//                // Envia um comando para a antena RFID
//                printWriter.println("read");
//                printWriter.flush();
//
//                // Lê a resposta da antena RFID
//                byte[] data = new byte[1024];
//                int bytesRead = inputStream.read(data);
//                System.out.println("bytesRead: " + bytesRead);
//
//                // Converte a resposta para uma string
//                response = new String(data, 0, bytesRead, StandardCharsets.UTF_8);
//                System.out.println("Resposta servidor: " + response + " Leitor: " + uri);
//
//                //String utf8String = hexToUtf8(response);
//                //System.out.println("EPC String: " + utf8String);
//                if (response.startsWith("0x")) {
//                    //String hexString = response;
//                    System.out.println("response:  " + response);
//
//                    converteHexUtf8(response);
//
//                }
//
//                //return response;
//                } catch (IOException e) {
//                System.err.println("Erro na conexão socket: " + e.getMessage());
//                socket.close();
//                socket = null; // Força a criação de uma nova conexão na próxima chamada
//                }
//
//            }

//        }
    }
}