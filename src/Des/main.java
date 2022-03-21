package Des;

import java.util.Scanner;


import java.util.Arrays;
public class main {


	public static void main(String args[]){
		Scanner s = new Scanner(System.in);
		String message="",key,cipher;
		
		Des d = new Des();
		
		int lines ;
		System.out.println("Please enter the number of lines you want to enter");
		

		lines=s.nextInt();
		System.out.println("enter message");
		/*
		AM,HERE,ABE,STANEY
		AT,ELRIGES
		COME,ELSIE
		NEVER!
		ELSIE,PRERARE,TO,MEET,THE,GOD*/
		 			
		for(int i=0;i<=lines;i++)
			message += s.nextLine()+"\n";

		
		do {
			System.out.println("enter key\n");
			//DANCEMEN
			key = s.next();			
		}while(d.setKey(key)==false);
		
	
	
		
		d.setMessage(message);
		//"AM,HERE,ABE,STANEY\nAT,ELRIGES\nCOME,ELSIE\nNEVER!\nELSIE,PRERARE,TO,MEET,THE,GOD"
		d.setKey(key);
		//"DANCEMAN"
		
        cipher=d.encrypt();
        
        
        System.out.println("cipher(base 64 encoded) :\n"+cipher);
		System.out.println("\n\nDecryption :　");        
		System.out.println("enter cipher\n");
		
		//String message2 = s.nextLine();
		message = s.nextLine();//absorb \n line	
		message = s.nextLine();	
		//K7CH+pKRzV6QwbB2zpl7EMK1OkvvtHIO729u+B01GXIF3/La0Trzr2nj8NiiK4/z6BqDCRia9zAyUlfWTPeuRgFlwx1ktYP3pI6UgUfipio=
		

        System.out.println(d.decrypt(message)); 
        

   } 
   	
      /*  for (byte i : b) {
        	
            System.out.println(i);
        }     
        
        
        System.out.println(new String(b, java.nio.charset.StandardCharsets.UTF_8));
        
*/
	
	

}

