package calculator;

import java.util.*;

public class Calculator
{
	//单项式类
	private static class Monomial
	{
		//表达式中的元素
		Double coefficient;//系数
		HashMap<String, Integer> vars;//变量，保存为变量名->次数的映射

		Monomial(String monomial)
		{
			coefficient = 1.0;
			vars = new HashMap<String, Integer>();
			String var;//变量名
			int index;//指数

			if(monomial.charAt(0) == '-')//处理减号
			{
				coefficient = -1.0;
				monomial = monomial.substring(1);
			}
			else if(monomial.charAt(0) == '+')//处理加号
			{
				coefficient = 1.0;
				monomial = monomial.substring(1);
			}
			String[] factors = monomial.split("\\*|(?<=[0-9])(?=[A-Za-z])|(?<=[A-Za-z])(?=[0-9])");
			/*正常来讲，factor只能有以下几种情况：
			 *1、变量，如x，boo
			 *2、数字，如1, 12，15.2
			 *3、幂式，如x^3，value^5
			 */
			for (String factor : factors)
			{
				if(factor.matches("^[A-Za-z]+$"))//变量
					vars.put(factor,((vars.containsKey(factor) ? (vars.get(factor))+1 : 1)));
				else if(factor.matches("^[0-9]+(\\.[0-9]+)?$"))//数字
					coefficient *= Double.valueOf(factor);
				else if(factor.matches("^[A-Za-z]+\\^[0-9]+$"))//幂式
				{
					String[] pair = factor.split("(?<=[A-Za-z]+)\\^(?=[0-9]*[1-9]+[0-9]*)");
					var = pair[0];
					index = Integer.valueOf(pair[1]);
					vars.put(var,((vars.containsKey(var) ? (vars.get(var))+index : index)));
				}

			}
		}
		Monomial()
		{
			coefficient = 1.0;
			vars = new HashMap<String, Integer>();
		}

	}
	
    //合并同类项并进行输出
    static void print(ArrayList <Monomial> result)
    {
        //合并同类项
        HashMap <HashMap<String, Integer>,Monomial> map = new HashMap <HashMap<String, Integer>,Monomial>();
        for(int i=0,j=0; j<result.size(); i++,j++)
        {
            Monomial monomial = result.get(j);
            if(map.containsKey(monomial.vars))
            {
                map.get(monomial.vars).coefficient += monomial.coefficient;
                result.remove(i);
                i--;
            }
            else
                map.put(monomial.vars, monomial);
        }
        //这个部分用来删除系数为0的项
        for(int i=0; i<result.size(); i++)
        {
            if(result.get(i).coefficient.equals(0.0))
            {
                result.remove(i);
                i--;
            }
        }
        //将结果输出
        if(result.size() == 0)
        	System.out.print("0");
        for(int i=0; i<result.size(); i++)
        {
            Monomial monomial = result.get(i);
            if(monomial.coefficient.equals(-1.0))
            {
            	System.out.print('-');
            	int j = 0;
            	for(String var : monomial.vars.keySet())
                {
                    for(int k=0; k<monomial.vars.get(var);k++)
                    {	
                    	if((k == 0) && (j == 0))
        					System.out.print(var);
        				else
        					System.out.print("*" + var);
                    }
                    j++;
                }
            }
            else if(monomial.coefficient.equals(1.0))
            {
            	if(i != 0)
            		System.out.print('+');
            	int j = 0;
            	for(String var : monomial.vars.keySet())
                {
                    for(int k=0; k<monomial.vars.get(var);k++)
                    {	
                    	if((k == 0) && (j == 0))
        					System.out.print(var);
        				else
        					System.out.print("*" + var);
                    }
                    j++;
                }
            }
            else
            {
            	if(monomial.coefficient < 0)
	                System.out.print(monomial.coefficient);
	            else if(monomial.coefficient > 0)
	            {	
	            	if(i != 0)
	            		System.out.print("+" + monomial.coefficient);
	            	else
	                	System.out.print(monomial.coefficient);
	            }
            	for(String var : monomial.vars.keySet())
                {
                    for(int i1=0; i1<monomial.vars.get(var);i1++)
                        System.out.print("*" + var);
                }
            }
            
        }
        System.out.print('\n');
    }

