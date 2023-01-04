package path;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class PathTest {
	
	//Length tests
	
	/*
	 * Tests the length of the path contained in testpath.txt
	 */
	@Test
	void lengthTest() {
		try (Scanner testScanner = new Scanner(new File("testpath.txt"))) {
			Path testPath = new Path(testScanner);
			
			assertEquals(testPath.getPointCount(),5);
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath.txt'");
		}
	}
	
	/*
	 * Tests the length of the path contained in testpath2.txt
	 */
	@Test
	void lengthTest2() {
		try (Scanner testScanner = new Scanner(new File("testpath2.txt"))) {
			Path testPath = new Path(testScanner);
			
			assertEquals(testPath.getPointCount(),10);
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath2.txt'");
		}
	}
	
	//Accessor tests
	@Test
	void getXTest1() {
		try (Scanner testScanner = new Scanner(new File("testpath.txt"))) {
			Path testPath = new Path(testScanner);
			
			assertEquals(testPath.getX(3),7);
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath.txt'");
		}
	}
	@Test
	void getXTest2() {
		try (Scanner testScanner = new Scanner(new File("testpath2.txt"))) {
			Path testPath = new Path(testScanner);
			
			assertEquals(testPath.getX(7),7);
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath2.txt'");
		}
	}
	
	@Test
	void getYTest1() {
		try (Scanner testScanner = new Scanner(new File("testpath.txt"))) {
			Path testPath = new Path(testScanner);
			
			assertEquals(testPath.getY(3),8);
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath.txt'");
		}
	}
	@Test
	void getYTest2() {
		try (Scanner testScanner = new Scanner(new File("testpath2.txt"))) {
			Path testPath = new Path(testScanner);
			
			assertEquals(testPath.getY(7),7);
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath2.txt'");
		}
	}
	
	//ToString tests
	@Test
	void stringTest1() {
		try (Scanner testScanner = new Scanner(new File("testpath.txt"))) {
			Path testPath = new Path(testScanner);
			
			String testString = "5\n1 2\n3 4\n5 6\n7 8\n9 10\n";
			
			if(!testString.equals(testPath.toString()))
				fail("String test 1 failed: testpath.txt doesn't match testPath.toString()");
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath.txt'");
		}
	}
	@Test
	void stringTest2() {
		try (Scanner testScanner = new Scanner(new File("testpath2.txt"))) {
			Path testPath = new Path(testScanner);
			
			String testString = "10\n0 0\n1 1\n2 2\n3 3\n4 4\n5 5\n6 6\n7 7\n8 8\n9 9\n";
			
			if(!testString.equals(testPath.toString()))
				fail("String test 2 failed: testpath2.txt doesn't match testPath.toString()");
		}
		catch (IOException e) {
			System.err.println("Unable to load data from file 'testpath2.txt'");
		}
	}
}
