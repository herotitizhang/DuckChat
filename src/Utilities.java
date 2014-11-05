import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utilities class contains methods needed by different classes.
 * @author Xiaowei Xu, Hanxiao Zhang
 *
 */

public class Utilities {
	
	/** 
	 * convert an object to a byte array
	 * @param obj
	 * @return
	 */
	public static byte[] getByteArray(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/** 
	 * convert a byte array to an object
	 * @param byteArray
	 * @return
	 */
	public static Object getObject(byte[] byteArray) {
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 
	/**
	 * We want the fields in a ClientRequest to have the exact sizes specified in 
	 * the DuckChat protocol. For example, a user name has to be 32 bytes, not more
	 * or less.
	 * 
	 * @param str: the input String that needs to be filled in the byte array
	 * @param size: the size of the byte array that will be returned
	 * @return a byte array with the size specified 
	 */
	public static byte[] fillInByteArray(String str, int size) {
		byte[] wellSized = new byte[size];
		byte[] content = str.getBytes();
		int iterationNum = (size > content.length) ? content.length : size;
		for (int i = 0; i < iterationNum; i++) {
			wellSized[i] = content[i];
		}
		return wellSized;
		
	}
	
// convert byte array to Int
	public static int convertByteArrayToInt (byte[] byteArray) 
	{
	    return byteArray[3] & 0xFF |(byteArray[2] & 0xFF) << 8 |
	            (byteArray[1] & 0xFF) << 16 | (byteArray[0] & 0xFF) << 24;
	}

// convert int to byte array
	public static byte[] convertIntToByteArray(int num)
	{
	    return new byte[] { (byte) ((num >> 24) & 0xFF), (byte) ((num >> 16) & 0xFF),   
	    		(byte) ((num >> 8) & 0xFF),(byte) (num & 0xFF) };
	}
	
}
