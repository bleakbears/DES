
import java.util.Scanner;


import java.util.Arrays;
public class playground {


	public static void main(String args[]){
	    String str = "hello";	  
	    byte[] b = str.getBytes();
	    
        int IArray[] = { 1,2,3,4,5,6,7,8 }; 
        
       int[] CArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
              CArray = Arrays.copyOfRange(IArray, 0, 8);
       for (int i : CArray) {
   	
       System.out.println(i);
   }          
      /*  for (byte i : b) {
        	
            System.out.println(i);
        }     
        
        
        System.out.println(new String(b, java.nio.charset.StandardCharsets.UTF_8));
        
*/
	}	
	

}

