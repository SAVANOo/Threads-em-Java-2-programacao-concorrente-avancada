package br.com.alura.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefas {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);
        System.out.println("---Conexão Estabelecida---");

        Thread threadEnviaComando = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintStream saida = new PrintStream(socket.getOutputStream());
                    Scanner teclado = new Scanner(System.in);

                    while (teclado.hasNextLine()) {
                        String linha = teclado.nextLine();
                        if (linha.trim().equals("")) {
                            break;
                        }
                        saida.println(linha);
                    }
                    saida.close();
                    teclado.close();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        });

        Thread threadRecebeDados = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Recebendo do servidor");
                    Scanner respostaDoServidor = new Scanner(socket.getInputStream());

                    while (respostaDoServidor.hasNextLine()) {
                        String linha = respostaDoServidor.nextLine();
                        System.out.println(linha);
                    }
                    respostaDoServidor.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        threadEnviaComando.start();
        threadRecebeDados.start();

        threadRecebeDados.join();
        System.out.println("Fechando socket do cliente");
        socket.close();
    }
}
