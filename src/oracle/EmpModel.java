/*
 * emp 테이블의 데이터를 처리하는 controller
 * */
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class EmpModel extends AbstractTableModel{
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[][]  data;	// 컬럼을 넣을 배열
	String[] column;	// 레코드를 넣을 배열
	
	public EmpModel() {
		// DB 연동을 미리 해놔야 getRow, getColumn등 값을 받아올 수 있음
		/*
		 * 1. 드라이버 로드
		 * 2. 접속
		 * 3. 쿼리문 수행
		 * 4. 접속 닫기
		 * */
		try {
			Class.forName(driver);
			System.out.println("드라이버 로드 성공");
			
			// 접속
			con=DriverManager.getConnection(url, user, password);
			if(con!=null){
				String sql="select * from emp";
				// pstmt에 의해 생성되는 rs는 커서가 자유로움!
				pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);	// 커서 업그레이드
				// 결과 집합 반환
				rs=pstmt.executeQuery();
				
				// 컬럼 반환
				ResultSetMetaData meta=rs.getMetaData();	// MetaData : 시스템 정보, 반환형 : ResultSet
				int col=meta.getColumnCount();				// 컬럼의 갯수
				
				column=new String[col];
				// 컬럼 명 채우기
				for(int i=0; i<column.length; i++){
					// MetaData에서 getColumnName는 index가 1번째부터 시작
					column[i]=meta.getColumnName(i+1);
				}
				
				int index=0;
				rs.last();				// 맨 마지막으로 커서를 보낸 뒤
			
				int row=rs.getRow();	// 레코드 번호
				
				// 총 레코드 수를 얻었으니 2차원 배열 생성
				data=new String[row][column.length];
				
				// 레코드를 2차원배열인 data에 넣기
				rs.beforeFirst();
				
				for(int i=0; i<data.length; i++){		// 행
					rs.next();	
					for(int j=0; j<data[i].length; j++){	// 열
						// 데이터베이스의 자료형과 꼭 일치하지 않아도 됨! 모르겠으면 String으로
						data[i][j]=rs.getString(column[j]);
					}
				}
			}
			else{
				System.out.println("접속 실패");
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 실패");
			e.printStackTrace();
		} catch (SQLException e) {
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
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
