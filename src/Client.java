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
		
		
		
		//try {
	//		serverAddress = args[2]; // TODO needs a real address (arg[0])
	//	} catch (UnknownHostException e) {
		//	e.printStackTrace();
		//} 
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
			if (userInput.equals("/exit")) {
				for (int i = 0; i < myChannels.size(); i++)
					sendClientRequest(ClientRequestGenerator.generateLeaveRequest(myChannels.get(i)));
				System.out.println("Bye.");
				System.exit(0);
			} else if (userInput.startsWith("/join")) {
				
				if (userInput.split("\\s+").length != 2) {
					System.out.println("Invalid command!");
				} else {
					String delims = " ";
					String[] tokens = userInput.split(delims);
					String channelName = tokens[1];
					if(myChannels.contains(channelName))
						System.out.println("You have already joined that channel.");
					else // if not in arraylist
					{
						//send request to server.
						sendClientRequest(ClientRequestGenerator.generateJoinRequest(channelName));
						
						//update active channel and arylist.
						currentChannel = channelName;
						myChannels.add(channelName);
					}
				}
						
			} else if (userInput.startsWith("/leave")) {
				
				if (userInput.split("\\s+").length != 2) {
					System.out.println("Invalid command!");
				} else {
					String delims = " ";
					String[] tokens = userInput.split(delims);
					String channelName = tokens[1];
					
					if(currentChannel.equals(channelName))
						currentChannel = ""; // need to set ignore in "say request"
					
					if(myChannels.contains(channelName)){
						myChannels.remove(channelName); //remove from arraylist
						sendClientRequest(ClientRequestGenerator.generateLeaveRequest(channelName));
					}				
					else
						System.out.println("You have not joined that channel yet!");		
				}
				
			} else if (userInput.equals("/list")) {
			
				sendClientRequest(ClientRequestGenerator.generateListRequest());
			
			} else if (userInput.startsWith("/who")) {

				if (userInput.split("\\s+").length != 2) {
					System.out.println("Invalid command!");
				} else {
					sendClientRequest(ClientRequestGenerator.generateWhoRequest(userInput.split(" ")[1]));
				}
				
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
			}
		} else { // say request
			if (currentChannel.equals("")) {
				System.out.println("You are not active in any channel now. Switch to a channel first!");
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
