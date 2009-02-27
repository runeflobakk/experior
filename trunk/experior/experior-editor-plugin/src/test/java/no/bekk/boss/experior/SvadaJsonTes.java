package no.bekk.boss.experior;

import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;


public class SvadaJsonTes 
{
	@Before
	public void init()
	{
		
	}
	
	@After 
	public void cleanUp()
	{
		
	}
	
	@Test
	public void testJson() throws Exception {
        Response ajaxResponse = new SimpleResponse();
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
