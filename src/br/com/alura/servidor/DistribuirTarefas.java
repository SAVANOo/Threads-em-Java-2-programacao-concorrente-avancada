package br.com.alura.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {

    private Socket socket;
    private ServidorTarefas servidorTarefas;

    public DistribuirTarefas(Socket socket, ServidorTarefas servidorTarefas) {
        this.socket = socket;
        this.servidorTarefas = servidorTarefas;
    }

    @Override
    public void run() {
        try {
            System.out.println("Distribuindo tarefas para " + socket);
            Scanner entradaCliente = new Scanner(socket.getInputStream());
            PrintStream saidaCliente = new PrintStream(socket.getOutputStream());

            while (entradaCliente.hasNextLine()) {
                String comando = entradaCliente.nextLine();
                switch (comando) {
                    case "c1": {
                        saidaCliente.println("Comando c1 recebido");
                        break;
                    }
                    case "c2": {
                        saidaCliente.println("Comando c2 recebido");
                        break;
                    }
                    case "shutdown" : {
                        servidorTarefas.close();
                        break;
                    }
                    default:{
                        saidaCliente.println("Comando não reconhecido");
                    }
                }
            }
            saidaCliente.close();
            entradaCliente.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
