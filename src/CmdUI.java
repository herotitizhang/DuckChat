import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class CmdUI {
	public static void main (String[] args) throws IOException {
		System.out.println(args[0]+", "+args[1]+", "+args[2]);
		
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress serverAddress = InetAddress.getLocalHost();
		
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
			} else {
				String logInNotice = "I am in";
				DatagramPacket sendPacket = new DatagramPacket(logInNotice.getBytes(), 
						logInNotice.getBytes().length, 
						serverAddress, 
						7777);
				clientSocket.send(sendPacket);
			}
		}
		System.out.println("you have quit");
	}

}
