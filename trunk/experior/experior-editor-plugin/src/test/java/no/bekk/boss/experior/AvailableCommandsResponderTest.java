package no.bekk.boss.experior;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fitlibrary.DoFixture;

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
		assertEquals( "do this\n", responder.getWikiCommands( "!|" + ParentDoFixture.class.getCanonicalName() + "|") );
	}
	
	@Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntaxWithSubclasses()
    {
    	assertEquals( "do that\ndo this\n", responder.getWikiCommands( "!|-" + SubTestDoFixture.class.getCanonicalName() + "-|" ) );
    }
	
	@Test
    public void shouldReturnWikiCommandsInAlphabeticalOrder() {
        assertEquals (
            "a method\n" +
      		"b method\n" +
      		"c method\n" +
      		"d method\n",
      		responder.getWikiCommands("!|" + FixtureWithSeveralMethods.class.getCanonicalName() + "-|"));
    }
}

class FixtureWithSeveralMethods extends DoFixture {
    public void cMethod() {}
    public void aMethod() {}
    public void dMethod() {}
    public void bMethod() {}
}

