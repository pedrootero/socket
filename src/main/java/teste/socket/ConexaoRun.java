package teste.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static teste.socket.SocketReaderRfid.converteHexUtf8;

import static com.thingmagic.ReaderUtil.hexStringToByteArray;

public class ConexaoRun implements Runnable{
    private Socket socket;
    private InputStream inputStream;
    private PrintWriter printWriter;
    private String response;

   private String leitor;

    public ConexaoRun(String leitor) {
        this.leitor = leitor;
    }

    @Override
    public void run() {
        boolean opcao = true;
        while (opcao) {
            try {
               // System.out.println("Thread " + Thread.currentThread().getName() + " Leitor: " + leitor);
                // Verifica se o socket está nulo ou não conectado
                if (socket == null || !socket.isConnected()) {

                    // Se estiver nulo ou não conectado, cria uma nova conexão
                    socket = new Socket(leitor, 8081);

                    inputStream = socket.getInputStream();

                    printWriter = new PrintWriter(socket.getOutputStream());
                    System.out.println("Conexão bem sucedida. ");

                } //else {
//                    System.out.println("Reutilizando conexão socket existente.");
//                }

                // Envia um comando para a antena RFID
                printWriter.println("read");
                printWriter.flush();

                // Lê a resposta da antena RFID
                byte[] data = new byte[1024];
                int bytesRead = inputStream.read(data);
               // System.out.println("bytesRead: " + bytesRead);

                // Converte a resposta para uma string
                response = new String(data, 0, bytesRead, StandardCharsets.UTF_8);
                //System.out.println("Resposta servidor: " + response + " Leitor: " + leitor);


//                String hexString = "0x4358502E4255502D30393835";
//                String utf8String = hexToUtf8(hexString);
//                System.out.println("EPC String: " + utf8String);
            //    if (response.contains("0x")) {
                    String hexString = response;
                   // System.out.println("response:  " + response);

                    converteHexUtf8(hexString, leitor);


              //  }

                //return response;
            } catch (IOException e) {
                System.err.println("Erro na conexão socket: " + e.getMessage());
                try {
                    opcao = false;
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                socket = null; // Força a criação de uma nova conexão na próxima chamada
            }
        }

    }
    }

