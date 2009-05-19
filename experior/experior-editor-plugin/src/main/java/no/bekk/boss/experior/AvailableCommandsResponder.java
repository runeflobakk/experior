//KLASSEN SOM TAR MOT RESPONSEN
package no.bekk.boss.experior;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import net.sf.json.JSONArray;
import fitlibrary.DoFixture;
import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;

public class AvailableCommandsResponder implements Responder
{
	@Override
	public Response makeResponse(FitNesseContext context, Request request) throws Exception
	{
		try
		{            
			String content = request.getQueryString();
			String command = this.getWikiCommands(content);
			String[] commands = command.split("\n");

			SimpleResponse ajaxResponse = new SimpleResponse();

			JSONArray json = new JSONArray();

			for( String s : commands )
				json.add(s);

			ajaxResponse.addHeader("json", json.toString());            
			return ajaxResponse;
		} 
		catch(Throwable t)
		{	
			throw new RuntimeException(t);
		}
	}

	public String getWikiCommands( String content )
	{
		String fixtureClassName = new Scanner(content).findInLine("(?<=\\!\\|\\-?)[\\w|\\.]+(?=\\-?\\|)");
		if( fixtureClassName == null )
			return "";

		StringBuilder wikiCommands = new StringBuilder();
		List<Class<?>> types = new ArrayList<Class<?>>();

		try
		{
			for (Class<?> oneType = Class.forName(fixtureClassName); DoFixture.class.isAssignableFrom(oneType.getSuperclass()); oneType = oneType.getSuperclass())
			{
				types.add(oneType);
			}
		}
		catch (ClassNotFoundException e)
		{
			return "";
			//throw new RuntimeException(e);
		}

		if( types != null)
		{
			for(Class<?> type : types)
			{
				for(Method method : type.getDeclaredMethods())
				{
					wikiCommands.append(toWikiCommand(method.getName()) + "\n" );
				}
			}
		}
		return wikiCommands.toString();
	}
	
	public String toWikiCommand(String methodName)
	{
		StringBuilder builder = new StringBuilder();
		for(Character character : methodName.toCharArray())
		{
			if (isUpperCase(character))
			{
				builder.append(" " + toLowerCase(character));
			}
			else
			{
				builder.append(character);
			}
		}
		return builder.toString();
	}
}
