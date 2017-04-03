// ������ Ŭ������ �ν��Ͻ��� ���� 1���� �����
/*
 * SingleTon pattern ���� ����
 * -> heap �޸� ������ �ö󰡴� �ν��Ͻ��� ���� �ϳ�
 * 
 * javaSE
 * javaEE	��ޱ��(javaSE�� �����Ͽ� ����� ���ø����̼� ���ۿ� ����)
 * 
 * Gang of four
 * �̱� ������ 4���� ���� ���� �Ⱓ(90��� ��)
 * "Design Pattern"		
 * (����	      ����          )
 * ������ ������ �ٸ����� �����ڵ鿡�� ���� ��� ��� ����
 * ex)
 * - SingleTon	��ü�� �ν��Ͻ��� ���� 1���� ����� ����
 * - Command	
 * */

package oracle;

public class Dog {
	static private Dog instance;	// ������ �̿��ؼ� Dog���� ����� �� �ֵ��� ���
	
	// �������� ������ private���� �����߱� ������ ���� Ŭ���� ���� �ڱ� �ڽŸ� ȣ�� ������
	// new�� ���� ������ ����!
	private Dog(){
		
	}
	/*
	 * public Dog(){}
	 * �����ڸ� public���� �����ϸ� Dog()���� ���� ����, Dog instance�� �ʿ伺 X
	 * */
	
	// �����ڿ� ���ؼ��� ���� �Ұ���������, �޼ҵ带 ���ؼ��� ���� �����ϵ��� ����
	// �ν��Ͻ� �޼ҵ��̹Ƿ� new���� ������ �� �޼ҵ� ȣ�� �Ұ���
	// ���� new���� ȣ�� �����ϰ� �Ϸ��� static ���̱�
	// static�� �ٿ����� static->non-static ���� �Ұ���, ���� �ν��Ͻ� ������ instance �ٷ� ���� �Ұ���
	// ����, instance�� ������ �����Ϸ��� Dog instance ���� static���� ����
	// Dog instance�� Dog getInstance()�� ��� Ŭ���� ���� �Ҽ�
	static public Dog getInstance() {
		if(instance==null){
			// getInstance()�� ȣ��Ǹ� ���� instance�� null�̹Ƿ� Dog�� new�� ���� heap�� �ö�
			// ������ heap�� Dog�� stack�� ���۷��� ������ ���� ����Ű�� �ִ� ���� �ƴ϶�, Ŭ���� ������ ����Ű�� ����
			instance=new Dog();
		}
		return instance;
	}
}
