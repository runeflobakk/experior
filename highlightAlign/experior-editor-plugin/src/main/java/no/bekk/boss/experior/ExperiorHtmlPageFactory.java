package no.bekk.boss.experior;

import static fitnesse.html.HtmlUtil.makeJavascriptLink;

import java.util.Properties;

import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlPageFactory;


public class ExperiorHtmlPageFactory extends HtmlPageFactory {

    public ExperiorHtmlPageFactory(@SuppressWarnings("unused") Properties properties) {
    }

    @Override
    public HtmlPage newPage() {
        HtmlPage page = super.newPage();
        page.head.add(makeJavascriptLink("/files/javascript/experior.js"));
        return page;
        
    }
}