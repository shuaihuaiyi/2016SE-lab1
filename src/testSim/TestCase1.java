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

public class TestCase1 {

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
	public void test1() {
		ArrayList <Monomial> exp;
		exp = Calculator.expression("x+x+y");
		Calculator.simplify(exp,"!simplify x=1 y=2");
		String result = outContent.toString();
		assertEquals("4.0\n", result);
	}
}
