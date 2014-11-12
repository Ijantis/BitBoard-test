package test;

public class test {

	public static void main(String[] args) {

		new test();

	}

	public test() {
		
		String test = "0000000000000000000000000000000000000000000000000000000000000010";
		
		long myNumber = Long.parseLong(test,2);
		System.out.println(myNumber);
		System.out.println(myNumber << 1);
	}
}
