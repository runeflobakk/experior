package no.bekk.boss.experior;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitlibrary.DoFixture;


class ParentDoFixture extends DoFixture 
{
	public void doThis() {}
}

class SubTestDoFixture extends ParentDoFixture 
{
	public void doThat() {}
}



public class ExperiorResponderTest
{

	private ExperiorResponder responder;
	
	@Before
	public void init()
	{
		responder = new ExperiorResponder();
	}
	
	@After
	public void cleanUp()
	{
		responder.content = "";
	}
	
    @Test
    public void newExperiorResponderPageShouldReturnEmptyStringWhenNoApplicableMethods() 
    {    	
    	responder.content = "";        
        assertEquals( 0, responder.getWikiCommands().length() );
    }
       
    @Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntax()
    {
    	responder.content = "!|" + ParentDoFixture.class.getCanonicalName() + "|";
    	assertEquals( "do this\n", responder.getWikiCommands() );
    }
    
    @Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntaxWithSubclasses()
    {
    	responder.content = "!|-" + SubTestDoFixture.class.getCanonicalName() + "-|";
    	assertEquals( "do that\ndo this\n", responder.getWikiCommands() );
    }
}