import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class CmdUI {
	public static void main (String[] args) throws IOException {
		
		// Validation
		String server = args[0];
		String port = args[1];
		String username = args[2];
		// TODO add validation methods to Utilities.java and invoke them here
		
		// set up a clientSocket to send and receive data
		DatagramSocket clientSocket = new DatagramSocket(); 
		
		// send the login request
		ClientRequest loginRequest = new ClientRequest(0, username.getBytes());
		byte[] data = Utilities.getByteArray(loginRequest); // serialization occurs
		clientSocket.send(
			new DatagramPacket(data, data.length, 
					InetAddress.getLocalHost(), Integer.parseInt(port) ) );
		
		// start processing the user's command
		Scanner console = new Scanner(System.in);
		while (console.hasNextLine()){
			String userInput = console.nextLine();
			if (userInput.startsWith("/")){
				if (userInput.startsWith("/exit")) {
					
				} else if (userInput.startsWith("/join")) {
					
				} else if (userInput.startsWith("/leave")) {
					
				} else if (userInput.startsWith("/list")) {
					
				} else if (userInput.startsWith("/who")) {
					
				} else if (userInput.startsWith("/switch")) {
					
				} else {
					System.out.println("Invalid command!");
				}
			} else { // say request
				
			}
		}
		System.out.println("you have quit");
	}
	
	

}
