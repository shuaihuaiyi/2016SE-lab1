package oo.entity;
import java.util.HashMap;
/**
 * 单项式
 * @author ZZY
 */
class Monomial
{
	//表达式中的元素
	private Double coefficient;//系数
	private HashMap<String, Integer> vars;//变量，保存为变量名->次数的映射
	
	public Monomial(String monomial)
	{
		coefficient = 1.0;
		vars = new HashMap<>();
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
	public Monomial()
	{
		coefficient = 1.0;
		vars = new HashMap<>();
	}
	
	public Double getCoefficient()
	{
		return coefficient;
	}
	public void setCoefficient(Double coefficient)
	{
		this.coefficient = coefficient;
	}
	public HashMap<String, Integer> getVars()
	{
		return vars;
	}
	public void setVars(HashMap<String, Integer> vars)
	{
		this.vars = vars;
	}
}
