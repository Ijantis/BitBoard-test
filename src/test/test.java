package test;

public class test {

	public static void main(String[] args) {

		String binaryTest = "1000000000000000000000000000000000000000000000000000000000000000";
		System.out.println(binaryTest.length());
		System.out.println(binaryTest);

		System.out.println(binaryTest.substring(2).length());
		System.out.println(binaryTest.substring(2));
		binaryTest = "1" + binaryTest.substring(2);
		System.out.println(binaryTest.length());
		System.out.println(binaryTest);
		long test = Long.parseLong(binaryTest, 2);
		System.out.println(Long.toBinaryString(test * 2).length());
		System.out.println(Long.toBinaryString(test * 2));
		System.out.println(test*2);
		System.out.println(Long.toBinaryString(Long.MAX_VALUE));
		System.out.println(Long.toBinaryString(Long.MIN_VALUE));
	}
	// Long.parseLong("1"+Binary.substring(2), 2)*2;
}
