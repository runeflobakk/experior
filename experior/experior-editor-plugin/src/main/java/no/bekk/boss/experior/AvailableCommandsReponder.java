package no.bekk.boss.experior;

import java.io.File;
import java.util.Collection;
import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import net.sf.json.*;



public class AvailableCommandsReponder implements Responder 
{
	/*
	@Override
	public Response makeResponse(FitNesseContext arg0, Request arg1)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	*/
	@Override
    public Response makeResponse(FitNesseContext context, Request request) 
	{
        //Henter ut metoder fra fixture basert på innholdet i wikisiden
        File wikiFile = new File(context.rootPagePath + "/" + request.getRequestLine());
        Class<?> fixtureClass = scanForFirstClassName(wikiFile);
        Collection<String> wikicommands = doFixtureMethodsToWikiCommands(fixtureClass);

        //Setter opp response
        Response ajaxResponse = new SimpleResponse();
        ajaxResponse.setContentType("text/xml"); //ajax-response er xml

      JSONObject json = new JSONObject().element("commands", wikicommands);
       
        ajaxResponse.addHeader("json", json.toString());
        return ajaxResponse;
    }

    private Collection<String> doFixtureMethodsToWikiCommands(Class<?> fixtureClass) {
        return null;
    }

    private Class<?> scanForFirstClassName(File wikiFile) {
        //scanner etter fixtureklasse !|-path.til.en.subklasse.av.DoFixture-|
        return null;
    }
}
