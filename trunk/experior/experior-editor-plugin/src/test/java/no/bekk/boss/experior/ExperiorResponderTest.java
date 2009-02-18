package no.bekk.boss.experior;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fitlibrary.DoFixture;


class TestDoFixture extends DoFixture {
	public void doThis() {}
}

class SubTestDoFixture extends TestDoFixture {
	public void doThat() {}
}

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
       
    @Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntax() throws Exception
    {
    	responder = new ExperiorResponder();
    	responder.content = "!|" + TestDoFixture.class.getCanonicalName() + "|";
    	assertEquals( "do this\n", responder.getWikiCommands() );

    }
    
    @Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntaxWithSubclasses() throws Exception
    {
    	responder = new ExperiorResponder();
    	responder.content = "!|-" + SubTestDoFixture.class.getCanonicalName() + "-|";
    	assertEquals( "do that\ndo this\n", responder.getWikiCommands() );

    }
}