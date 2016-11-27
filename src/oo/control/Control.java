package oo.control;

import oo.entity.Polynomial;
public class Control
{
	private String polynomial;
	
	public String control(String input)
	{
		/*根据多项表达式定义多项式格式：
		1.+-*符号支持零个或者多个whitespace (spaces, tabs and new lines).
		2.数字前面的*不能省略，然而字母前面的*可以省略。
		3.^幂运算后面必须是正整数前面是字母
		4.支持小数和负数运算。
		*/
		if (input.matches("^\\s*[+\\-]?\\s*(([a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?)|(\\d+(\\.\\d+)?))"
				+ "((\\s*\\*\\s*\\d+(\\.\\d+)?)|(\\s*\\*?\\s*[a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?))*"
				+ "(\\s*[+\\-]\\s*(([a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?)|(\\d+(\\.\\d+)?))"
				+ "((\\s*\\*\\s*\\d+(\\.\\d+)?)|(\\s*\\*?\\s*[a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?))*)*\\s*$")
				&& !input.matches(".*[^+\\-*\\^\\s]\\s+[^+\\-*\\^\\s].*$")
				)//处理表达式
		{
			polynomial = input;
			return polynomial;
		}
		else if (polynomial != null)
		{
			if (input.matches("!simplify[\\s\\-A-Za-z0-9=.]*$"))//处理运算命令
			{
				Polynomial p = new Polynomial(polynomial);
				return p.simplify(input);
			}
			else if (input.matches("!d/d[\\sA-Za-z]*$"))//处理求导命令
			{
				Polynomial p = new Polynomial(polynomial);
				return p.derivative(input);
			}
			else
				return "Input error,wrong type of polynomial!";
		}
		else
			return "Invalid input!";
	}
}
