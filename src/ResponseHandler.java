
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
		if (serverResponse.getIdentifier() == 0) { // say response
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			sb.append(new String(serverResponse.getChannelName()).trim());
			sb.append("][");
			sb.append(new String(serverResponse.getUserName()).trim());
			sb.append("]: ");
			sb.append(new String(serverResponse.getText()).trim());
			System.out.println(sb.toString());
		} else if (serverResponse.getIdentifier() == 1) { // list response
			
		} else if (serverResponse.getIdentifier() == 2) { // who response
			
		} else if (serverResponse.getIdentifier() == 3) { // error response
			
		}
	}

}
