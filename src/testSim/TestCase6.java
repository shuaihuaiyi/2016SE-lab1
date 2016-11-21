package testSim;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import calculator.Calculator;
import calculator.Calculator.Monomial;

public class TestCase6 {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void test6() {
		ArrayList <Monomial> exp;
		exp = Calculator.expression("x+y");
		Calculator.simplify(exp,"!simplify x=a");
		String result = outContent.toString();
		assertEquals("Simplify parameter error format,should be x=3.3 or likely\r\n", result);
	}
}
