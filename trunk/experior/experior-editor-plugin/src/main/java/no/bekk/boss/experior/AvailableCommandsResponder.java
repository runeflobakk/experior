package no.bekk.boss.experior;

import static no.bekk.boss.experior.FitnesseMethodsExtractor.getWikiCommands;
import net.sf.json.JSONArray;
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
			String command = getWikiCommands(content);
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


}
