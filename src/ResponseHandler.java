import java.util.ArrayList;
import java.util.Stack;


public class ResponseHandler implements Runnable{

	byte[] receiveData;
	
	public ResponseHandler(byte[] receiveData) {
		this.receiveData = receiveData;
	}
	
	@Override
	public void run() {
		if (receiveData[0] == 0) {
			handleSayResponse(receiveData);
		} else if (receiveData[0] == 1) {
			handleListResponse(receiveData);
		} else if (receiveData[0] == 2) {
			handleWhoResponse(receiveData);
		} else if (receiveData[0] == 3) {
			handleErrorResponse(receiveData);
		}
		
	}
	
	private void handleSayResponse(byte[] sayResponse) {
		
		// get text field
		int lastByteOfTextField; 
		for (lastByteOfTextField = 68; lastByteOfTextField < 132; lastByteOfTextField++ ){
			if (sayResponse[lastByteOfTextField] == 0) break;
		}
		if (lastByteOfTextField == 132) lastByteOfTextField --;
		lastByteOfTextField --;
			
		
		byte[] textField = new byte[lastByteOfTextField-68+1];
		for (int i = 68; i <= lastByteOfTextField; i++) {
			textField[i-68] = sayResponse[i]; 
		}
		
		// get user name 
		int lastByteOfUsername;
		for (lastByteOfUsername = 36; lastByteOfUsername < 68; lastByteOfUsername++ ){
			if (sayResponse[lastByteOfUsername] == 0) break;
		}
		if (lastByteOfUsername == 68) lastByteOfUsername --;
		lastByteOfUsername --;
		
		byte[] userName = new byte[lastByteOfUsername-36+1];
		for (int i = 36; i <= lastByteOfUsername; i++) {
			userName[i-36] = sayResponse[i]; 
		}
		
		// get channel name
		int lastByteOfChannelName;
		for (lastByteOfChannelName = 4; lastByteOfChannelName < 36; lastByteOfChannelName++ ){
			if (sayResponse[lastByteOfChannelName] == 0) break;
		}
		if (lastByteOfChannelName == 36) lastByteOfChannelName --;
		lastByteOfChannelName --;
		
		byte[] channelName = new byte[lastByteOfChannelName-4+1];
		for (int i = 4; i <= lastByteOfChannelName; i++) {
			channelName[i-4] = sayResponse[i]; 
		}
		
		// get the String
		for(int i=0;i<Client.buffer.length();i++)
			System.out.print("\b \b");
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(new String(channelName));
		sb.append("][");
		sb.append(new String(userName));
		sb.append("]: ");
		sb.append(new String(textField));
		System.out.println(sb.toString());
		
		for(int i=0;i<sb.length();i++)
			System.out.print("\b \b");
		
		System.out.print(Client.buffer);
		
	}
	
	private void handleListResponse(byte[] listResponse) {
		
		// get the number of channels
		int numOfChannels = listResponse[4];

		// add all channels
		ArrayList<String> channelNames = new ArrayList<String>();
		for (int i = 0; i < numOfChannels; i++) {
			int lastByteOfChannelName;
			for (lastByteOfChannelName = 8+i*32; lastByteOfChannelName <8+(i+1)*32; lastByteOfChannelName++ ){
				if (listResponse[lastByteOfChannelName] == 0) break;
			}
			if (lastByteOfChannelName == 8+(i+1)*32) lastByteOfChannelName --;
			lastByteOfChannelName --;
			
			byte[] channelName = new byte[lastByteOfChannelName-(8+i*32)+1];
			for (int j = 8+i*32; j <= lastByteOfChannelName; j++) {
				channelName[j-(8+i*32)] = listResponse[j]; 
			}
			channelNames.add(new String(channelName));
		}
		
		// print all channels
		for(int i=0;i<Client.buffer.length();i++)
			System.out.print("\b \b");
		
		StringBuilder sb = new StringBuilder();
		sb.append("Existing channels:");
		sb.append(System.getProperty("line.separator")); // get the nextLine character of the OS
		sb.append("  ");
		for (int i = 0; i < channelNames.size(); i++) {
			sb.append(new String(channelNames.get(i)).trim());
			sb.append("  ");
		}
		System.out.println(sb.toString());
		
		for(int i=0;i<sb.length();i++)
			System.out.print("\b \b");
		
		System.out.print(Client.buffer);
		
	}
	
	private void handleWhoResponse(byte[] whoResponse) {
		// get the number of channels
		int numOfChannels = whoResponse[4];

		// get channel name
		int lastByteOfChannelName;
		for (lastByteOfChannelName = 8; lastByteOfChannelName < 40; lastByteOfChannelName++ ){
			if (whoResponse[lastByteOfChannelName] == 0) break;
		}
		if (lastByteOfChannelName == 40) lastByteOfChannelName --;
		lastByteOfChannelName --;
		
		byte[] channelName = new byte[lastByteOfChannelName-8+1];
		for (int i = 8; i <= lastByteOfChannelName; i++) {
			channelName[i-8] = whoResponse[i]; 
		}
		
		// add all users
		ArrayList<String> userNames = new ArrayList<String>();
		for (int i = 0; i < numOfChannels; i++) {
			int lastByteOfUserName;
			for (lastByteOfUserName = 40+i*32; lastByteOfUserName <40+(i+1)*32; lastByteOfUserName++ ){
				if (whoResponse[lastByteOfUserName] == 0) break;
			}
			if (lastByteOfUserName == 40+(i+1)*32) lastByteOfUserName --;
			lastByteOfUserName --;
			
			byte[] userName = new byte[lastByteOfUserName-(40+i*32)+1];
			for (int j = 40+i*32; j <= lastByteOfUserName; j++) {
				userName[j-(40+i*32)] = whoResponse[j]; 
			}
			userNames.add(new String(userName));
		}
		
		// print all channels
		for(int i=0;i<Client.buffer.length();i++)
			System.out.print("\b \b");
		
		StringBuilder sb = new StringBuilder();
		sb.append("Users on channel ").append(new String(channelName)).append(":");
		sb.append(System.getProperty("line.separator")); // get the nextLine character of the OS
		sb.append("  ");
		for (int i = 0; i < userNames.size(); i++) {
			sb.append(new String(userNames.get(i)).trim());
			sb.append("  ");
		}
		System.out.println(sb.toString());
		
		for(int i=0;i<sb.length();i++)
			System.out.print("\b \b");
		
		System.out.print(Client.buffer);
		
	}
	
	private void handleErrorResponse(byte[] errorResponse) {

		int lastByteOfErrorMsg;
		for (lastByteOfErrorMsg = 4; lastByteOfErrorMsg < 68; lastByteOfErrorMsg++ ){
			if (errorResponse[lastByteOfErrorMsg] == 0) break;
		}
		if (lastByteOfErrorMsg == 68) lastByteOfErrorMsg --;
		lastByteOfErrorMsg --;
		
		byte[] errorMsg = new byte[lastByteOfErrorMsg-4+1];
		for (int i = 4; i <= lastByteOfErrorMsg; i++) {
			errorMsg[i-4] = errorResponse[i]; 
		}
		
		
		for(int i=0;i<Client.buffer.length();i++)
			System.out.print("\b \b");
		
		String err =new String(errorMsg);
		System.out.println(err);
		
		for(int i=0;i<err.length();i++)
			System.out.print("\b \b");
		
		System.out.print(Client.buffer);
	}

}
