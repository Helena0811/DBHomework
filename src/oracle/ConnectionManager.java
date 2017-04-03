/*
 * ��� ���̺��� �����͸� ó���ϴ� controller
 * 
 * model[0]=new DefaultTableModel();
   model[1]=new EmpModel();
   model[2]=new DeptModel();
 * �̿� ���� ���̺� ���� ���� model�� ����� �޸𸮿� �ø��� �ȴٸ�,
 * model���� ���� ������ �ϰ� �Ǵµ� ���� �ٸ� ���ӱ⸦ �̿��ؼ� ������ �� -> ��� �Ѱ����� ������ �߻��ϸ� �Ҿ���
 * ���ʿ� �����ͺ��̽� ���� �� connection 1���� ��� ���̺� ���� ����
 * 
 * -> Connection�� �и�
 * 
 * �� TableModel���� ���� ������ ���� ��ü�� �ΰ� �Ǹ�, 
 * ���� ������ �ٲ� �� ��� Ŭ������ �ڵ嵵 �����ؾ� �ϴ� ������������ ���� �� �ƴ϶�,
 * �� TableModel ���� Connection�� �����ϱ� ������ ������ ���� �� �߻��Ѵ�.
 * �ϳ��� ���ø����̼��� ����Ŭ�� �δ� ������ 1�������ε� ����ϴ�!
 * �׸���, ������ ���� �� �̸� �ϳ��� ���ǿ��� �߻���Ű�� ���� DML �۾��� ���ϵ��� ���Ѵ�! -> �ٸ� ������� �νĵ�
 * 
 * ����) ����ν�� model���� connection ���� �� �ٷ� �ݰ� �ֱ� ������ connection �������� ���� ������ �߻����� X
 * 		������ ���ῡ �ʿ��� �μ��� driver, url, user, password���� �ߺ���
 * -> ��ü�� �޸𸮿� �ϳ��� �ø���!(�ν��Ͻ� 1��)
 * 
 * ��ü�� �ν��Ͻ��� �޸� heap�� 1���� ����� ���
 * 
 * */
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	// ConnectionManager�� ������ ��ȸ �ο�
	static private ConnectionManager instance;
	
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	
	// �����ڰ� �����ϴ� ��� �̿��� ������ �ƿ� ��������!
	// ����ڿ� ���� ���� ����, new�� ����!
	// if�� ������ ���� 1���� �����
	private ConnectionManager(){
		try {
			Class.forName(driver);
			// con�� �����ڿ� �־��� ������ �� ��ü�� ������ �� �� �ѹ��� �����
			con=DriverManager.getConnection(url, user, password);
			
		} 	catch (ClassNotFoundException e){
			e.printStackTrace();
		}	catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// �ν��Ͻ��� ���� ���̵� �ܺο��� �޼ҵ带 ȣ���Ͽ� 
	// �� ��ü�� �ν��Ͻ��� ������ �� �ֵ��� getter ����
	// �����ڰ� �����ϴ� �޼ҵ�
	static public ConnectionManager getInstance(){
		if(instance==null){
			// heap�� �ö� ConnectionManager�� Ŭ���� ������ instance�� ����Ű�� �ִ� ����
			// ����, heap���� ConnectionManager�� 1���� ������ �� �ִ�!
			instance=new ConnectionManager();
		}
		return instance;
	}
	
	// �� �޼ҵ带 ȣ���� ȣ���ڴ� Connection ��ü�� ��ȯ����
	public Connection getConnection(){
		return con;
	}
	// connection�� �� ����� �� �ݱ�
	public void disConnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
