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

/**
 * Class with Responder which responds to requests with URL that ends on ?commands.
 * These requests is performed by and XMLHttpRequest-object.
 * The comments used in this class is not intended to follow the JavaDoc-standard. 
 *
 */
public class AvailableCommandsResponder implements Responder
{
	/**
	 * Creates a JSON-object with method-names. The class-name is resolved from
	 * the query-string.
	 */
	@Override
	public Response makeResponse(FitNesseContext context, Request request) throws Exception {
		try {            
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
		catch(Throwable t) {	
			throw new RuntimeException(t);
		}
	}

	/**
	 * Scans through the variable content to find a valid class-name. If a valid class-
	 * name is found, it gets all the declared methods in this class. It also gets the
	 * methods in the super-classes up to the class DoFixture.
	 */
	public String getWikiCommands(String content) {
		Scanner s = new Scanner(content);
		String fixtureClassName = s.findInLine("(?<=\\!\\|\\-?)[\\w|\\.]+(?=\\-?\\|)");
		
		if( fixtureClassName == null )
			return "";

		StringBuilder wikiCommands = new StringBuilder();
		List<Class<?>> types = new ArrayList<Class<?>>();

		try {
			for (Class<?> oneType = Class.forName(fixtureClassName); 
					DoFixture.class.isAssignableFrom(oneType.getSuperclass()); 
					oneType = oneType.getSuperclass())
			{
				types.add(oneType);
			}
		}
		catch (ClassNotFoundException e) {
			return "";
		}

		if( types != null) 	{
			for(Class<?> type : types) {
				for(Method method : type.getDeclaredMethods()) {
					wikiCommands.append(toWikiCommand(method.getName()) + "\n" );
				}
			}
		}
		return wikiCommands.toString();
	}
	
	/**
	 * Creates a wikicommand valid in FitNesse from a method-name.
	 * @param methodName
	 */
	public String toWikiCommand(String methodName) {
		StringBuilder builder = new StringBuilder();
		for(Character character : methodName.toCharArray()) {
			if (isUpperCase(character)) {
				builder.append(" " + toLowerCase(character));
			}
			else {
				builder.append(character);
			}
		}
		return builder.toString();
	}
}
