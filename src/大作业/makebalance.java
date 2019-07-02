package 大作业;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import java.io.*;
public class makebalance {

	public static void main(String[] args) {
		File f=new File("balances.txt");
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
		  ArrayList<balance> al=new ArrayList<balance>();
		al.add(new balance(1234,20190604,"收入","工资",3.00));
		al.add(new balance(1235,20190604,"收入","其他",4.00));
		al.add(new balance(1236,20190604,"支出","购物",5.00));
		try {
			fos=new FileOutputStream(f);
			oos=new ObjectOutputStream(fos);
			oos.writeObject(al);
		}catch(IOException e1) {
			System.out.println(e1.getMessage());
		}finally {
			
				try {
					  if(oos!=null) oos.close();
					  if(fos!=null) fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}
	

}

class balance implements java.io.Serializable{
	private long id;
	private long date;
	private String type;
	private String content;
	private double money;
	public long getId() {
		return id;
	}
	public long getDate() {
		return date;
	}
	public String getType() {
		return type;
	}
	public String getContent() {
		return content;
	}
	public double getMoney() {
		return money;
	}
	public balance(long id, long date, String type, String content, double money) {
		super();
		this.id = id;
		this.date = date;
		this.type = type;
		this.content = content;
		this.money = money;
	}
	@Override
	public int hashCode() {
		//final int prime = 31;
		//int result = 1;
		//result = prime * result + id;
		return 1;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
        if (obj instanceof balance){
            balance  stu = (balance) obj;
            if (stu.id == this.id&&stu.date==this.date&&stu.money==stu.money&&stu.content.equals(this.content)&&stu.type.equals(this.type))
                {
            	return true;
                }
        }
        //JOptionPane.showMessageDialog(new JFrame(),"删除失败");
        return false;
    }
}
class SortBy implements Comparator<balance> {
  public int compare(balance obj1, balance obj2) {
       balance point1 =  obj1;
        balance point2 =  obj2;
       // System.out.println("asogasuofhoiaefhiuew");//测试
        if (point1.getId() <= point2.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
}