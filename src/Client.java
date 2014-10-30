import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	
	private static DatagramSocket clientSocket = null;
	private static String currentChannel = "Common";
	
	private static InetAddress serverAddress;
	private static int port;
	
	public static void main (String[] args) {
		
		// TODO add validation methods to Utilities.java and invoke them here
		// to see if serverAddress and port are valid
		
		try {
			serverAddress = InetAddress.getLocalHost(); // TODO needs a real address (arg[0])
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} 
		port = Integer.parseInt(args[1]);
		String username = args[2];
		
		
		// set up a clientSocket to send and receive data
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		
		// send the login request
		byte[] adjustedUsername = Utilities.fillInByteArray(username, 32);
		ClientRequest loginRequest = new ClientRequest(0, adjustedUsername);
		
		sendClientRequest(loginRequest);
		
		
		// start processing the user's command
		Scanner console = new Scanner(System.in);
		while (console.hasNextLine()){
			String userInput = console.nextLine();
			processUserInput(userInput);
		}
	}
	
	public static void processUserInput(String userInput) {
		byte[] adjustedUsername, adjustedChannelName, adjustedText;
		ClientRequest request;
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
			adjustedChannelName = Utilities.fillInByteArray(currentChannel, 32);
			adjustedText = Utilities.fillInByteArray(userInput, 64);
			request = new ClientRequest(4, adjustedChannelName, adjustedText);
			sendClientRequest(request);
		}
	}
	
	// send the ClientRequest to the server
	private static void sendClientRequest(ClientRequest clientRequest) {
		byte[] dataToBeSent = Utilities.getByteArray(clientRequest); // serialization occurs
		try {
			clientSocket.send(new DatagramPacket(dataToBeSent, dataToBeSent.length, 
						serverAddress, port) );
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
