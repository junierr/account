package ´ó×÷Òµ;
import java.util.*;
import java.io.*;


public class chaxun implements java.io.Serializable {

	public static void main(String[] args) {
		File f=new File("balances.txt");
		FileInputStream fis=null;
		ObjectInputStream iis=null;
		try {
			fis=new FileInputStream(f);
			iis=new ObjectInputStream(fis);
			ArrayList<balance> al=new ArrayList<balance>();
			try {
				al=(ArrayList<balance>)iis.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			
		}catch(IOException e1) {
			System.out.println(e1.getMessage());
		}finally {
			try{
				if(iis!=null) iis.close();
			    if(fis!=null) fis.close();
			   }catch(IOException e1) {
				   System.out.println(e1.getMessage());
			   }
		}

	}

}
