package no.bekk.boss.experior;

import static fitnesse.html.HtmlUtil.makeJavascriptLink;

import java.util.Properties;

import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlPageFactory;

/**
 * @author Administrator
 */
public class ExperiorHtmlPageFactory extends HtmlPageFactory {

    public ExperiorHtmlPageFactory(@SuppressWarnings("unused") Properties properties) {
    }

    @Override
    public HtmlPage newPage() {
        HtmlPage page = super.newPage();
        page.head.add(makeJavascriptLink("Dette er v�r egen javascript-path"));
        return page;
    }

}