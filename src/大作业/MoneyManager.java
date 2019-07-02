package ����ҵ;
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

//��¼����
class LoginFrame extends JFrame implements ActionListener{
	private JLabel l_user,l_pwd; //�û�����ǩ�������ǩ
	private JTextField t_user;//�û����ı���
	private JPasswordField t_pwd; //�����ı���
	private JButton b_ok,b_cancel,b_make; //��¼��ť���˳���ť,ע�ᰴť
	
	public LoginFrame(){
		super("��ӭʹ�ø�������˱�!");
		l_user=new JLabel("�û�����",JLabel.RIGHT);
		l_pwd=new JLabel(" ���룺",JLabel.RIGHT);
		t_user=new JTextField(31);
		t_pwd=new JPasswordField(31);
		b_ok=new JButton("��¼");
		b_cancel=new JButton("�˳�");
		b_make=new JButton("ע��");
		//���ַ�ʽFlowLayout��һ����������һ��
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout()); 
		c.add(l_user);
		c.add(t_user);
		c.add(l_pwd);
		c.add(t_pwd);
		c.add(b_ok);
		c.add(b_cancel);
		c.add(b_make);
		//Ϊ��ť��Ӽ����¼�
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		b_make.addActionListener(this);
        //�����С���ɵ��� 
		 this.setResizable(false);
		this.setSize(455,150);
		
