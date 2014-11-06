import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


public class ResponseListener implements Runnable{

	private ExecutorService threadExecutor;
	private DatagramSocket clientSocket;
	
	
	public ResponseListener(ExecutorService threadExecutor, DatagramSocket clientSocket) {
		this.threadExecutor = threadExecutor;
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		
		while (true) {
			try {
				// get all the resources needed to process server's response
				byte[] receiveData = new byte[1024]; // a placeholder for incoming data
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);

				// run a new thread to process server's response so that 
				// the current thread can still get information from the server
				threadExecutor.execute(new ResponseHandler(receiveData));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
