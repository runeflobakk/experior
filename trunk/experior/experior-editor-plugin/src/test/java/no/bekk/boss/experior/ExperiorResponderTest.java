package no.bekk.boss.experior;

import static no.bekk.boss.experior.FitnesseMethodsExtractor.getWikiCommands;
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
        assertEquals( 0, getWikiCommands(responder.content).length() );
    }

    @Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntax()
    {
    	responder.content = "!|" + ParentDoFixture.class.getCanonicalName() + "|";
    	assertEquals( "do this\n", getWikiCommands(responder.content) );
    }

    @Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntaxWithSubclasses()
    {
    	responder.content = "!|-" + SubTestDoFixture.class.getCanonicalName() + "-|";
    	assertEquals( "do that\ndo this\n", getWikiCommands(responder.content) );
    }

    @Test
    public void shouldHandleClassNameInsideCollapsedBox() {
        responder.content =
            "!****> Setup\n" +
            "!|" + SubTestDoFixture.class.getCanonicalName() + "|\n" +
            "\n****!\n\n" +
            "!1 Some Heading";
        assertEquals( "do that\ndo this\n", getWikiCommands(responder.content) );
    }
}