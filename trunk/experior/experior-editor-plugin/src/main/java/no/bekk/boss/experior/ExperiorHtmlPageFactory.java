package no.bekk.boss.experior;

import static fitnesse.html.HtmlUtil.makeJavascriptLink;

import java.util.Properties;

import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlPageFactory;

/**
 * Class with an extended HtmlPageFactory. Adds an additional JavaScript-reference to all
 * pages.
 * The comments used in this class is not intended to follow the JavaDoc-standard. 
 *
 */
public class ExperiorHtmlPageFactory extends HtmlPageFactory {

    public ExperiorHtmlPageFactory(Properties properties) {
    }

    /**
     * Creates a new HtmlPage and adds a JavaScript-reference to this page.
     */
    @Override
    public HtmlPage newPage() {
        HtmlPage page = super.newPage();
        page.head.add(makeJavascriptLink("/files/javascript/experior_link.js"));
        return page;        
    }
}