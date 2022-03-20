package Des;

import java.util.Arrays;

//import java.util.Arrays;

public class Des {


	
	
	byte[] message;	  //String into byte[] type
	
	byte[] message_block;        //64bit text block
	byte[] key;//64 bit key ,default key		 		 
	//byte[] ciphertext;       //64bit block
	byte offset;

	byte[] cipher;

	
	
	
	//key new byte[] {(byte)0x13,(byte)0x34 ,(byte)0x57 ,(byte)0x79 ,(byte)0x9B, (byte)0xBC, (byte)0xDF, (byte)0xF1}
	//new byte[]{(byte)1, (byte) 162, (byte) 179, (byte)196,(byte) 213, (byte)230, (byte)7, (byte)248};
	
	static byte[][] roundKey;

	int roundKeyStarter; //encrypt: 0  decrypt:15
	int roundKeyOrder;	//encrypt: 1  decrypt:-1
	Des(){

		key = BitArray.getBooleanArray(new byte[] {(byte)0x13,(byte)0x34 ,(byte)0x57 ,(byte)0x79 ,(byte)0x9B, (byte)0xBC, (byte)0xDF, (byte)0xF1});
    	roundKey = generateRoundKey(key);
    	roundKeyStarter=0;
    	roundKeyOrder=1;
		message =new byte[]{(byte)1, (byte) 162, (byte) 179, (byte)196,(byte) 213, (byte)230, (byte)7, (byte)248};

	}
	
    public static void main(String[] args) {  

        Des d = new Des();   


        d.setKey("QSQSQSQS");        
        d.setMessage("hellooolKKKKKKKsaK");
        d.encrypt();
        System.out.println(d.decrypt());
        
        //byte[] cipher = {(byte)132,(byte)63,(byte)222,(byte)230,(byte)159,(byte)100,(byte)27,(byte)7}; deafult answer
      

    	         
        

        
    
    }  
  
    
    

    public void setKey(String input) {
    	
    	if(input.length() != 8){
   		 System.out.println("please type  valid key(8 word)");	
    	}	
    	key=BitArray.getBooleanArray(msgEncoder(input));
    	System.out.println("the key is :");
    	for(byte i : key) {
        	System.out.print(i);    		
    	}
    	System.out.println(); 
    	roundKey = generateRoundKey(key);
    	return;
    }  
   
    
    
    public void setMessage(String input) {
    	
       	message=msgEncoder(input);
       	cipher=new byte[message.length];
       	System.out.println("the text is :");         	
       	for(byte i : key) {
           	System.out.print(i);    		
       	}
       	System.out.println("\n");      	
       	return;   	
    }
    public String encrypt() {
    	return encrypt(message,cipher);
    } 
    public String encrypt(byte[] message,byte[]cipher) {
    	
    	for(int i = 0 ; i < message.length/8 ; i++) {
            for(byte j:Arrays.copyOfRange(message, i*8, i*8+8)) {
             //   System.out.print(j);        	
            }  
            System.out.println(); 
    		message_block = BitArray.getBooleanArray(Arrays.copyOfRange(message, i*8, i*8+8)); 

   		
           /* for(byte j:key) {
                System.out.print(j);        	
            }   */
            
            System.out.println();   
            message_block = permutation(message_block,Table.IP); 
            //round  
            byte[] message_L= new byte[32],message_R = new byte[32],reg = new byte[32];
            
            int starter = roundKeyStarter;
            for(int j = 0 ; j < 16 ; j++) {
            	
            	
                msgSpilt(message_block,message_L,message_R);

                
                reg = feistel(message_R,roundKey[starter]);
                
                starter+=roundKeyOrder;

        
                reg= xor(reg,message_L);
                
                msgCombine(message_block,message_R, reg);          	
            	
            }
            msgSpilt(message_block,message_L,message_R);     
            msgCombine(message_block,message_R, message_L); 
            
            
            
            message_block = permutation(message_block,Table.FP);      		
    		
           /* for(byte j:message_block) {
                System.out.print(j);        	
            }     		
            System.out.println();                  */
            byte[] c = BitArray.byteToBit(message_block);//64bit to 8 byte
            for(int j= 0;j<8;j++) {
            	cipher[i*8+j] = c[j];
            }
           /* for(byte j:c) {
                System.out.println(j & 0xFF);        	
            } */  
            System.out.println();  

    	}
    	
    	

    	
    	roundKeyStarter=0;
    	roundKeyOrder=1;  
	    System.out.println("cipher"); 
	    for(byte j:cipher) {
	        System.out.print(j & 0xFF);  
	        System.out.print(" "); 	        
	    }   
	    System.out.println(); 
    	return msgDecoder(cipher);
    }
    public String decrypt() {
    	roundKeyStarter=15;
    	roundKeyOrder=-1;
    	return encrypt(cipher,message);    	
    }    
    
