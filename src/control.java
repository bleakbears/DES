
import java.util.Scanner;
import java.util.Arrays;
public class control {


	public static void main(String args[]){
		int mode ;	
		String input;
		Scanner p = new Scanner (System.in);
		input = p.nextLine();

		byte[] msg = msgEncoder(input);
		
		
		
	//	Arrays.copyOfRange(message, i, i+7)
		 System.out.println(input);	
		 System.out.println(input.length());	 
		 System.out.println(msg.length);		 
		 
		 System.out.println(msgDecoder(msg));	
		 
	 
		 

	}
	
    static byte[] msgEncoder(String msg) {
    	int length = msg.length();
    	if(length%8 !=0)
    		length += 8-length%8;
    	
    	byte[] result;    
    	result = Arrays.copyOf(msg.getBytes(java.nio.charset.StandardCharsets.UTF_8).clone(),length);

    	return result;  	
    }	
    
    static String msgDecoder(byte[] output) {    // get message string from output data
    	return new String(output, java.nio.charset.StandardCharsets.UTF_8);
    }        
}