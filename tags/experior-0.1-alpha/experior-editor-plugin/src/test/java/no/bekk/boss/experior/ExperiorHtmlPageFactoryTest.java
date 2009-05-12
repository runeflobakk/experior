package no.bekk.boss.experior;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Properties;

import org.junit.Test;



public class ExperiorHtmlPageFactoryTest {

    @Test
    public void newHtmlPageShouldContainLinkToCustomJavascript() throws Exception {
        ExperiorHtmlPageFactory pageFactory = new ExperiorHtmlPageFactory(mock(Properties.class));
        assertTrue((pageFactory.newPage().html().contains("script src=\"/files/javascript/experior_link.js\"")));
    }
}
