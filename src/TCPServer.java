import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            // Crear el socket del servidor
            serverSocket = new ServerSocket(9999);
            System.out.println("Servidor TCP iniciat. Esperant clients...");

            while (true) {
                // Esperar a que un client es connecti
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket);

                // Crear un nou gestor de clients per tractar la comunicació amb aquest client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Gestor de clients
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Crear els objectes per llegir i escriure dades amb el client
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                // Llegir l'objecte Llista del client
                Llista llista = (Llista) in.readObject();
                System.out.println("Llista rebuda del client: " + llista.getNom() + ", " + llista.getNumberList());

                // Ordenar i eliminar duplicats de la llista
                List<Integer> sortedUniqueNumbers = new ArrayList<>(new TreeSet<>(llista.getNumberList()));

                // Enviar la llista ordenada i sense repeticions al client
                llista.setNumberList(sortedUniqueNumbers);
                out.writeObject(llista);
                System.out.println("Llista enviada al client: " + llista.getNumberList());

                // Tancar els recursos
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
