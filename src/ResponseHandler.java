import java.util.Stack;


public class ResponseHandler implements Runnable{

	private ServerResponse serverResponse;
	
	public ResponseHandler(ServerResponse serverResponse) {
		this.serverResponse = serverResponse;
	}
	
	@Override
	public void run() {
		handleServerResponse();
	}
	
	private void handleServerResponse() {
		System.out.println("got feedback");
		StringBuilder sb = new StringBuilder();
		if (serverResponse.getIdentifier() == 0) { // say response
			sb.append('[');
			sb.append(new String(serverResponse.getChannelName()).trim());
			sb.append("][");
			sb.append(new String(serverResponse.getUserName()).trim());
			sb.append("]: ");
			sb.append(new String(serverResponse.getText()).trim());
			System.out.println(sb.toString());
		} else if (serverResponse.getIdentifier() == 1) { // list response
			byte[][] channelNames = serverResponse.getChannelNames();
			sb.append("Existing channels:");
			sb.append(System.getProperty("line.separator")); // get the nextLine character of the OS
			sb.append("  ");
			for (int i = 0; i < channelNames.length; i++) {
				sb.append(new String(channelNames[i]).trim());
				sb.append(" ");
			}
			System.out.println(sb.toString());
		} else if (serverResponse.getIdentifier() == 2) { // who response
			byte[][] userNames = serverResponse.getUserNames();
			sb.append("Users on channel ");
			sb.append(new String(serverResponse.getChannelName()).trim());
			sb.append(":");
			sb.append(System.getProperty("line.separator")); // get the nextLine character of the OS
			sb.append("  ");
			for (int i = 0; i < userNames.length; i++) {
				sb.append(new String(userNames[i]).trim());
				sb.append(" ");
			}
			System.out.println(sb.toString());
		} else if (serverResponse.getIdentifier() == 3) { // error response
			
		}
	}

}
