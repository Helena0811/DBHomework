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
 * */
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class EmpModelUpgrade extends AbstractTableModel{	
	//ConnectionManager manager;
	Connection con;				// AppMain�� �����ϰ� �ֱ� ������ ������ ����ϱ�
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[][]  data;	// �÷��� ���� �迭
	String[] column;	// ���ڵ带 ���� �迭
	
	public EmpModelUpgrade(Connection con) {
		this.con=con;
		// DB ������ �̸� �س��� getRow, getColumn�� ���� �޾ƿ� �� ����
		/*
		 * 1. ����̹� �ε�
		 * 2. ����
		 * 3. ������ ����
		 * 4. ���� �ݱ�
		 * */
		try {
			// �ν��Ͻ� 1���� �ö�
			// ConnectionManager�� getInstance()�� �ν��Ͻ� ��ȯ
			// manager=ConnectionManager.getInstance(); 	�ʿ�X
			
			System.out.println("����̹� �ε� ����");
			
			// ����
			// con�� �ϳ��� ������ �ְ� �����ǹǷ�, windowâ�� ���� �� �����
			// con=manager.getConnection();					�ʿ�X
			
			if(con!=null){
				String sql="select * from emp";
				// pstmt�� ���� �����Ǵ� rs�� Ŀ���� �����ο�!
				pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);	// Ŀ�� ���׷��̵�
				// ��� ���� ��ȯ
				rs=pstmt.executeQuery();
				
				// �÷� ��ȯ
				ResultSetMetaData meta=rs.getMetaData();	// MetaData : �ý��� ����, ��ȯ�� : ResultSet
				int col=meta.getColumnCount();				// �÷��� ����
				
				column=new String[col];
				
				// �÷� �� ä���
				for(int i=0; i<column.length; i++){
					// MetaData���� getColumnName�� index�� 1��°���� ����
					column[i]=meta.getColumnName(i+1);
				}
				
				rs.last();				// �� ���������� Ŀ���� ���� ��
			
				int row=rs.getRow();	// ���ڵ� ��ȣ
				
				// �� ���ڵ� ���� ������� 2���� �迭 ����
				data=new String[row][column.length];
				
				// ���ڵ带 2�����迭�� data�� �ֱ�
				rs.beforeFirst();
				
				for(int i=0; i<data.length; i++){		// ��
					rs.next();	
					for(int j=0; j<data[i].length; j++){	// ��
						// �����ͺ��̽��� �ڷ����� �� ��ġ���� �ʾƵ� ��! �𸣰����� String����
						data[i][j]=rs.getString(column[j]);
					}
				}
			}
			else{
				System.out.println("���� ����");
			}
			
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// windowâ�� ����Ǹ� ���� ����ǹǷ� �ٷ� ������ �������� ����
			/*
			if(this.con!=null){
				try {
					this.con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			*/
		}
		
	}
	
	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return column.length;
	}
	
	public String getColumnName(int index) {
		return column[index];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	
}
