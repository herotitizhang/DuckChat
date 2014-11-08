import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
	
	private static DatagramSocket clientSocket = null;
	private static ArrayList <String> myChannels = new ArrayList<String> (); 
	private static String currentChannel = "Common";
	private static InetAddress serverAddress;
	private static int serverPort;
	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	public static StringBuilder buffer = new StringBuilder();
	
	public static void main (String[] args) {
		
		if (args.length !=3){
			System.out.println("need 3 args");
			System.exit(0);
		}
		
		try {
			serverAddress = InetAddress.getByName(args[0]); 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		serverPort = Integer.parseInt(args[1]);
		if (serverPort < 1024 || serverPort > 65535) {
			System.out.println("Invalid port number!");
			System.exit(1);
		}
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
		terminalRawMode();
	
		Console console = System.console();
		Reader reader = console.reader();
				
		while(true) {
			try {		
                if ( System.in.available() != 0 ) {
                    int c = reader.read();
                    
                    if ( c == 0x1B )
                    	break; //esc for test
                    
                   
                    
                char ch = ((char) c);
               
                if(c==0x7F&&buffer.length()==0)	{System.out.print("\b \b");System.out.print("\b \b");} //backspace when no input.
                else	buffer.append(ch);
                
                String cmd = buffer.toString().trim();         
            
                for(int i=0; i<buffer.length(); i++) 
                   System.out.print("\b \b");
                
               
             // press backspace              
               if(c == 0x7F && buffer.length()>1){ 
                	buffer.deleteCharAt(buffer.length()-1);
                	buffer.deleteCharAt(buffer.length()-1);
                	
                	System.out.print("\b \b");
                	System.out.print("\b \b");
                	System.out.print(buffer);    
                	
                }
               
            // press enter
                else if (c == 0x0D )
                {	
                	System.out.print("\b \b");
                	if(buffer.length()==1) buffer.deleteCharAt(0);
                	else{
                	System.out.println(buffer);
                	processUserInput(cmd);
                	buffer.delete(0, buffer.length());
                	}  	
                }
               
               //normal char        
                else
                	System.out.print(buffer);  
               
               
                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		terminalCookedMode();
	}

	
	public static void processUserInput(String userInput) {
		if (userInput.startsWith("/")){
			if (userInput.equals("/exit")) {
				for (int i = 0; i < myChannels.size(); i++)
					sendClientRequest(ClientRequestGenerator.generateLeaveRequest(myChannels.get(i)));
				printInRawMode("Bye.");
				terminalCookedMode();
				System.exit(0);
			} else if (userInput.startsWith("/join")) {
				
				if (userInput.split("\\s+").length != 2) {
					printInRawMode("Invalid command!");
				} else {
					String delims = " ";
					String[] tokens = userInput.split(delims);
					String channelName = tokens[1];
					if(myChannels.contains(channelName))
						printInRawMode("You have already joined that channel.");
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
					printInRawMode("Invalid command!");
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
						printInRawMode("You have not joined that channel yet!");		
				}
				
			} else if (userInput.equals("/list")) {
			
				sendClientRequest(ClientRequestGenerator.generateListRequest());
			
			} else if (userInput.startsWith("/who")) {

				if (userInput.split("\\s+").length != 2) {
					printInRawMode("Invalid command!");
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
					printInRawMode("You have not joined that channel!");		
				
			} else {
				printInRawMode("Invalid command!");
			}
		} else { // say request
			if (currentChannel.equals("")) {
				printInRawMode("You are not active in any channel now. Switch to a channel first!");
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
	
	// used for raw mode
	private static void terminalRawMode() {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	//cooked mode
    private static void terminalCookedMode() {
        try {
            String [] cmd = new String[] {"/bin/sh", "-c", "stty sane </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void printInRawMode(String print){
    	
    	System.out.println(print);
    	for(int i=0; i<print.length();i++)
    		System.out.print("\b \b");
    	
    }
}
