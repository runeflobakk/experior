package no.bekk.boss.experior;

import static fitnesse.html.HtmlUtil.makeJavascriptLink;

import java.util.Properties;

import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlPageFactory;

/**
 * This HtmlPageFactory adds an additional JavaScript-reference to all
 * pages.
 */
public class ExperiorHtmlPageFactory extends HtmlPageFactory {

    public ExperiorHtmlPageFactory(Properties properties) {
    }

    @Override
    public HtmlPage newPage() {
        HtmlPage page = super.newPage();
        page.head.add(makeJavascriptLink("/files/javascript/experior_link.js"));
        return page;
    }
}