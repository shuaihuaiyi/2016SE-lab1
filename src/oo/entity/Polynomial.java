package oo.entity;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 多项式
 * @author ZZY
 */
public class Polynomial
{
	private ArrayList <Monomial> polynomial;
	
	//构造方法，将字符串表达式转换成多项式
	public Polynomial(String input)
	{
		String fixedInput = input.replaceAll("\\s+","");
		String[] monomials = fixedInput.split("(?=\\+|-)");
		polynomial = new ArrayList<>();
		for(String monomial : monomials)
			polynomial.add(new Monomial(monomial));
	}
	public Polynomial()
	{
		polynomial = null;
	}
	
	//按照输入的值对表达式进行计算
	public String simplify(String input)
	{
		ArrayList <Monomial> result = new ArrayList<>();
		HashMap <String,Double> solves = new HashMap<>();
		
		//对传入参数进行控制
		if(polynomial == null)
			return "No expression specified!";
		if(!input.matches("!simplify[\\s\\-A-Za-z0-9=.]*$"))
			return "Invalid input, should be start with '!simplify'";
		//得到单个赋值表达式，进行处理，得到赋值表
		String[] assigns = input.substring(9).split("\\s+");
		for(String assign : assigns)
		{
			if(assign.length() == 0)
				continue;
			//else
			String[] temp = assign.split("=");
			if(temp.length == 1)
				return "Simplify format is wrong!";
			else
			{
				if(temp[0].matches("^[A-Za-z]+$") && temp[1].matches("^-?\\d+(\\.\\d+)?$"))
				{
					Boolean flag = false;
					for(Monomial monomial : polynomial)
					{
						if(monomial.getVars().containsKey(temp[0]))
							flag = true;
					}
					if(!flag)
						return "No such variable(s) in the expression!!";
					if(!(solves.containsKey(temp[0])))
						solves.put(temp[0], (Double.valueOf(temp[1])));
					else
						return "Simplify parameter is repeated!";
				}
				else
					return "Simplify parameter error format,should be x=3.3 or likely";
			}
		}
		//对乘法进行运算
		for(int i=0; i<polynomial.size(); i++)
		{
			Monomial monomial = polynomial.get(i);//处理一个单项式
			result.add(new Monomial());
			result.get(i).setCoefficient(monomial.getCoefficient());//设置系数
			for(String var : monomial.getVars().keySet())//逐个规定变量
			{
				if(solves.containsKey(var))
					result.get(i).setCoefficient(result.get(i).getCoefficient() * Math.pow(solves.get(var), polynomial.get(i).getVars().get(var)));
				else
					result.get(i).getVars().put(var, monomial.getVars().get(var));
			}
		}
		return print(result);
	}
	//对表达式求导
	public String derivative(String input)
	{
		ArrayList <Monomial> result = new ArrayList<>();
		String assign = input.substring(4).replaceAll("\\s+","");
		if(!assign.matches("^[A-Za-z]+$"))
			return "Error! invalid input!";
		int count= 0;//用来计算表达式中是否有这个变量
		if(polynomial==null)
			return "No expression specified!";
		for(int i=0,j=0; j<polynomial.size(); i++,j++)
		{
			Monomial monomial = polynomial.get(j);//处理一个单项式
			result.add(new Monomial());
			result.get(i).setCoefficient(monomial.getCoefficient());//设置系数
			if(!monomial.getVars().containsKey(assign))
			{
				result.remove(i);
				count += 1;
				if(count==polynomial.size())
					return "Error! no variable!";
				i--;
				continue;
			}
			for(String var: monomial.getVars().keySet())//逐个规定变量
			{
				if((assign.equals(var)) && (polynomial.get(j).getVars().get(var)>1))
				{
					result.get(i).setCoefficient(result.get(i).getCoefficient() * polynomial.get(j).getVars().get(var));
					result.get(i).getVars().put(var, monomial.getVars().get(var)-1);
				}
				else if(!assign.equals(var))
					result.get(i).getVars().put(var, monomial.getVars().get(var));
			}
		}
		return print(result);
	}
	//合并同类项并进行输出
	private String print(ArrayList<Monomial> result)
	{
		String r="";
		//合并同类项
		HashMap <HashMap<String, Integer>,Monomial> map = new HashMap<>();
		for(int i=0; i<result.size(); i++)
		{
			Monomial monomial = result.get(i);
			if(map.containsKey(monomial.getVars()))
			{
				map.get(monomial.getVars()).setCoefficient(map.get(monomial.getVars()).getCoefficient() + monomial.getCoefficient());
				result.remove(i);
				i--;
			}
			else
				map.put(monomial.getVars(), monomial);
		}
		//这个部分用来删除系数为0的项
		for(int i=0; i<result.size(); i++)
		{
			if(result.get(i).getCoefficient().equals(0.0))
			{
				result.remove(i);
				i--;
			}
		}
		//将结果输出
		if(result.size() == 0)
			r+=("0");
		for(int i=0; i<result.size(); i++)
		{
			Monomial monomial = result.get(i);
			
			if(monomial.getCoefficient().equals(-1.0))
			{
				r+=('-');
				int j = 0;
				if(monomial.getVars().isEmpty())
					r+=("1.0");
				for(String var : monomial.getVars().keySet())
				{
					if(monomial.getVars().get(var) <= 2)
					{
						for(int k = 0; k< monomial.getVars().get(var); k++)
						{
							if((k == 0) && (j == 0))
								r+=(var);
							else
								r+=("*" + var);
						}
					}
					else
					{
						if(j == 0)
							r+=(var + "^" + monomial.getVars().get(var));
						else
							r+=("*" + var + "^" + monomial.getVars().get(var));
					}
					j++;
				}
			}
			else if(monomial.getCoefficient().equals(1.0))
			{
				if(i != 0)
					r+=('+');
				int j = 0;
				if(monomial.getVars().isEmpty())
					r+=("1.0");
				for(String var : monomial.getVars().keySet())
				{
					if(monomial.getVars().get(var) <= 2)
					{
						for(int k = 0; k< monomial.getVars().get(var); k++)
						{
							if((k == 0) && (j == 0))
								r+=(var);
							else
								r+=("*" + var);
						}
					}
					else
					{
						if(j == 0)
							r+=(var + "^" + monomial.getVars().get(var));
						else
							r+=("*" + var + "^" + monomial.getVars().get(var));
					}
					j++;
				}
			}
			else
			{
				if(monomial.getCoefficient() < 0)
					r+=(monomial.getCoefficient());
				else if(monomial.getCoefficient() > 0)
				{
					if(i != 0)
						r+=("+" + monomial.getCoefficient());
					else
						r+=(monomial.getCoefficient());
				}
				for(String var : monomial.getVars().keySet())
				{
					if(monomial.getVars().get(var) <= 2)
					{
						for(int k = 0; k< monomial.getVars().get(var); k++)
							r+=("*" + var);
					}
					else
						r+=("*" + var + "^" + monomial.getVars().get(var));
				}
			}
			
		}
		return r;
	}
	public ArrayList<Monomial> getPolynomial()
	{
		return polynomial;
	}
	public void setPolynomial(ArrayList<Monomial> polynomial)
	{
		this.polynomial = polynomial;
	}
}
