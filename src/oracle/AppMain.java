package oracle;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class AppMain extends JFrame implements ItemListener{
	ConnectionManager manager;
	Connection con;	// ��� ��ü�� �����ϱ� ����, ���ø����̼��� ������ �� �ø���
	JTable table;
	JScrollPane scroll;
	JPanel p_west, p_center;
	Choice choice;
	String[][] item={
		{"�� ���̺��� �����ϼ���", ""},
		{"��� ���̺� ����", "emp"},
		{"�μ� ���̺� ����", "dept"}
	};
	
	// �ƹ� ���� ���� ���̺��̳� emp, dept�� �迭�� �����ؼ� �����ϱ� ���� ��������!
	TableModel[] model=new TableModel[item.length];
	
	public AppMain() {
		// �����ΰ� ������ �и���Ű�� ���� �߰���(Controller)�� ���簡 �ʿ�
		// JTable������ �� ��Ʈ�ѷ��� ������ TableModel�� ����!
		// TableModel�� ����� ���, JTable�� �ڽ��� ������� �� �����͸� TableModel�κ��� ������ ���� �����
		// getRowCount(), getColumnCount(), getValueAt()
		
		// ���̺��� �����Ǿ� ���� -> ���� ���
		//table=new JTable(3,2);
		manager=ConnectionManager.getInstance();
		con=manager.getConnection();
		
		table=new JTable();		// table=new JTable(��); -> ������ ���� choice�� ���� ����ڰ� ����, table�� �����Ǵ� �ͺ��� �ʰ� ������
		scroll=new JScrollPane(table);
		p_west=new JPanel();
		p_center=new JPanel();
		choice=new Choice();
		
		
		for(int i=0; i<item.length; i++){
			choice.add(item[i][0]);
		}
		
		// ���̺� �𵨵��� �÷�����
		model[0]=new DefaultTableModel();
		model[1]=new EmpModelUpgrade(con);
		model[2]=new DeptModelUpgrade(con);
		
		// ���̺� ������ �ֱ�
		// ���� ����� ���� ����� �ǹǷ� ������������ ������ -> �����ΰ� ������ ��������
		// ���ʿ� java�� ������Ʈ ����̹Ƿ� �������� X, ������ �и���Ű�� �����θ� ������!
		/*
		table.setValueAt("���", 0, 0);
		table.setValueAt("��", 0, 1);
		table.setValueAt("���", 1, 0);
		table.setValueAt("ƫ��", 1, 1);
		table.setValueAt("�׾�", 2, 0);
		table.setValueAt("�ؾ�", 2, 1);
		*/
		
		// choice ���� - ���ǹ��� ������� �ʱ� ���� 2���� �迭�� �����ϱ�
		/*
		choice.add("���̺� ����");
		choice.add("��� ���̺�");	// select * from emp;
		choice.add("�μ� ���̺�");	// select * from dept;
		*/
		// ������â ���� �� ����Ŭ ���� ����
		this.addWindowListener(new WindowAdapter() {
			public void winDowClosing(WindowEvent e){
				// Ŀ�ؼ� �ݱ�
				manager.disConnect(con);
				
				// ���α׷� ����
				System.exit(0);
			}
		});
	
		
		p_west.add(choice);	// west ���� �гο� choice ����
		p_center.add(scroll);
		
		add(p_west,BorderLayout.WEST);
		add(p_center);
		
		pack();
		
		// choice�� ItemListener ����
		choice.addItemListener(this);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// �ش�Ǵ� ���̺� ���
	public void showData(int index){
		System.out.println("����� ���� �� ���̺��� "+item[index][1]+"�Դϴ�.");
		
		// �ش�Ǵ� ���̺� �� ���
		// emp�� ��� EmpModel, dept�� ��� DeptModel ���
		// �Ѵ� �ƴ� ���, DefaultTableModel���
		table.setModel(model[index]);
	}
	
	// choice �̺�Ʈ ����
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice) e.getSource();
		int index=ch.getSelectedIndex();
		showData(index);
	}
	
	public static void main(String[] args) {
		new AppMain();
	}
}
