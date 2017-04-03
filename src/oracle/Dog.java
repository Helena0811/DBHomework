// 강아지 클래스의 인스턴스를 오직 1개만 만들기
/*
 * SingleTon pattern 개발 패턴
 * -> heap 메모리 영역에 올라가는 인스턴스는 오직 하나
 * 
 * javaSE
 * javaEE	고급기술(javaSE를 포함하여 기업용 어플리케이션 제작에 사용됨)
 * 
 * Gang of four
 * 미국 개발자 4명이 개발 서적 출간(90년대 초)
 * "Design Pattern"		
 * (설계	      습관          )
 * 국적과 지역이 다르더라도 개발자들에게 공통 용어 사용 가능
 * ex)
 * - SingleTon	객체의 인스턴스를 오직 1개만 만드는 패턴
 * - Command	
 * */

package oracle;

public class Dog {
	static private Dog instance;	// 변수를 이용해서 Dog형을 사용할 수 있도록 허락
	
	// 생성자의 접근을 private으로 선언했기 때문에 같은 클래스 내인 자기 자신만 호출 가능함
	// new에 의한 생성을 막자!
	private Dog(){
		
	}
	/*
	 * public Dog(){}
	 * 생성자를 public으로 지정하면 Dog()생성 접근 가능, Dog instance의 필요성 X
	 * */
	
	// 생성자에 의해서는 접근 불가능하지만, 메소드를 통해서는 접근 가능하도록 구현
	// 인스턴스 메소드이므로 new하지 않으면 이 메소드 호출 불가능
	// 따라서 new없이 호출 가능하게 하려면 static 붙이기
	// static을 붙였더니 static->non-static 접근 불가능, 따라서 인스턴스 변수인 instance 바로 접근 불가능
	// 따라서, instance의 접근이 가능하려면 Dog instance 또한 static으로 선언
	// Dog instance와 Dog getInstance()는 모두 클래스 원본 소속
	static public Dog getInstance() {
		if(instance==null){
			// getInstance()가 호출되면 현재 instance가 null이므로 Dog가 new에 의해 heap에 올라감
			// 하지만 heap의 Dog는 stack의 레퍼런스 변수에 의해 가리키고 있는 것이 아니라, 클래스 원본이 가리키고 있음
			instance=new Dog();
		}
		return instance;
	}
}
