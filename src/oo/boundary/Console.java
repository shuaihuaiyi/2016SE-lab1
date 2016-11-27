package oo.boundary;
import oo.control.Control;

import java.util.Scanner;
public class Console
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		Control control = new Control();
		while(true)
		{
			System.out.print(">");
			if(scan.hasNextLine())
			{
				String input = scan.nextLine();
				if(input.equals("#"))
				{
					scan.close();
					return;
				}
				//else
				System.out.println(control.control(input));
			}
		}
	}
}