		//������ʾ����
		Dimension screen = this.getToolkit().getScreenSize();
	        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	public void actionPerformed(ActionEvent e) {
		if(b_cancel==e.getSource()){

       int n = JOptionPane.showConfirmDialog(null, "ȷ���˳���?", "ȷ�϶Ի���", JOptionPane.YES_NO_OPTION); 
       if (n == JOptionPane.YES_OPTION) { 
	     System.exit(0);
       } else if (n == JOptionPane.NO_OPTION) { 
	   JOptionPane.showMessageDialog(new JFrame(),"��ȡ��");
     }
			//��ֹ����
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
					JOptionPane.showMessageDialog(null,"�û����������", "����", JOptionPane.ERROR_MESSAGE);
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
		else if(b_make==e.getSource()) { //ע��
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
					JOptionPane.showMessageDialog(null,"�û����ظ�", "����", JOptionPane.ERROR_MESSAGE);
				else {
					 al.put(user, pwd);
					 JOptionPane.showMessageDialog(null,"ע��ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
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



//JOptionPane.showMessageDialog(null,"�û����������", "����", //JOptionPane.ERROR_MESSAGE);


//������
class MainFrame extends JFrame implements ActionListener{
	private JMenuBar mb=new JMenuBar();
	private JMenu m_system=new JMenu("ϵͳ����");
	private JMenu m_fm=new JMenu("��֧����");
	private JMenuItem mI[]={new JMenuItem("��������"),new JMenuItem("�˳�ϵͳ")};
	private JMenuItem m_FMEdit=new JMenuItem("��֧�༭");
	private JLabel l_type,l_fromdate,l_todate,l_bal,l_ps;  
	private JTextField t_fromdate,t_todate; 
	private JButton b_select1,b_select2;
	private JComboBox c_type;
	private JPanel p_condition,p_detail;
	private String s1[]={"����","֧��"};
	private double bal1,bal2;	
	private JTable table;
	private String username;
	
	public MainFrame(String username){
		super(username+",��ӭʹ�ø�������˱�!");
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
	    
	    l_type=new JLabel("��֧���ͣ�");	
	    c_type=new JComboBox(s1);
	    b_select1=new JButton("��ѯ");
		l_fromdate=new JLabel("��ʼʱ��");
        t_fromdate=new JTextField(8);
		l_todate=new JLabel("��ֹʱ��");
        t_todate=new JTextField(8);
		b_select2=new JButton("��ѯ");
		l_ps = new JLabel("ע�⣺ʱ���ʽΪYYYYMMDD�����磺20150901");
		p_condition=new JPanel();
		p_condition.setLayout(new GridLayout(3,1));
		p_condition.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder("�����ѯ����"), 
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
	    BorderFactory.createTitledBorder("��֧��ϸ��Ϣ"), 
	    BorderFactory.createEmptyBorder(5,5,5,5)));
        l_bal=new JLabel();
        String[] cloum = {"���", "����", "����","����","���",};
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
 				   if(type.equals("����")) {
 					   bal1+=al.get(i).getMoney();
 				   }
 				   else if(type.equals("֧��")) {
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
		    l_bal.setText("��������֧���Ϊ"+bal1+"Ԫ�����ѳ�֧�����ʶ����ѣ�");	
		else  		
			l_bal.setText("��������֧���Ϊ"+bal1+"Ԫ��");   				
		 	
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
	    	 int n = JOptionPane.showConfirmDialog(null, "ȷ���˳���?", "ȷ�϶Ի���", JOptionPane.YES_NO_OPTION); 
	    	 if (n == JOptionPane.YES_OPTION) { 
	    	 	System.exit(0);
	    	 } else if (n == JOptionPane.NO_OPTION) { 
	    	 	JOptionPane.showMessageDialog(new JFrame(),"��ȡ��");
	    	 }
	     }else if(temp==m_FMEdit){
	    	new BalEditFrame();
	     }else if(temp==b_select1){  //������֧���Ͳ�ѯ	 
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
	     }else if(temp==b_select2){   //����ʱ�䷶Χ��ѯ		 
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
//�޸��������
class ModifyPwdFrame extends JFrame implements ActionListener{
	private JLabel l_oldPWD,l_newPWD,l_newPWDAgain;
	private JPasswordField t_oldPWD,t_newPWD,t_newPWDAgain;
	private JButton b_ok,b_cancel;
	private String username;
	
	public ModifyPwdFrame(String username){
		super("�޸�����");
		this.username=username;
		l_oldPWD=new JLabel("������");
		l_newPWD=new JLabel("�����룺");
		l_newPWDAgain=new JLabel("ȷ�������룺");
		t_oldPWD=new JPasswordField(15);
		t_newPWD=new JPasswordField(15);
		t_newPWDAgain=new JPasswordField(15);
		b_ok=new JButton("ȷ��");
		b_cancel=new JButton("ȡ��");
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
		if(b_cancel==e.getSource()){     //ȡ����ť
			this.dispose();  //�رյ�ǰ����
		}else if(b_ok==e.getSource()){  //�޸�����
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
					JOptionPane.showMessageDialog(null,"ԭ���벻ƥ��", "����", JOptionPane.ERROR_MESSAGE);
				}
				else if(!newPWD.equals(newPWDagain)) {
					JOptionPane.showMessageDialog(null,"���������벻��ȷ", "����", JOptionPane.ERROR_MESSAGE);
				}
				else {
					fos=new FileOutputStream(f);
				    oos=new ObjectOutputStream(fos);
				    al.put(this.username, newPWD);
					//System.out.println(al.get(this.username));
					//oos.flush();
					oos.writeObject(al);
					JOptionPane.showMessageDialog(new JFrame(),"���޸�");
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
//��֧�༭����
class BalEditFrame extends JFrame implements ActionListener{
	private JLabel l_id,l_date,l_bal,l_type,l_item;
	private JTextField t_id,t_date,t_bal;
	private JComboBox c_type,c_item;
	private JButton b_update,b_delete,b_select,b_new,b_clear;
	private JPanel p1,p2,p3;
	private JScrollPane scrollpane;
	private JTable table;

	public BalEditFrame(){
		super("��֧�༭" );
		l_id=new JLabel("��ţ�");
		l_date=new JLabel("���ڣ�");
		l_bal=new JLabel("��");
		l_type=new JLabel("���ͣ�");
		l_item=new JLabel("���ݣ�");
		t_id=new JTextField(8);
		t_date=new JTextField(8);
		t_bal=new JTextField(8);

		String s1[]={"����","֧��"};
		String s2[]={"����","����","�Ӽ�","��ͨ","����","����","����","����","����"};
		c_type=new JComboBox(s1);
		c_item=new JComboBox(s2);
		
		b_select=new JButton("��ѯ");
		b_update=new JButton("�޸�");
		b_delete=new JButton("ɾ��");
		b_new=new JButton("¼��");
		b_clear=new JButton("���");
		
		Container c=this.getContentPane();
		c.setLayout(new BorderLayout());
		
		p1=new JPanel();
        p1.setLayout(new GridLayout(5,2,10,10));
        p1.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("�༭��֧��Ϣ"), 
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
		BorderFactory.createTitledBorder("��ʾ��֧��Ϣ"), 
		BorderFactory.createEmptyBorder(5,5,5,5)));		
				
		String[] cloum = { "���", "����", "����","����", "���"};
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
		if(b_select==e.getSource()){  //��ѯ������֧��Ϣ
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
		}else if(b_update==e.getSource()){  // �޸�ĳ����֧��Ϣ
			if(t_id.getText().isEmpty()||t_date.getText().isEmpty()||t_bal.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null,"��Ϣ����Ϊ��", "����", JOptionPane.ERROR_MESSAGE);
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
						JOptionPane.showMessageDialog(null,"��ƥ���ţ��޷��޸�", "����", JOptionPane.ERROR_MESSAGE);
					}else {
						al.remove(new balance(al.get(t).getId(),al.get(t).getDate(),al.get(t).getType(),al.get(t).getContent(),al.get(t).getMoney()));
					    al.add(new balance(Integer.parseInt(t_id.getText()),Integer.parseInt(t_date.getText()),(String)c_type.getSelectedItem(),(String)c_item.getSelectedItem(),Double.parseDouble(t_bal.getText())));
					    JOptionPane.showMessageDialog(null,"���޸�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
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
		}else if(b_delete==e.getSource()){   //ɾ��ĳ����֧��Ϣ
			if(t_id.getText().isEmpty()||t_date.getText().isEmpty()||t_bal.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null,"��Ϣ����Ϊ��", "����", JOptionPane.ERROR_MESSAGE);
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
						 JOptionPane.showMessageDialog(null,"ɾ���ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					}
					else 
						JOptionPane.showMessageDialog(null,"����֧��Ϣ�����ڣ�ɾ��ʧ��", "����", JOptionPane.ERROR_MESSAGE);
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
		}else if(b_new==e.getSource()){   //����ĳ����֧��Ϣ 	
			if(t_id.getText().isEmpty()||t_date.getText().isEmpty()||t_bal.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null,"��Ϣ����Ϊ��", "����", JOptionPane.ERROR_MESSAGE);
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
					if(flag==1) JOptionPane.showMessageDialog(null,"����Ѵ��ڣ�¼��ʧ��", "����", JOptionPane.ERROR_MESSAGE);
					else{
						  al.add(new balance(Integer.parseInt(t_id.getText()),Integer.parseInt(t_date.getText()),(String)c_type.getSelectedItem(),(String)c_item.getSelectedItem(),Double.parseDouble(t_bal.getText())));
						  JOptionPane.showMessageDialog(null,"¼��ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
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
		}else if(b_clear==e.getSource()){   //��������
			t_id.setText(null);
			t_date.setText(null);
			t_bal.setText(null);
		}	
	}
}
 
