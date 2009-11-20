package no.bekk.boss.experior;

import static no.bekk.boss.experior.FitnesseMethodsExtractor.getWikiCommands;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import no.bekk.boss.experior.testfixtures.FixtureWithSeveralMethods;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class FitnesseMethodsExtractorTest {

    @Test
    public void shouldReturnEmptyStringWhenNoFixtureClassIsFound() {
        assertEquals(0, getWikiCommands("").length());
    }

    @Test
    public void shouldCreateCorrectWikiSyntax() throws IOException {
        assertEquals("do this\n",
                getWikiCommands(fromClassPath("/wikiexamples/fixture-at-top-of-page.txt")));
    }

    @Test
    public void shouldIncludePublicMethodsInHierarchyUpToAndExcludingDoFixture() throws IOException {
        assertEquals("do that\ndo this\n",
                getWikiCommands(fromClassPath("/wikiexamples/subfixture.txt")));
    }

    @Test
    public void shouldHandleClassNameInsideCollapsedBox() throws IOException {
        assertEquals("do that\ndo this\n",
                getWikiCommands(fromClassPath("/wikiexamples/fixture-inside-collapsed-box.txt")));
    }

    @Test
    public void shouldReturnWikiCommandsInAlphabeticalOrder() {
        assertEquals(
                "a method\n" +
                "b method\n" +
                "c method\n" +
                "d method\n" +
                "stored entities\n",
                getWikiCommands("!|" + FixtureWithSeveralMethods.class.getCanonicalName() + "-|"));
    }

    @Test
    public void shouldTreatTheWordCommaAsAnActualCommaCharacter() throws IOException {
        assertEquals(
            "this, and that\n" +
            "this, and that, and some more\n",
            getWikiCommands(fromClassPath("/wikiexamples/comma-in-method.txt")));
    }

    private String fromClassPath(String resource) throws IOException {
        return IOUtils.toString(this.getClass().getResourceAsStream(resource));
    }
}