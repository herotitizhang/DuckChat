import java.io.IOException;
import java.util.Scanner;


public class CmdUI {
	public static void main (String[] args) throws IOException {
		System.out.println(args[0]+", "+args[1]+", "+args[2]);
		
		
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
				
			}
		}
		System.out.println("you have quit");
	}

}
