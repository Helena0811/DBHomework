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
 * 
 * 각 TableModel마다 접속 정보와 접속 객체를 두게 되면, 
 * 접속 정보가 바뀔 떄 모든 클래스의 코드도 수정해야 하는 유지보수상의 문제 뿐 아니라,
 * 각 TableModel 마다 Connection을 생성하기 때문에 접속이 여러 개 발생한다.
 * 하나의 어플리케이션이 오라클과 맺는 접속은 1개만으로도 충분하다!
 * 그리고, 접속이 여러 개 이면 하나의 세션에서 발생시키는 각종 DML 작업이 통일되지 못한다! -> 다른 사람으로 인식됨
 * 
 * 수정) 현재로써는 model에서 connection 수행 후 바로 닫고 있기 때문에 connection 여러개로 인한 문제는 발생하지 X
 * 		하지만 연결에 필요한 인수인 driver, url, user, password등이 중복됨
 * -> 객체를 메모리에 하나만 올리자!(인스턴스 1개)
 * 
 * 객체의 인스턴스를 메모리 heap에 1개만 만드는 방법
 * 
 * */
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	// ConnectionManager을 생성할 기회 부여
	static private ConnectionManager instance;
	
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	
	// 개발자가 제공하는 방법 이외의 접근은 아예 차단하자!
	// 사용자에 의한 임의 생성, new를 막자!
	// if문 때문에 최초 1번만 수행됨
	private ConnectionManager(){
		try {
			Class.forName(driver);
			// con을 생성자에 넣었기 때문에 이 객체가 생성될 때 단 한번만 선언되
			con=DriverManager.getConnection(url, user, password);
			
		} 	catch (ClassNotFoundException e){
			e.printStackTrace();
		}	catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 인스턴스의 생성 없이도 외부에서 메소드를 호출하여 
	// 이 객체의 인스턴스를 가져갈 수 있도록 getter 제공
	// 개발자가 제공하는 메소드
	static public ConnectionManager getInstance(){
		if(instance==null){
			// heap에 올라간 ConnectionManager는 클래스 원본의 instance가 가리키고 있는 상태
			// 따라서, heap에는 ConnectionManager가 1개만 존재할 수 있다!
			instance=new ConnectionManager();
		}
		return instance;
	}
	
	// 이 메소드를 호출한 호출자는 Connection 객체를 반환받음
	public Connection getConnection(){
		return con;
	}
	// connection을 다 사용한 후 닫기
	public void disConnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
