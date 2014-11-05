import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


public class ResponseListener implements Runnable{

	private ExecutorService threadExecutor;
	private DatagramSocket clientSocket;
	private static byte[] receiveData = new byte[1024]; // a placeholder for incoming data
	
	public ResponseListener(ExecutorService threadExecutor, DatagramSocket clientSocket) {
		this.threadExecutor = threadExecutor;
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		
		while (true) {
			try {
				// get all the resources needed to process server's response
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				System.out.println("GOT RESPONSE!");
				
				if (receiveData[0] == 0) {
					handleSayResponse(receiveData);
				} else if (receiveData[0] == 1) {
					handleListResponse(receiveData);
				} else if (receiveData[0] == 2) {
					handleWhoResponse(receiveData);
				} else if (receiveData[0] == 3) {
					handleErrorResponse(receiveData);
				}
				
				
//				ServerResponse serverResponse = (ServerResponse)Utilities.getObject(receiveData); // Deserialization occurs
//				receiveData = new byte[1024]; // TODO the purpose is to clear the placeholder. is it necessary?
//				
//				// make a new thread to process server's response so that 
//				// the current thread can still receive incoming data
//				ResponseHandler handleResponseTask = new ResponseHandler(serverResponse);
//				threadExecutor.execute(handleResponseTask);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(new String(channelName));
		sb.append("][");
		sb.append(new String(userName));
		sb.append("]: ");
		sb.append(new String(textField));
		System.out.println(sb.toString());
		
	}
	
	private void handleListResponse(byte[] listResponse) {
		System.out.println(listResponse[0]);
		
		int lastByte = 4;
		for (lastByte = 4; lastByte < 8; lastByte++) {
			if (listResponse[lastByte] == 0) break;
		}
		
		int numOfChannels = 0;
		for (int i = 0; lastByte > 4; i++, lastByte--) {
			numOfChannels += (int) (listResponse[lastByte] * Math.pow(10, i));
		}
		
		
			
		
		// error message
		byte[] errorMessage = new byte[64];
		for (int i = 4; i < 68; i++) {
			errorMessage[i-4] = listResponse[i];
		}
//		System.out.println(new String(errorMessage));
	}
	
	private void handleWhoResponse(byte[] whoResponse) {
//		System.out.println(errorResponse[0]);
//		// error message
//		byte[] errorMessage = new byte[64];
//		for (int i = 4; i < 68; i++) {
//			errorMessage[i-4] = errorResponse[i];
//		}
//		System.out.println(new String(errorMessage));
	}
	
	private void handleErrorResponse(byte[] errorResponse) {

		System.out.println("This is an error message");
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
		
		System.out.println(new String(errorMsg));
	}
	

	
//	ArrayList<Integer> nullBytes = new ArrayList<Integer>();
//	
//	for (int i = 0; i < sayResponse.length ; i ++) {
//		if (sayResponse[i] == 0 ) {
//			nullBytes.add(i);
//		}
//	}
//	
//	System.out.println(nullBytes.size());
//	for (int i = 0; i < nullBytes.size(); i++) {
//		System.out.print(nullBytes.get(i)+" ");
//	}

}
