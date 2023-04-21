
public class test1 {

	public static void main(String[] args) {
		testv v = new testv();
		test2 t2 = new test2(v);
		test3 t3 = new test3(v);

		new Thread(t2).start();
		new Thread(t3).start();

	}

}
