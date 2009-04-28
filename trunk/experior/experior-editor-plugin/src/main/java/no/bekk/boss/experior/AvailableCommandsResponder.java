//KLASSEN SOM TAR MOT RESPONSEN
package no.bekk.boss.experior;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sun.xml.internal.txw2.Document;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fitlibrary.DoFixture;
import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.MockingPageCrawler;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPagePath;

public class AvailableCommandsResponder implements Responder
{
	private ExperiorResponder responder = new ExperiorResponder();
	
	
    @Override
    public Response makeResponse(FitNesseContext context, Request request)throws Exception
    {
        try
        {
            
            //String fixtureClassName = (String)request.getInput("fixture");
            String content = request.getQueryString();
            String command = this.getWikiCommands(content);
            String[] commands = command.split("\n");
                        
            SimpleResponse ajaxResponse = new SimpleResponse();
            
            //String json2 ="javascript";
            //String[] jason3 = {"javascript","JALLA"};
            JSONArray json = new JSONArray();
            //JSONObject json = (JSONObject) new JSONObject();
            for( String s : commands )
            	json.add(s);
            //json.put("Kommando", commands);
            //JSONArray jarray = new JSONArray().put("Commands", jason3);
           // ajaxResponse.setContentType("text/xml"); //ajax-response er xml
            
           
    		//String resource = request.getResource();
    		//WikiPagePath path = PathParser.parse(resource);
    		/*
    		PageCrawler crawler = context.root.getPageCrawler();
    		if (!crawler.pageExists(root, path)) {
    			crawler.setDeadEndStrategy(new MockingPageCrawler());
    			page = crawler.getPage(root, path);
    		} else
    			page = crawler.getPage(root, path);
			
    		pageData = page.getData();
    		content = createPageContent();

    		String html = doMakeHtml(resource, context);

    		response.setContent(html);
    		response.setMaxAge(0);

    		return response;
    		*/
            
    		//ajaxResponse.setContent("");
    		//ajaxResponse.setMaxAge(0);
            ajaxResponse.addHeader("json", json.toString());            
            return ajaxResponse;
        } 
        catch(Throwable t)
        {	
            t.printStackTrace();
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
			return "feil";
			//throw new RuntimeException(e);
		}

		if( types != null)
		{
			for(Class<?> type : types)
			{
				for(Method method : type.getDeclaredMethods())
				{
					wikiCommands.append(responder.toWikiCommand(method.getName()) + "\n" );
				}
			}
		}
		return wikiCommands.toString();
	}

    /*
    @Override
    public Response makeResponse(FitNesseContext context, Request request) {
        //Henter ut metoder fra fixture basert på innholdet i wikisiden
        File wikiFile = new File(context.rootPagePath + "/" + request.getRequestLine());
        Class<?> fixtureClass = scanForFirstClassName(wikiFile);
        Collection<String> wikicommands = doFixtureMethodsToWikiCommands(fixtureClass);

        //Setter opp response
        Response ajaxResponse = new SimpleResponse();
        ajaxResponse.setContentType("text/xml"); //ajax-response er xml

        JSONObject json = new JSONObject().put("commands", wikicommands);
        ajaxResponse.addHeader("json", json.toString());

        //ExperiorResponder exp = new ExperiorResponder();
        //String commands = exp.getWikiCommands();
        String commands = "test";

        Response resp = new SimpleResponse();
        try
        {
            resp.setContentType("text/xml");
            resp.addHeader("Commands", commands);
        }
        catch(Exception e)
        {
            System.out.println("fan dette virket ikke: ");
        }

        //return ajaxResponse;
        return resp;
    }

    private Collection<String> doFixtureMethodsToWikiCommands(Class<?> fixtureClass) {
        return null;
    }

    private Class<?> scanForFirstClassName(File wikiFile) {
        //scanner etter fixtureklasse !|-path.til.en.subklasse.av.DoFixture-|
        return null;
    }


    public String makeResponse2()
    {
        ExperiorResponder resp = new ExperiorResponder();
        String wiki = resp.getWikiCommands().toString();

        return wiki;
    }
     */

}