	//将字符串表达式转换成自定义数据类型
	static ArrayList<Monomial> expression(String input)
	{
		ArrayList <Monomial> exp = new ArrayList <Monomial> ();//表达式

		String fixedInput = input.replaceAll("(?<=[+\\-*^])\\s+(?=[+\\-*^])","");
		String[] monomials = fixedInput.split("(?=\\+|-)");
		for(String monomial : monomials)
			exp.add(new Monomial(monomial));
		return exp;
	}
	//按照输入的值对表达式进行计算
	static void simplify(ArrayList <Monomial> exp, String input)
	{
		ArrayList <Monomial> result = new ArrayList <Monomial>();
		HashMap <String,Double> solves = new HashMap <String,Double>();

		//得到单个赋值表达式，进行处理，得到赋值表
		String[] assigns = input.substring(9).split("\\s+");
		for(String assign : assigns)
		{
			if(assign.length() == 0)
				continue;
			//else
			String[] temp = assign.split("=");
			if(temp.length == 1)
			{
				System.out.println("Simplify parameter is loss!");
				return;
			}
			else
			{
				if(temp[0].matches("^[A-Za-z]+$") && temp[1].matches("^-?\\d+(\\.\\d+)?$"))
				{
					if(!(solves.containsKey(temp[0])))
						solves.put(temp[0], (Double.valueOf(temp[1])));
					else
					{
						System.out.println("Simplify parameter is repeated!");
						return;
					}
				}
				else
				{
					System.out.println("Simplify parameter error format,should be x=3.3 or likely");
					return;
				}
			}
		}
		//对乘法进行运算
		for(int i=0; i<exp.size(); i++)
		{
			Monomial monomial = exp.get(i);//处理一个单项式
			result.add(new Monomial());
			result.get(i).coefficient = monomial.coefficient;//设置系数
			for(String var : monomial.vars.keySet())//逐个规定变量
			{
				if(solves.containsKey(var))
					result.get(i).coefficient *=  Math.pow(solves.get(var), exp.get(i).vars.get(var));
				else
					result.get(i).vars.put(var, monomial.vars.get(var));
			}
		}
		print(result);
	}
	//对表达式求导
	static void derivative(ArrayList <Monomial> exp, String input)
	{
		ArrayList <Monomial> result = new ArrayList <Monomial>();
		String assign = input.substring(4).replaceAll("\\s+","");
		if(!assign.matches("^[A-Za-z]+$"))
		{
			System.out.println("Error! invaild input!");
			return;
		}
		int count= 0;//用来计算表达式中是否有这个变量
		for(int i=0,j=0; j<exp.size(); i++,j++)
		{
			Monomial monomial = exp.get(j);//处理一个单项式
			result.add(new Monomial());
			result.get(i).coefficient = monomial.coefficient;//设置系数
			if(!monomial.vars.containsKey(assign))
			{
				result.remove(i);
				count += 1;
				i--;
				continue;
			}
			if(count==exp.size())
			{
				System.out.println("Error! no variable!");
				return;
			}
			for(String var: monomial.vars.keySet())//逐个规定变量
			{
				if((assign.equals(var)) && (exp.get(j).vars.get(var)>1))
				{
					result.get(i).coefficient *= exp.get(j).vars.get(var);
					result.get(i).vars.put(var, monomial.vars.get(var)-1);
				}
				else if(!assign.equals(var))
					result.get(i).vars.put(var, monomial.vars.get(var));
			}
		}
		print(result);
	}

	public static void main(String[] args)
	{
		ArrayList <Monomial> exp;
		Scanner scan = new Scanner(System.in);
		String polynomia =null;
		while(true)
		{
			System.out.print(">");
			if(scan.hasNextLine())
			{
				String input = scan.nextLine();
				/*根据多项表达式定义多项式格式：
				 1.+-*符号支持零个或者多个whitespace (spaces, tabs and new lines).
				 2.数字前面的*不能省略，然而字母前面的*可以省略。
				 3.^幂运算后面必须是正整数前面是字母
				 4.支持小数和负数运算。
				 */
				if(input.matches("^\\s*[+-]?\\s*(([a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?)|(\\d+(\\.\\d+)?))"
						+ "((\\s*\\*\\s*\\d+(\\.\\d+)?)|(\\s*\\*?\\s*[a-zA-Z]+(\\^\\d*[1-9]+\\d*)?))*(\\s*[+-]{1}\\s*(([a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?)|(\\d+(\\.\\d+)?))"
						+ "((\\s*\\*\\s*\\d+(\\.\\d+)?)|(\\s*\\*?\\s*[a-zA-Z]+(\\s*\\^\\s*\\d*[1-9]+\\d*)?))*)*$")
				  )//处理表达式
				{
					polynomia=input;
					System.out.println(polynomia);
				}
				else if(polynomia!=null)
				{
					if(input.matches("!simplify[\\s\\-A-Za-z0-9=.]*$") )//处理运算命令
					{
						
						exp = expression(polynomia);
						simplify(exp,input);
					}
					else if(input.matches("!d\\/d[\\sA-Za-z]*$"))//处理求导命令
					{
						exp = expression(polynomia);
						derivative(exp,input);
					}
					else
						System.out.println("Input error,wrong type of polynomia!");
				}
				else
					System.out.println("Invalid input!");
			}

		}
	}
}
