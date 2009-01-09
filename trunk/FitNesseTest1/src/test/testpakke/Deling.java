package test.testpakke;
import javax.swing.*;



public class Deling 
{
	public static int deling( int tall, int divisor )
	{
		try
		{
			return tall / divisor;
			
		}
		catch( NumberFormatException nfe )
		{
			return 0;
		}
		
		
	}
	
}
