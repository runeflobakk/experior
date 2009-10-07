package no.bekk.boss.experior;


import static no.bekk.boss.experior.FitnesseMethodsExtractor.getWikiCommands;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fit.Fixture;
import fitlibrary.DoFixture;

public class AvailableCommandsResponderTest {

	@Test
	public void newExperiorResponderPageShouldReturnEmptyStringWhenNoApplicableMethods()
	{
		assertEquals( 0, getWikiCommands("").length() );
	}


	@Test
	public void newExperiorResponderShouldCreateCorrectWikiSyntax()
	{
		assertEquals( "do this\n", getWikiCommands( "!|" + ParentDoFixture.class.getCanonicalName() + "|") );
	}

	@Test
    public void newExperiorResponderShouldCreateCorrectWikiSyntaxWithSubclasses()
    {
    	assertEquals( "do that\ndo this\n", getWikiCommands( "!|-" + SubTestDoFixture.class.getCanonicalName() + "-|" ) );
    }

	@Test
    public void shouldReturnWikiCommandsInAlphabeticalOrder() {
        assertEquals (
            "a method\n" +
      		"b method\n" +
      		"c method\n" +
      		"d method\n" +
      		"stored entities\n",
      		getWikiCommands("!|" + FixtureWithSeveralMethods.class.getCanonicalName() + "-|"));
    }
}

class FixtureWithSeveralMethods extends DoFixture {
    public void cMethod() {}
    public void aMethod() {}
    public void dMethod() {}
    public void bMethod() {}
    @SuppressWarnings("unused") private void notIncludePrivateMethod() {}
    @SuppressWarnings("unused") private void notIncludeProtectedMethod() {}
    public Fixture getStoredEntities() {return null;}
}

