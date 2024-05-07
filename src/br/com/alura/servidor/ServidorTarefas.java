package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTarefas {

    private ServerSocket server;
    private ExecutorService threadPool;
    private boolean isRunning = true;

    public ServidorTarefas() throws IOException {
        System.out.println("---Iniciando Servidor---");
        this.server = new ServerSocket(12345);
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() throws IOException {
        while (isRunning) {
            try {
                Socket socket = server.accept();
                System.out.println("Aceito cliente na porta:" + socket.getPort());
                DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket, this);
                threadPool.execute(distribuirTarefas);
            } catch(SocketException e) {
                System.out.println("SocketException is running:" + isRunning);
            }

        }
    }

    public void close() throws IOException {
        isRunning = false;
        server.close();
        threadPool.shutdown();
    }

    public static void main(String[] args) throws Exception {
        ServidorTarefas servidorTarefas = new ServidorTarefas();
        servidorTarefas.start();
        servidorTarefas.close();
    }
}
