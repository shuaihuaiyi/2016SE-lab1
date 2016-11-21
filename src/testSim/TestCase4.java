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

public class TestCase4 {

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
	public void test4() {
		ArrayList <Monomial> exp;
		exp = Calculator.expression("x+y");
		Calculator.simplify(exp,"!simplify a=1");
		String result = outContent.toString();
		assertEquals("No such variable(s) in the expression!!\r\n", result);
	}
}
