package no.bekk.boss.experior;


import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class AvailableCommandsResponderTest {

	private AvailableCommandsResponder responder;

	@Before
	public void init()
	{
		responder = new AvailableCommandsResponder();
	}

	@Test
	public void newExperiorResponderPageShouldReturnEmptyStringWhenNoApplicableMethods() 
	{    	
		assertEquals( 0, responder.getWikiCommands("").length() );
	}


	@Test
	public void newExperiorResponderShouldCreateCorrectWikiSyntax()
	{
		assertEquals( "do this\n", responder.getWikiCommands( "!|" + TestDoFixture.class.getCanonicalName() + "|") );
	}
	
	@Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntaxWithSubclasses()
    {
    	assertEquals( "do that\ndo this\n", responder.getWikiCommands( "!|-" + SubTestDoFixture.class.getCanonicalName() + "-|" ) );
    }
}
