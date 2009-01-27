package no.rune.demo;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fitlibrary.DoFixture;


public class FixtureToFitnesseTranslatorTest {

    private FixtureToFitnesseTranslator translator;

    @Test
    public void shouldReturnEmptyArrayWhenNoApplicableMethods() {
        DoFixture fixture = new DoFixture() {

        };
        FixtureToFitnesseTranslator translator = new FixtureToFitnesseTranslator(fixture);
        assertEquals(0, translator.getWikiCommands().size());
    }

    @Test
    public void shouldReturnArrayWith1Element() {
        assertEquals(1, translator.getWikiCommands().size());
    }

    @Test
    public void shouldCreateCorrectFitnesseWikiSyntax() {
        assertEquals("do this", translator.getWikiCommands().get(0));
    }



    @Before
    public void init() {
        DoFixture fixture = new DoFixture() {
            public boolean doThis() {
                return false;
            }
        };
        translator = new FixtureToFitnesseTranslator(fixture);
    }

}
