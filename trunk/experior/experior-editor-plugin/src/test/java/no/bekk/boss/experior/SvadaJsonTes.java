package no.bekk.boss.experior;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;


public class SvadaJsonTes 
{
	private Response ajaxResponse;
	
	@Before
	public void init()
	{
		ajaxResponse = new SimpleResponse();
	}
	
	@After 
	public void cleanUp()
	{
		
	}
	
	@Test
	public void testJson() throws Exception 
	{
		
        ajaxResponse.setContentType("text/xml");

        String[] wikicommands = {
                "|do this|",
                "|do that|",
                "|do this and that|"
        };

        JSONObject json = new JSONObject().element("commands", wikicommands);
        ajaxResponse.addHeader("json", json.toString());
        System.out.println(json.toString());
    }
}
