package 大作业;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.AncestorListener;


public class MoneyManager {
	public static void main(String[] args) {
		 LoginFrame lf=new LoginFrame();
		 lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

//登录界面
class LoginFrame extends JFrame implements ActionListener{
	private JLabel l_user,l_pwd; //用户名标签，密码标签
	private JTextField t_user;//用户名文本框
	private JPasswordField t_pwd; //密码文本框
	private JButton b_ok,b_cancel,b_make; //登录按钮，退出按钮,注册按钮
	
	public LoginFrame(){
		super("欢迎使用个人理财账本!");
		l_user=new JLabel("用户名：",JLabel.RIGHT);
		l_pwd=new JLabel(" 密码：",JLabel.RIGHT);
		t_user=new JTextField(31);
		t_pwd=new JPasswordField(31);
		b_ok=new JButton("登录");
		b_cancel=new JButton("退出");
		b_make=new JButton("注册");
		//布局方式FlowLayout，一行排满排下一行
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout()); 
		c.add(l_user);
		c.add(t_user);
		c.add(l_pwd);
		c.add(t_pwd);
		c.add(b_ok);
		c.add(b_cancel);
		c.add(b_make);
		//为按钮添加监听事件
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		b_make.addActionListener(this);
        //界面大小不可调整 
		 this.setResizable(false);
		this.setSize(455,150);
		
		//界面显示居中
		Dimension screen = this.getToolkit().getScreenSize();
	        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	public void actionPerformed(ActionEvent e) {
		if(b_cancel==e.getSource()){

       int n = JOptionPane.showConfirmDialog(null, "确认退出吗?", "确认对话框", JOptionPane.YES_NO_OPTION); 
       if (n == JOptionPane.YES_OPTION) { 
	     System.exit(0);
       } else if (n == JOptionPane.NO_OPTION) { 
	   JOptionPane.showMessageDialog(new JFrame(),"已取消");
     }
			//终止程序
		}else if(b_ok==e.getSource()){
			File f=new File("users.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
	 		try{
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				Map<String,String> al=new HashMap<String,String>();
				try {
					al=(HashMap<String,String>)iis.readObject();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String pwd=t_pwd.getText();
				//System.out.println(pwd);
				String ddd=al.get(t_user.getText());
				//System.out.println(ddd);
				if(pwd.equals(ddd)) {
					 new MainFrame(t_user.getText().trim());
				}
				else {
					JOptionPane.showMessageDialog(null,"用户名密码出错", "警告", JOptionPane.ERROR_MESSAGE);
				}
			}catch(IOException e1) {
				System.out.println(e1.getMessage());
			}finally {
				try {
					if(iis!=null) iis.close();
					if(fis!=null) fis.close();
				}catch(IOException e1) {
					System.out.println(e1.getMessage());
				}
			}
			  
		}
		else if(b_make==e.getSource()) { //注册
			File f=new File("users.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
			FileOutputStream fos=null;
			ObjectOutputStream oos=null;
	 		try{
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				Map<String,String> al=new HashMap<String,String>();
				try {
					al=(HashMap<String,String>)iis.readObject();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				iis.close();fis.close();
				fos=new FileOutputStream(f);
				oos=new ObjectOutputStream(fos);
				String user=t_user.getText();
				String pwd=t_pwd.getText();
				if(al.containsKey(user))
					JOptionPane.showMessageDialog(null,"用户名重复", "警告", JOptionPane.ERROR_MESSAGE);
				else {
					 al.put(user, pwd);
					 JOptionPane.showMessageDialog(null,"注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
				} 
				oos.writeObject(al);
			}catch(IOException e1) {
				System.out.println(e1.getMessage());
			}finally {
				try {
					if(iis!=null) iis.close();
					if(fis!=null) fis.close();
					if(oos!=null) oos.close();
					if(fos!=null) fos.close();
				}catch(IOException e1) {
					System.out.println(e1.getMessage());
				}
			}
		}
	}

	
	
}



//JOptionPane.showMessageDialog(null,"用户名密码出错", "警告", //JOptionPane.ERROR_MESSAGE);


//主界面
class MainFrame extends JFrame implements ActionListener{
	private JMenuBar mb=new JMenuBar();
	private JMenu m_system=new JMenu("系统管理");
	private JMenu m_fm=new JMenu("收支管理");
	private JMenuItem mI[]={new JMenuItem("密码重置"),new JMenuItem("退出系统")};
	private JMenuItem m_FMEdit=new JMenuItem("收支编辑");
	private JLabel l_type,l_fromdate,l_todate,l_bal,l_ps;  
	private JTextField t_fromdate,t_todate; 
	private JButton b_select1,b_select2;
	private JComboBox c_type;
	private JPanel p_condition,p_detail;
	private String s1[]={"收入","支出"};
	private double bal1,bal2;	
	private JTable table;
	private String username;
	
	public MainFrame(String username){
		super(username+",欢迎使用个人理财账本!");
		this.username=username;
		Container c=this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(mb,"North");
		mb.add(m_system);
		mb.add(m_fm);
		m_system.add(mI[0]);
		m_system.add(mI[1]);
		m_fm.add(m_FMEdit);
		m_FMEdit.addActionListener(this);
	        mI[0].addActionListener(this);
	    mI[1].addActionListener(this);
	    
	    l_type=new JLabel("收支类型：");	
	    c_type=new JComboBox(s1);
	    b_select1=new JButton("查询");
		l_fromdate=new JLabel("起始时间");
        t_fromdate=new JTextField(8);
		l_todate=new JLabel("终止时间");
        t_todate=new JTextField(8);
		b_select2=new JButton("查询");
		l_ps = new JLabel("注意：时间格式为YYYYMMDD，例如：20150901");
		p_condition=new JPanel();
		p_condition.setLayout(new GridLayout(3,1));
		p_condition.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder("输入查询条件"), 
	    BorderFactory.createEmptyBorder(5,5,5,5)));
		
		JPanel p1 = new JPanel();
	    JPanel p2 = new JPanel();
	    JPanel p3 = new JPanel();
	    p1.add(l_type);
	    p1.add(c_type);
	    p1.add(b_select1);
	    p2.add(l_fromdate);
		p2.add(t_fromdate);
		p2.add(l_todate);
		p2.add(t_todate);
		p2.add(b_select2);
		p3.add(l_ps);
		p_condition.add(p1);
	    p_condition.add(p2);
	    p_condition.add(p3);
        c.add(p_condition,"Center");
        
        b_select1.addActionListener(this);
        b_select2.addActionListener(this);
        
        p_detail=new JPanel();
        p_detail.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder("收支明细信息"), 
	    BorderFactory.createEmptyBorder(5,5,5,5)));
        l_bal=new JLabel();
        String[] cloum = {"编号", "日期", "类型","内容","金额",};
		Object[][] row = new Object[50][5];
		table = new JTable(row, cloum);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(580,350));
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setViewportView(table);
		p_detail.add(l_bal);
		p_detail.add(scrollpane);
		c.add(p_detail,"South");
		
		bal1=0;
		File f=new File("balances.txt");
 		FileInputStream fis=null;
 		ObjectInputStream iis=null;
 		try {
 			fis=new FileInputStream(f);
 			iis=new ObjectInputStream(fis);
 			ArrayList<balance> al=new ArrayList<balance>();
 			try {
 				al=(ArrayList<balance>)iis.readObject();
 				for(int i=0;i<al.size();i++) {
 				   String type=al.get(i).getType();
 				   if(type.equals("收入")) {
 					   bal1+=al.get(i).getMoney();
 				   }
 				   else if(type.equals("支出")) {
 					   bal1-=al.get(i).getMoney();
 				   }
 				table.setValueAt(al.get(i).getId(), i, 0);
 				table.setValueAt(al.get(i).getDate(), i, 1);
 				table.setValueAt(al.get(i).getType(), i, 2);
 				table.setValueAt(al.get(i).getContent(), i, 3);
 				table.setValueAt(al.get(i).getMoney(), i, 4);
 						
 				}
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
		
	    if(bal1<0)
		    l_bal.setText("个人总收支余额为"+bal1+"元。您已超支，请适度消费！");	
		else  		
			l_bal.setText("个人总收支余额为"+bal1+"元。");   				
		 	
        this.setResizable(false);
		this.setSize(600,580);
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	
   public void actionPerformed(ActionEvent e) {
	     Object temp=e.getSource();
	     if(temp==mI[0]){
	    	new ModifyPwdFrame(username);
	     }else if(temp==mI[1]){
	    	 int n = JOptionPane.showConfirmDialog(null, "确认退出吗?", "确认对话框", JOptionPane.YES_NO_OPTION); 
	    	 if (n == JOptionPane.YES_OPTION) { 
	    	 	System.exit(0);
	    	 } else if (n == JOptionPane.NO_OPTION) { 
	    	 	JOptionPane.showMessageDialog(new JFrame(),"已取消");
	    	 }
	     }else if(temp==m_FMEdit){
	    	new BalEditFrame();
	     }else if(temp==b_select1){  //根据收支类型查询	 
	    	 File f=new File("balances.txt");
	 		FileInputStream fis=null;
	 		ObjectInputStream iis=null;
	 		try {
	 			fis=new FileInputStream(f);
	 			iis=new ObjectInputStream(fis);
	 			ArrayList<balance> al=new ArrayList<balance>();
	 			try {
	 				al=(ArrayList<balance>)iis.readObject();
	 				Collections.sort(al, new SortBy());
	 				int count=0;
	 				for(int i=0;i<50;i++) {
	 					table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
	 						
	 				}
	 				for(int i=0;i<al.size();i++) {
	 					if(i==0) count=0;
	 					String type=al.get(i).getType();
	 					if(type.equals(c_type.getSelectedItem())) {
	 						table.setValueAt(al.get(i).getId(), count, 0);
							table.setValueAt(al.get(i).getDate(), count, 1);
							table.setValueAt(al.get(i).getType(), count, 2);
							table.setValueAt(al.get(i).getContent(), count, 3);
							table.setValueAt(al.get(i).getMoney(), count++, 4);
	 					}
	 						
	 				}
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
	     }else if(temp==b_select2){   //根据时间范围查询		 
	    	 File f=new File("balances.txt");
		 		FileInputStream fis=null;
		 		ObjectInputStream iis=null;
		 		try {
		 			fis=new FileInputStream(f);
		 			iis=new ObjectInputStream(fis);
		 			ArrayList<balance> al=new ArrayList<balance>();
		 			try {
		 				al=(ArrayList<balance>)iis.readObject();
		 				Collections.sort(al, new SortBy());
		 				int count=0;
		 				for(int i=0;i<50;i++) {
		 					table.setValueAt(null, i, 0);
							table.setValueAt(null, i, 1);
							table.setValueAt(null, i, 2);
							table.setValueAt(null, i, 3);
							table.setValueAt(null, i, 4);
		 						
		 				}
		 				for(int i=0;i<al.size();i++) {
		 					if(i==0) count=0;
		 					String date=t_fromdate.getText();
		 					int date1=Integer.parseInt(date);
		 					date=t_todate.getText();
		 					int date2=Integer.parseInt(date);
		 					if(date1<=al.get(i).getDate()&&date2>=al.get(i).getDate()) {
		 						table.setValueAt(al.get(i).getId(), count, 0);
								table.setValueAt(al.get(i).getDate(), count, 1);
								table.setValueAt(al.get(i).getType(), count, 2);
								table.setValueAt(al.get(i).getContent(), count, 3);
								table.setValueAt(al.get(i).getMoney(), count++, 4);
		 					}		 						
		 				}
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
}
//修改密码界面
class ModifyPwdFrame extends JFrame implements ActionListener{
	private JLabel l_oldPWD,l_newPWD,l_newPWDAgain;
	private JPasswordField t_oldPWD,t_newPWD,t_newPWDAgain;
	private JButton b_ok,b_cancel;
	private String username;
	
	public ModifyPwdFrame(String username){
		super("修改密码");
		this.username=username;
		l_oldPWD=new JLabel("旧密码");
		l_newPWD=new JLabel("新密码：");
		l_newPWDAgain=new JLabel("确认新密码：");
		t_oldPWD=new JPasswordField(15);
		t_newPWD=new JPasswordField(15);
		t_newPWDAgain=new JPasswordField(15);
		b_ok=new JButton("确定");
		b_cancel=new JButton("取消");
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout());
		c.add(l_oldPWD);
		c.add(t_oldPWD);
		c.add(l_newPWD);
		c.add(t_newPWD);
		c.add(l_newPWDAgain);
		c.add(t_newPWDAgain);
		c.add(b_ok);
		c.add(b_cancel);
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		this.setResizable(false);
		this.setSize(280,160);
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(b_cancel==e.getSource()){     //取消按钮
			this.dispose();  //关闭当前窗口
		}else if(b_ok==e.getSource()){  //修改密码
			File f=new File("users.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
			FileOutputStream fos=null;
			ObjectOutputStream oos=null; 
	 		try{
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				Map<String,String> al=new HashMap<String,String>();
				try {
					al=(HashMap<String,String>)iis.readObject();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
				}
				fis.close();iis.close();
				String oldpwd=t_oldPWD.getText();
				//System.out.println(this.username);
				String ddd=al.get(this.username);
				String newPWD=t_newPWD.getText();
				
				String newPWDagain=t_newPWDAgain.getText();
				if(!oldpwd.equals(ddd)) {
					JOptionPane.showMessageDialog(null,"原密码不匹配", "警告", JOptionPane.ERROR_MESSAGE);
				}
				else if(!newPWD.equals(newPWDagain)) {
					JOptionPane.showMessageDialog(null,"两次新密码不正确", "警告", JOptionPane.ERROR_MESSAGE);
				}
				else {
					fos=new FileOutputStream(f);
				    oos=new ObjectOutputStream(fos);
				    al.put(this.username, newPWD);
					//System.out.println(al.get(this.username));
					//oos.flush();
					oos.writeObject(al);
					JOptionPane.showMessageDialog(new JFrame(),"已修改");
					this.dispose();
				}
				
			}catch(IOException e1) {
				System.out.println(e1.getMessage());
			}finally {
				try {
					if(oos!=null) oos.close();
					if(iis!=null) iis.close();
					if(fis!=null) fis.close();
					if(fos!=null) fos.close();
				}catch(IOException e1) {
					System.out.println(e1.getMessage());
				}
			}
	 		
		}
	}
}
//收支编辑界面
class BalEditFrame extends JFrame implements ActionListener{
	private JLabel l_id,l_date,l_bal,l_type,l_item;
	private JTextField t_id,t_date,t_bal;
	private JComboBox c_type,c_item;
	private JButton b_update,b_delete,b_select,b_new,b_clear;
	private JPanel p1,p2,p3;
	private JScrollPane scrollpane;
	private JTable table;

	public BalEditFrame(){
		super("收支编辑" );
		l_id=new JLabel("编号：");
		l_date=new JLabel("日期：");
		l_bal=new JLabel("金额：");
		l_type=new JLabel("类型：");
		l_item=new JLabel("内容：");
		t_id=new JTextField(8);
		t_date=new JTextField(8);
		t_bal=new JTextField(8);

		String s1[]={"收入","支出"};
		String s2[]={"购物","餐饮","居家","交通","娱乐","人情","工资","奖金","其他"};
		c_type=new JComboBox(s1);
		c_item=new JComboBox(s2);
		
		b_select=new JButton("查询");
		b_update=new JButton("修改");
		b_delete=new JButton("删除");
		b_new=new JButton("录入");
		b_clear=new JButton("清空");
		
		Container c=this.getContentPane();
		c.setLayout(new BorderLayout());
		
		p1=new JPanel();
        p1.setLayout(new GridLayout(5,2,10,10));
        p1.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("编辑收支信息"), 
        BorderFactory.createEmptyBorder(5,5,5,5)));
		p1.add(l_id);
		p1.add(t_id);
		p1.add(l_date);
		p1.add(t_date);
		p1.add(l_type);
		p1.add(c_type);
		p1.add(l_item);
		p1.add(c_item);
		p1.add(l_bal);
		p1.add(t_bal);
		c.add(p1, BorderLayout.WEST);
		
		p2=new JPanel();
		p2.setLayout(new GridLayout(5,1,10,10));
		p2.add(b_new);
		p2.add(b_update);
		p2.add(b_delete);
		p2.add(b_select);
		p2.add(b_clear);
	    
		c.add(p2,BorderLayout.CENTER);	
		
		p3=new JPanel();
		p3.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createTitledBorder("显示收支信息"), 
		BorderFactory.createEmptyBorder(5,5,5,5)));		
				
		String[] cloum = { "编号", "日期", "类型","内容", "金额"};
		Object[][] row = new Object[50][5];
		table = new JTable(row, cloum);
		scrollpane = new JScrollPane(table);
		scrollpane.setViewportView(table);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		p3.add(scrollpane);
		c.add(p3,BorderLayout.EAST);		
	 
		b_update.addActionListener(this);
		b_delete.addActionListener(this);
		b_select.addActionListener(this);
		b_new.addActionListener(this);
		b_clear.addActionListener(this);
	    
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			//int clickCount = e.getClickCount();
			int count=table.getSelectedRow();
			if (table==e.getSource()) {
			 // System.out.println(count);
			  t_id.setText(table.getValueAt(count, 0).toString());
			  t_date.setText(table.getValueAt(count, 1).toString());
			  c_type.setSelectedItem(table.getValueAt(count, 2).toString());
			  c_item.setSelectedItem(table.getValueAt(count, 3).toString());
			  t_bal.setText(table.getValueAt(count, 4).toString());
			}
			}
			});
		
	    this.setResizable(false);
		this.setSize(800,300);
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	public void actionPerformed(ActionEvent e) {
		if(b_select==e.getSource()){  //查询所有收支信息
			File f=new File("balances.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
			try {
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				ArrayList<balance> al=new ArrayList<balance>();
				try {
					al=(ArrayList<balance>)iis.readObject();
					Collections.sort(al, new SortBy());
					for(int i=0;i<50;i++) {
	 					table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
	 						
	 				}
					for(int i=0;i<al.size();i++) {
						table.setValueAt(al.get(i).getId(), i, 0);
						table.setValueAt(al.get(i).getDate(), i, 1);
						table.setValueAt(al.get(i).getType(), i, 2);
						table.setValueAt(al.get(i).getContent(), i, 3);
						table.setValueAt(al.get(i).getMoney(), i, 4);
					}
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
		}else if(b_update==e.getSource()){  // 修改某条收支信息
			if(t_id.getText().isEmpty()||t_date.getText().isEmpty()||t_bal.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null,"信息不能为空", "警告", JOptionPane.ERROR_MESSAGE);
			}else {
			File f=new File("balances.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
			FileOutputStream fos=null;
			ObjectOutputStream oos=null;
			try {
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				ArrayList<balance> al=new ArrayList<balance>();
				try {
					al=(ArrayList<balance>)iis.readObject();
					iis.close();fis.close();
					fos=new FileOutputStream(f);
					oos=new ObjectOutputStream(fos);
           
					
				
					int flag=0,t=0;
					for(int i=0;i<al.size();i++) {
						if(al.get(i).getId()==Integer.parseInt(t_id.getText())) {
							flag=1;
							t=i;
							break;
							
						}
					}
					if(flag==0) {
						JOptionPane.showMessageDialog(null,"无匹配编号，无法修改", "警告", JOptionPane.ERROR_MESSAGE);
					}else {
						al.remove(new balance(al.get(t).getId(),al.get(t).getDate(),al.get(t).getType(),al.get(t).getContent(),al.get(t).getMoney()));
					    al.add(new balance(Integer.parseInt(t_id.getText()),Integer.parseInt(t_date.getText()),(String)c_type.getSelectedItem(),(String)c_item.getSelectedItem(),Double.parseDouble(t_bal.getText())));
					    JOptionPane.showMessageDialog(null,"已修改", "提示", JOptionPane.INFORMATION_MESSAGE);
					}	
					Collections.sort(al, new SortBy());
					oos.writeObject(al);
					for(int i=0;i<50;i++) {
	 					table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
	 						
	 				}
					for(int i=0;i<al.size();i++) {
						table.setValueAt(al.get(i).getId(), i, 0);
						table.setValueAt(al.get(i).getDate(), i, 1);
						table.setValueAt(al.get(i).getType(), i, 2);
						table.setValueAt(al.get(i).getContent(), i, 3);
						table.setValueAt(al.get(i).getMoney(), i, 4);
					}
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
				    if(oos!=null) oos.close();
				    if(fos!=null) fos.close();
				   }catch(IOException e1) {
					   System.out.println(e1.getMessage());
				   }
			}
			}
		}else if(b_delete==e.getSource()){   //删除某条收支信息
			if(t_id.getText().isEmpty()||t_date.getText().isEmpty()||t_bal.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null,"信息不能为空", "警告", JOptionPane.ERROR_MESSAGE);
			}else {
			File f=new File("balances.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
			FileOutputStream fos=null;
			ObjectOutputStream oos=null;
			try {
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				ArrayList<balance> al=new ArrayList<balance>();
				try {
					al=(ArrayList<balance>)iis.readObject();
					iis.close();fis.close();
					fos=new FileOutputStream(f);
					oos=new ObjectOutputStream(fos);
					if(al.contains(new balance(Integer.parseInt(t_id.getText()),Integer.parseInt(t_date.getText()),(String)c_type.getSelectedItem(),(String)c_item.getSelectedItem(),Double.parseDouble(t_bal.getText())))) {
						 al.remove(new balance(Integer.parseInt(t_id.getText()),Integer.parseInt(t_date.getText()),(String)c_type.getSelectedItem(),(String)c_item.getSelectedItem(),Double.parseDouble(t_bal.getText())));
						 JOptionPane.showMessageDialog(null,"删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
					}
					else 
						JOptionPane.showMessageDialog(null,"该收支信息不存在，删除失败", "警告", JOptionPane.ERROR_MESSAGE);
					Collections.sort(al, new SortBy());
					oos.writeObject(al);
					for(int i=0;i<50;i++) {
	 					table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
	 						
	 				}
					for(int i=0;i<al.size();i++) {
						table.setValueAt(al.get(i).getId(), i, 0);
						table.setValueAt(al.get(i).getDate(), i, 1);
						table.setValueAt(al.get(i).getType(), i, 2);
						table.setValueAt(al.get(i).getContent(), i, 3);
						table.setValueAt(al.get(i).getMoney(), i, 4);
					}
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
				    if(oos!=null) oos.close();
				    if(fos!=null) fos.close();
				   }catch(IOException e1) {
					   System.out.println(e1.getMessage());
				   }
			}
			}
		}else if(b_new==e.getSource()){   //新增某条收支信息 	
			if(t_id.getText().isEmpty()||t_date.getText().isEmpty()||t_bal.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null,"信息不能为空", "警告", JOptionPane.ERROR_MESSAGE);
			}else {
			File f=new File("balances.txt");
			FileInputStream fis=null;
			ObjectInputStream iis=null;
			FileOutputStream fos=null;
			ObjectOutputStream oos=null;
			try {
				fis=new FileInputStream(f);
				iis=new ObjectInputStream(fis);
				ArrayList<balance> al=new ArrayList<balance>();
				try {
					al=(ArrayList<balance>)iis.readObject();
					iis.close();fis.close();
					fos=new FileOutputStream(f);
					oos=new ObjectOutputStream(fos);
					int flag=0;
					for(int i=0;i<al.size();i++) {
						if(al.get(i).getId()==Integer.parseInt(t_id.getText())) {
							flag=1;
							break;
						}
					}
					if(flag==1) JOptionPane.showMessageDialog(null,"编号已存在，录入失败", "警告", JOptionPane.ERROR_MESSAGE);
					else{
						  al.add(new balance(Integer.parseInt(t_id.getText()),Integer.parseInt(t_date.getText()),(String)c_type.getSelectedItem(),(String)c_item.getSelectedItem(),Double.parseDouble(t_bal.getText())));
						  JOptionPane.showMessageDialog(null,"录入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
						}
					Collections.sort(al, new SortBy());
					oos.writeObject(al);
					for(int i=0;i<50;i++) {
	 					table.setValueAt(null, i, 0);
						table.setValueAt(null, i, 1);
						table.setValueAt(null, i, 2);
						table.setValueAt(null, i, 3);
						table.setValueAt(null, i, 4);
	 						
	 				}
					for(int i=0;i<al.size();i++) {
						table.setValueAt(al.get(i).getId(), i, 0);
						table.setValueAt(al.get(i).getDate(), i, 1);
						table.setValueAt(al.get(i).getType(), i, 2);
						table.setValueAt(al.get(i).getContent(), i, 3);
						table.setValueAt(al.get(i).getMoney(), i, 4);
					}
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
				    if(oos!=null) oos.close();
				    if(fos!=null) fos.close();
				   }catch(IOException e1) {
					   System.out.println(e1.getMessage());
				   }
			}
			}
		}else if(b_clear==e.getSource()){   //清空输入框
			t_id.setText(null);
			t_date.setText(null);
			t_bal.setText(null);
		}	
	}
}
 
