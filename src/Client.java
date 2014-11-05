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
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
	
	private static DatagramSocket clientSocket = null;
	private static ArrayList <String> myChannels = new ArrayList<String> (); 
	private static String currentChannel = "Common";
	private static InetAddress serverAddress;
	private static int serverPort;
	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	public static void main (String[] args) {
		
		// TODO add validation methods to Utilities.java and invoke them here
		// to see if serverAddress and port are valid
		
		
		
		try {
			serverAddress = InetAddress.getByName(args[0]); 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		serverPort = Integer.parseInt(args[1]);
		String userName = args[2];
		
		myChannels.add(currentChannel);
		// set up a clientSocket to send and receive data
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		
		// start listening to server's response 
		ResponseListener responseListener = new ResponseListener(threadExecutor, clientSocket);
		threadExecutor.execute(responseListener);
		
		// send the login request
		sendClientRequest(ClientRequestGenerator.generateLogInRequest(userName));
		sendClientRequest(ClientRequestGenerator.generateJoinRequest("Common"));

		// start processing the user's command
		Scanner console = new Scanner(System.in);
		while (console.hasNextLine()){
			String userInput = console.nextLine();
			processUserInput(userInput);
		}
	}
	
	public static void processUserInput(String userInput) {
		if (userInput.startsWith("/")){
			if (userInput.startsWith("/exit")) {
				for (int i = 0; i < myChannels.size(); i++)
					sendClientRequest(ClientRequestGenerator.generateLeaveRequest(myChannels.get(i)));
				System.exit(0);
			} else if (userInput.startsWith("/join")) {
				
				String delims = " ";
				String[] tokens = userInput.split(delims);
				String channelName = tokens[1];
				
				if(myChannels.contains(channelName))
					System.out.println("The channel u wanna join is already subscribed");
				else // if not in arraylist
				{
					//send request to server.
					sendClientRequest(ClientRequestGenerator.generateJoinRequest(channelName));
					
					//update active channel and arylist.
					currentChannel = channelName;
					myChannels.add(channelName);
				}
						
			} else if (userInput.startsWith("/leave")) {
				
				String delims = " ";
				String[] tokens = userInput.split(delims);
				String channelName = tokens[1];
				
				if(currentChannel.equals(channelName))
					currentChannel = ""; // need to set ignore in "say request"
				
				if(myChannels.contains(channelName)){
					myChannels.remove(channelName);//remove from alist
					sendClientRequest(ClientRequestGenerator.generateLeaveRequest(channelName));
				}				
				else
					System.out.println("The channel u want to leave has not been joined!");		
					
			} else if (userInput.startsWith("/list")) {
				sendClientRequest(ClientRequestGenerator.generateListRequest());
			} else if (userInput.startsWith("/who")) {
				sendClientRequest(ClientRequestGenerator.generateWhoRequest(userInput.split(" ")[1]));
			} else if (userInput.startsWith("/switch")) {
				
				String delims = " ";
				String[] tokens = userInput.split(delims);
				String channelName = tokens[1];
				
				if(myChannels.contains(channelName))
					currentChannel = channelName ;
				else
					System.out.println("You have not joined that channel!");		
				
				
			} else {
				System.out.println("Invalid command!");
				// TODO what to do with an invalid command?
			}
		} else { // say request
			if (currentChannel.equals("")) {
				System.out.println("Error: You are not in any channel now!");
			} else {
				byte[] request = ClientRequestGenerator.generateSayRequest(currentChannel, userInput);
				sendClientRequest(request);
			}
		}
		
	}
	
	// send the ClientRequest to the server
	private static void sendClientRequest(byte[] dataToBeSent) {
		if (dataToBeSent == null) return;
		try {
			clientSocket.send(new DatagramPacket(dataToBeSent, dataToBeSent.length, 
					serverAddress, serverPort));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
