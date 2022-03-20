package Des;

import java.util.Arrays;


public class Des {

	byte[] message;	  			//input message into byte[] type
	byte[] cipher;    			//plaintext after encrypt
	
	byte[] message_block;		//64bit text block
	byte[] key;					//64 bit key 		 

	byte offset;    			//not work yet  , so cipher text will be multiples of 8
	
	static byte[][] roundKey;	
	int roundKeyStarter;		 //encrypt: 0  decrypt:15
	int roundKeyOrder;			//encrypt: 1  decrypt:-1


	
	
	

	Des(){
        //construct with default value
		//key = BitArray.getBitArray(new byte[] {(byte)0x13,(byte)0x34 ,(byte)0x57 ,(byte)0x79 ,(byte)0x9B, (byte)0xBC, (byte)0xDF, (byte)0xF1});
    	//roundKey = generateRoundKey(key);
    	roundKeyStarter=0;
    	roundKeyOrder=1;
		message =new byte[]{(byte)1, (byte) 162, (byte) 179, (byte)196,(byte) 213, (byte)230, (byte)7, (byte)248};

	}
	
    public static void main(String[] args) {  
    	//test board
        String input = new String("AM,HERE,ABE,STANEY\r\n"
        		+ "AT,ELRIGES\r\n"
        		+ "COME,ELSIE\r\n"
        		+ "NEVER!\r\n"
        		+ "ELSIE,PRERARE,TO,MEET,THE,GOD");   	

        Des d = new Des();   
        d.setMessage(input);
        d.setKey("DANCEMEN");

        System.out.println(d.encrypt());
        System.out.println(d.decrypt());
        
        
        //{(byte)132,(byte)63,(byte)222,(byte)230,(byte)159,(byte)100,(byte)27,(byte)7}; deafult answer
      

    
    }  
  
    
    

    public void setKey(String input) {//parse 8 length string to key ,and generate roundKey if input is valid  
    	
    	if(input.length() != 8){  //key length check ,no pariry check 
   		 System.out.println("please type  valid key(8 word)");
   		 return;
    	}	
							
 	
    	System.out.println("Key in  byte array");   
    	
    	key=BitArray.getBitArray(msgEncoder(input));
										
    	System.out.println("Key in  64 bit block :");
    	for(byte i : key) {
        	System.out.print(i);    		
    	}
    	
    	roundKey = generateRoundKey(key);
    	return;
    }  
   
    
    
    public void setMessage(String input) {
       	System.out.println("the text is :");       	
       	message=msgEncoder(input);
       	cipher=new byte[message.length];
      	      	
       	return;   	
    }
    
    public String encrypt() {
    	return encrypt(message,cipher);
    } 
    public String encrypt(byte[] message,byte[]cipher) {
    	
    	for(int i = 0 ; i < message.length/8 ; i++) {     //spilt message into 64 bit block
    		message_block = BitArray.getBitArray(Arrays.copyOfRange(message, i*8, i*8+8)); 

   		
  
            message_block = permutation(message_block,Table.IP);   //init permutation 
            
            
            
            byte[] message_L= new byte[32],message_R = new byte[32],reg = new byte[32];  //round
            
            int starter = roundKeyStarter;
            for(int j = 0 ; j < 16 ; j++) {
            	
            	
                msgSpilt(message_block,message_L,message_R);

                
                reg = feistel(message_R,roundKey[starter]);
                
                starter+=roundKeyOrder;

        
                reg= xor(reg,message_L);
                
                msgCombine(message_block,message_R, reg);          //swap L and R	
            	
            }
            msgSpilt(message_block,message_L,message_R);        // last round  no need to swap
            msgCombine(message_block,message_R, message_L); 
            
            
            
            message_block = permutation(message_block,Table.FP);      // final permutation		(reverse of IP)
    		
              
            byte[] c = BitArray.bitToByte(message_block);//64bit to 8 byte
            for(int j= 0;j<8;j++) {    //send (8 byte)value to cipher array(input length)
            	cipher[i*8+j] = c[j];
            }

 

    	}
    	
    	

    	
    	roundKeyStarter=0;
    	roundKeyOrder=1;  
	    System.out.println("result"); 
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
    
    private byte[] permutation(byte[] b,int[] table) {
	
    	byte[] result = new byte[table.length];
    	    	
        for(int i = 0 ; i<table.length ; i++){
            result[i] = b[table[i]-1];        	
        }    	
         
    	return result;
    }     
     

    
    private void msgSpilt(byte[] msg,byte[] L,byte[] R) {
    	int length = msg.length/2;
    	for(int i = 0 ; i < length ; i++) {
    		L[i] = msg[i];
    		R[i] = msg[i+length];
    	}  	
    	return;
    }    
    private void msgCombine(byte[] msg,byte[] L,byte[] R) {
    	int length = msg.length/2;    	
    	for(int i = 0 ; i < length ; i++) {
    		msg[i]=L[i];
    		msg[i+length]=R[i];
    	}  	    	
    	return;
    }      
    
    private byte[] xor(byte[] a,byte[] b) {

    	byte[] result = new byte[a.length];
    	for(int i = 0 ; i < a.length ; i++ ) {
    		if (a[i]==b[i]) 
    			result[i] = 0;
    		else
    			result[i] = 1;
    	}
    	return result;
    }    
    
    
    private byte[] Sbox(byte[] msg) {//48bit to 32bit
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
    
    private byte[][] generateRoundKey(byte[] key) {
    	byte[][]result=new byte[16][48];
        byte[] key_56 = permutation(key,Table.PC1);    	//64 bit to 58 bit
        
        for(int i = 0 ; i < 16 ; i++) {                      //round key *16
        	for(int j = 0 ; j < Table.shiftBit[i];j++) {     // shift L part and R part with specified value 
                BitArray.shift(key_56, 0, 27);
                BitArray.shift(key_56, 28, 55);        		
        	}

            
            result[i] = permutation(key_56,Table.PC2);  //get 48 bit round key       	
        }

    	for(int i = 0 ; i < 16 ; i++) {
           	System.out.printf("\n%d round\n",i+1);    		
        	for(int j = 0 ; j < 48 ; j++) {
               	System.out.printf("%d",result[i][j]);       		
        	}    
    	}
       	System.out.printf("\n");
    	return result;
    }   
    private byte[] feistel(byte[] msg,byte[] key) {
    	
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
    	
       	for(byte i : result) {
           	System.out.printf("%d ",i);    		
       	}
       	System.out.printf("\n\n");        	
    	return result;  	
    }	
    
    String msgDecoder(byte[] output) {    // get message string from output data
    	return new String(output, java.nio.charset.StandardCharsets.UTF_8);
    }    
}
