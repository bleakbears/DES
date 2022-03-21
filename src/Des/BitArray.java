package Des;    
public class BitArray { 
	//turn 8(n) byte array to 64(n*8) bit array
	//L shift byte array in custom scope
	//64(n*8) bit array back
    public static byte[] getBitArray(byte[] b) {  
        byte[] array = new byte[8*b.length];  
        
        for (int i = 0; i < b.length; i++) {  
        	for(int j = 7; j >= 0; j--) {
                array[i*8+j] = (byte)(b[i] & 1);  
                b[i] = (byte) (b[i] >> 1);          		
        	}

        }  
        return array;  
    }  
    
    public static void shift(byte[] b,int start, int end) {  
    	byte temp = b[start];
    	
        for (int i = start; i < end ; i++) {  
        	b[i] = b[i+1];
        }  
        b[end] = temp ;
        return ;  
    }    
    
    
    
  
    public static byte[] bitToByte(byte[] b) {  
    	byte[] result= new byte[b.length/8];
    	for(int i =0; i < result.length;i++) {		
    			result[i] = (byte)(b[i*8]*128+b[i*8+1]*64+b[i*8+2]*32+b[i*8+3]*16+
    					b[i*8+4]*8+b[i*8+5]*4+b[i*8+6]*2+b[i*8+7]);
    	}
    	
        return result  ;
    }  
    

    
   
}  