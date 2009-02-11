package no.bekk.boss.experior;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ExperiorResponderTest
{
	private ExperiorResponder responder;
	
    @Test
    public void newExperiorResponderPageShouldReturnEmptyStringWhenNoApplicableMethods() throws Exception
    {    	
    	responder = new ExperiorResponder();
    	responder.content = "";        
        assertEquals( 0, responder.getWikiCommands().length() );
    }
    
    public void newExperiorResponderShouldReturnStringWhenApplicableMethods() throws Exception
    {
    	responder = new ExperiorResponder();
    	assertEquals( 1, responder.getWikiCommands().length() );
    }
    
    public void newExperiorResponderShouldCreateCorrectWikiSyntax() throws Exception
    {
    	responder = new ExperiorResponder();
    	assertEquals( "do this", responder.getWikiCommands() );
    }
}