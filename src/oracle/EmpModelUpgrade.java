/*
 * 모든 테이블의 데이터를 처리하는 controller
 * 
 * model[0]=new DefaultTableModel();
   model[1]=new EmpModel();
   model[2]=new DeptModel();
 * 이와 같이 테이블 마다 따로 model을 만들어 메모리에 올리게 된다면,
 * model마다 따로 접속을 하게 되는데 서로 다른 접속기를 이용해서 들어오게 됨 -> 어느 한곳에서 수정이 발생하면 불안정
 * 애초에 데이터베이스 접속 시 connection 1개로 모든 테이블 접근 가능
 * 
 * -> Connection의 분리
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
	Connection con;				// AppMain이 보유하고 있기 때문에 가져와 사용하기
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[][]  data;	// 컬럼을 넣을 배열
	String[] column;	// 레코드를 넣을 배열
	
	public EmpModelUpgrade(Connection con) {
		this.con=con;
		// DB 연동을 미리 해놔야 getRow, getColumn등 값을 받아올 수 있음
		/*
		 * 1. 드라이버 로드
		 * 2. 접속
		 * 3. 쿼리문 수행
		 * 4. 접속 닫기
		 * */
		try {
			// 인스턴스 1개만 올라감
			// ConnectionManager의 getInstance()로 인스턴스 반환
			// manager=ConnectionManager.getInstance(); 	필요X
			
			System.out.println("드라이버 로드 성공");
			
			// 접속
			// con은 하나만 가지고 있고 공유되므로, window창이 닫힐 때 종료됨
			// con=manager.getConnection();					필요X
			
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
			// window창이 종료되면 같이 종료되므로 바로 연결을 해제하지 않음
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
