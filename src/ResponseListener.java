import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
				ServerResponse serverResponse = (ServerResponse)Utilities.getObject(receiveData); // Deserialization occurs
				receiveData = new byte[1024]; // TODO the purpose is to clear the placeholder. is it necessary?
				
				// make a new thread to process server's response so that 
				// the current thread can still receive incoming data
				ResponseHandler handleResponseTask = new ResponseHandler(serverResponse);
				threadExecutor.execute(handleResponseTask);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleResponse() {
		
	}

}
