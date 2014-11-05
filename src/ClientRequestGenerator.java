import java.math.BigInteger;


public class ClientRequestGenerator {
	
	public static byte[] generateLogInRequest(String uName) {
	    
		byte[] identifier = new byte[4];
	    identifier[0] = 0;
	    
	    byte[] userName = Utilities.fillInByteArray(uName, 32);
	    
	    if (!checkUserName(userName)) return null;

    	byte[] request = new byte[4+32];
    	for (int i = 0; i < 4; i++) {
    		request[i] = identifier[i]; 
    	}
    	for (int i = 4; i < 36; i++) {
    		request[i] = userName[i-4];
    	}
    	return request;
	    
	}
	
	public static byte[] generateJoinRequest(String cName) {
	    
		byte[] identifier = new byte[4];
	    identifier[0] = 2;
	    
	    byte[] channelName = Utilities.fillInByteArray(cName, 32);
	    
	    if (!checkUserName(channelName)) return null;

    	byte[] request = new byte[4+32];
    	for (int i = 0; i < 4; i++) {
    		request[i] = identifier[i]; 
    	}
    	for (int i = 4; i < 36; i++) {
    		request[i] = channelName[i-4];
    	}
    	return request;
	    
	}
	
	public static byte[] generateLeaveRequest(String cName) {
	    
		byte[] identifier = new byte[4];
	    identifier[0] = 3;
	    
	    byte[] channelName = Utilities.fillInByteArray(cName, 32);
	    
	    if (!checkUserName(channelName)) return null;

    	byte[] request = new byte[4+32];
    	for (int i = 0; i < 4; i++) {
    		request[i] = identifier[i]; 
    	}
    	for (int i = 4; i < 36; i++) {
    		request[i] = channelName[i-4];
    	}
    	return request;
	    
	}
	
	public static byte[] generateSayRequest(String channelName, String txtFld) {
		   
		byte[] identifier = new byte[4];
	    identifier[0] = 4;
	    
	    byte[] userName = Utilities.fillInByteArray(channelName, 32);
	    byte[] textField = Utilities.fillInByteArray(txtFld, 64);
	    
	    if (!checkUserName(userName) || !checkTextField(textField)) return null;
	        	
    	byte[] request = new byte[4+32+64];
    	
    	for (int i = 0; i < 4; i++) {
    		request[i] = identifier[i];
    	}
    	for (int i = 4; i < 36; i++) {
    		request[i] = userName[i-4];
    	}
    	for (int i = 36; i < 100; i++) {
    		request[i] = textField[i-36];
    	}
    	
    	return request;
	  
	}
	
	public static byte[] generateListRequest() {
	    
		byte[] identifier = new byte[4];
	    identifier[0] = 5;
	    
    	return identifier;
	    
	}
	
	public static byte[] generateWhoRequest(String cName) {
	    
		byte[] identifier = new byte[4];
	    identifier[0] = 6;
	    
	    byte[] channelName = Utilities.fillInByteArray(cName, 32);
	    
	    if (!checkUserName(channelName)) return null;

    	byte[] request = new byte[4+32];
    	for (int i = 0; i < 4; i++) {
    		request[i] = identifier[i]; 
    	}
    	for (int i = 4; i < 36; i++) {
    		request[i] = channelName[i-4];
    	}
    	return request;
	    
	}
	
	private static boolean checkUserName(byte[] userName) {
		if (userName.length == 0) {
	    	System.out.println("Empty username!");
	    	return false;
		}
		return true;
	}
	
	private static boolean checkTextField(byte[] textField) {
		if (textField.length == 0) {
	    	System.out.println("Empty username!");
	    	return false;
		}
		return true;
	}
}
