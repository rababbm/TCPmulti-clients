import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {
    public static void main(String[] args) {
        try {
            // Establecer la conexión con el servidor en el puerto 9999 (el mismo que el servidor)
            Socket socket = new Socket("localhost", 9999);

            // Crear los objetos para leer y escribir datos con el servidor
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Solicitar al usuario que ingrese el nombre de la lista
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el nombre de la lista: ");
            String nom = scanner.nextLine();

            // Solicitar al usuario que ingrese los números de la lista
            List<Integer> numbers = new ArrayList<>();
            System.out.println("Ingrese los números de la lista (ingrese 'fin' para terminar): ");
            while (true) {
                String input = scanner.nextLine();
                if (input.equals("fin")) {
                    break;
                }
                try {
                    int number = Integer.parseInt(input);
                    numbers.add(number);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido.");
                }
            }

            // Crear un objeto Llista con el nombre y los números ingresados por el usuario
            Llista llista = new Llista(nom, numbers);

            // Enviar el objeto Llista al servidor
            out.writeObject(llista);
            out.flush();
            System.out.println("Llista enviada al servidor: " + llista.getNumberList());

            // Recibir la respuesta del servidor
            Llista modifiedList = (Llista) in.readObject();
            System.out.println("Llista modificada recibida del servidor: " + modifiedList.getNumberList());

            // Cerrar los recursos
            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