    static byte[] permutation(byte[] b,int[] table) {
	
    	byte[] result = new byte[table.length];
    	    	
        for(int i = 0 ; i<table.length ; i++){
            result[i] = b[table[i]-1];        	
        }    	
         
    	return result;
    }     
     

    
    static void msgSpilt(byte[] msg,byte[] L,byte[] R) {
    	int length = msg.length/2;
    	for(int i = 0 ; i < length ; i++) {
    		L[i] = msg[i];
    		R[i] = msg[i+length];
    	}  	
    	return;
    }    
    static void msgCombine(byte[] msg,byte[] L,byte[] R) {
    	int length = msg.length/2;    	
    	for(int i = 0 ; i < length ; i++) {
    		msg[i]=L[i];
    		msg[i+length]=R[i];
    	}  	    	
    	return;
    }      
    
    static byte[] xor(byte[] a,byte[] b) {

    	byte[] result = new byte[a.length];
    	for(int i = 0 ; i < a.length ; i++ ) {
    		if (a[i]==b[i]) 
    			result[i] = 0;
    		else
    			result[i] = 1;
    	}
    	return result;
    }    
    
    
    static byte[] Sbox(byte[] msg) {//48bit to 32bit
    	byte[] result = new byte[32];
    	byte xoooox, oxxo;   //x0000x 0xx0
    	for(int i = 0 ; i < 8 ; i++) {
    		
    		oxxo = (byte) (msg[i*6]*2+msg[i*6+5]);
    		xoooox =(byte) (msg[i*6+1]*8+msg[i*6+2]*4+msg[i*6+3]*2+msg[i*6+4]);
    		
			byte temp = Table.Sbox[i][oxxo][xoooox];  

    		for(int j = 3 ; j >=0 ; j--) {
                result[i*4+j] = (byte)(temp & 1);  
                temp = (byte) (temp >> 1);  
    		} 
    	}
    	
	
    	
    	return result; 
    }
    
    static byte[][] generateRoundKey(byte[] key) {
    	byte[][]result=new byte[16][48];
        byte[] key_56 = permutation(key,Table.PC1);    	
        
        for(int i = 0 ; i < 16 ; i++) {
        	for(int j = 0 ; j < Table.shiftBit[i];j++) {
                BitArray.shift(key_56, 0, 27);
                BitArray.shift(key_56, 28, 55);        		
        	}

            
            result[i] = permutation(key_56,Table.PC2);  //key1         	
        }

    	
    	return result;
    }   
    static byte[] feistel(byte[] msg,byte[] key) {
    	
    	byte[] exp = permutation(msg,Table.E); //32 to 48 bit   	
    	exp = xor(key,exp);            
        exp = Sbox(exp); 
        exp= permutation(exp,Table.P);
    	return exp;
    }
    
    
    
    
    
    
    
    
    byte[] msgEncoder(String input) { //message string to input data
    	int length = input.length();
    	if(length%8 !=0)
    		length += 8-length%8;
    	
    	byte[] result;    
    	result = Arrays.copyOf(input.getBytes().clone(),length);

    	return result;  	
    }	
    
    String msgDecoder(byte[] output) {    // get message string from output data
    	return new String(output, java.nio.charset.StandardCharsets.UTF_8);
    }    
}
