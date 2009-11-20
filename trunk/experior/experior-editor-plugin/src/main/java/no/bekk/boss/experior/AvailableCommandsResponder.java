package no.bekk.boss.experior;

import static no.bekk.boss.experior.FitnesseMethodsExtractor.getWikiCommands;
import net.sf.json.JSONArray;
import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;

/**
 * Responder which retrieves the available commands which the test may invoke.
 * These requests are typiucally performed asynchronized using AJAX.
 * (The response contains JSON)
 */
public class AvailableCommandsResponder implements Responder {

    /**
     * Creates a JSON-object with method-names from the specified DoFixture in
     * the wiki page.
     */
    @Override
    public Response makeResponse(FitNesseContext context, Request request) {
        String content = request.getQueryString();
        String command = getWikiCommands(content);
        String[] commands = command.split("\n");

        SimpleResponse ajaxResponse = new SimpleResponse();

        JSONArray json = new JSONArray();
        for (String s : commands) {
            json.add(s);
        }

        ajaxResponse.addHeader("json", json.toString());
        return ajaxResponse;
    }

}
