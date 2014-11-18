package board;

public class test {

	public static void main(String[] args) {

		new test();

	}

	public test() {

		String temp = "1000000000000000000000000000000000000000000000000000000000000000";
		System.out.println("bits : " + temp.length());

		long myNumber = Long.parseLong(temp.substring(0, temp.length()-1), 2) * 2;
		System.out.println(Long.toBinaryString(myNumber).length());
	}

	public static void testMethod(long bitboard) {

	}
}
