package ´ó×÷Òµ;
import java.io.*;
import java.util.*;
public class maketxt {

	public static void main(String[] args) throws ClassNotFoundException {
		
			File f=new File("users.txt");
			FileOutputStream fos=null;
			ObjectOutputStream oos=null;
			
			try {
				if(f.exists());
				else {
				     f.createNewFile();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
			try {
				Map<String,String> map=new HashMap<String,String>();
			    map.put("shenjx", "123123");
			    fos=new FileOutputStream(f);
			    oos=new ObjectOutputStream(fos);
			    oos.writeObject(map);
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try{
				if(oos!=null) oos.close();
			    if(fos!=null) fos.close();
			   }catch(IOException e) {
				   System.out.println(e.getMessage());
			   }
		}
			
		

	}

}

