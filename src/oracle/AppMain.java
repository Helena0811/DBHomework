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
	Connection con;	// 모든 객체간 공유하기 위해, 어플리케이션이 가동될 때 올리기
	JTable table;
	JScrollPane scroll;
	JPanel p_west, p_center;
	Choice choice;
	String[][] item={
		{"▼ 테이블을 선택하세요", ""},
		{"사원 테이블 선택", "emp"},
		{"부서 테이블 선택", "dept"}
	};
	
	// 아무 형태 없는 테이블이나 emp, dept를 배열에 저장해서 접근하기 쉽게 구현하자!
	TableModel[] model=new TableModel[item.length];
	
	public AppMain() {
		// 디자인과 로직을 분리시키기 위해 중간자(Controller)의 존재가 필요
		// JTable에서는 이 컨트롤러의 역할을 TableModel이 해줌!
		// TableModel을 사용할 경우, JTable은 자신이 보여줘야 할 데이터를 TableModel로부터 정보를 얻어와 출력함
		// getRowCount(), getColumnCount(), getValueAt()
		
		// 테이블이 고정되어 있음 -> 변경 대상
		//table=new JTable(3,2);
		manager=ConnectionManager.getInstance();
		con=manager.getConnection();
		
		table=new JTable();		// table=new JTable(모델); -> 하지만 모델은 choice에 의해 사용자가 선택, table이 생성되는 것보다 늦게 설정됨
		scroll=new JScrollPane(table);
		p_west=new JPanel();
		p_center=new JPanel();
		choice=new Choice();
		
		
		for(int i=0; i<item.length; i++){
			choice.add(item[i][0]);
		}
		
		// 테이블 모델들을 올려놓자
		model[0]=new DefaultTableModel();
		model[1]=new EmpModelUpgrade(con);
		model[2]=new DeptModelUpgrade(con);
		
		// 테이블에 데이터 넣기
		// 현재 방식은 변경 대상이 되므로 유지보수성이 떨어짐 -> 디자인과 로직이 섞여있음
		// 애초에 java는 컴포넌트 기반이므로 적절하지 X, 로직은 분리시키고 디자인만 남기자!
		/*
		table.setValueAt("사과", 0, 0);
		table.setValueAt("배", 0, 1);
		table.setValueAt("장미", 1, 0);
		table.setValueAt("튤립", 1, 1);
		table.setValueAt("잉어", 2, 0);
		table.setValueAt("붕어", 2, 1);
		*/
		
		// choice 구성 - 조건문을 사용하지 않기 위해 2차원 배열로 구현하기
		/*
		choice.add("테이블 선택");
		choice.add("사원 테이블");	// select * from emp;
		choice.add("부서 테이블");	// select * from dept;
		*/
		// 윈도우창 닫을 때 오라클 접속 끊기
		this.addWindowListener(new WindowAdapter() {
			public void winDowClosing(WindowEvent e){
				// 커넥션 닫기
				manager.disConnect(con);
				
				// 프로그램 종료
				System.exit(0);
			}
		});
	
		
		p_west.add(choice);	// west 영역 패널에 choice 부착
		p_center.add(scroll);
		
		add(p_west,BorderLayout.WEST);
		add(p_center);
		
		pack();
		
		// choice와 ItemListener 연결
		choice.addItemListener(this);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// 해당되는 테이블 출력
	public void showData(int index){
		System.out.println("당신이 보게 될 테이블은 "+item[index][1]+"입니다.");
		
		// 해당되는 테이블 모델 사용
		// emp일 경우 EmpModel, dept일 경우 DeptModel 사용
		// 둘다 아닌 경우, DefaultTableModel사용
		table.setModel(model[index]);
	}
	
	// choice 이벤트 구현
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice) e.getSource();
		int index=ch.getSelectedIndex();
		showData(index);
	}
	
	public static void main(String[] args) {
		new AppMain();
	}
}
