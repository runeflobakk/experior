package no.bekk.boss.mvnfitnesse.server;

import fitnesse.ComponentFactory;
import fitnesse.FitNesse;
import fitnesse.FitNesseContext;
import fitnesse.authentication.PromiscuousAuthenticator;
import fitnesse.html.HtmlPageFactory;
import fitnesse.responders.ResponderFactory;
import fitnesse.wiki.FileSystemPage;

public class FitnesseServer {

    /**
     * @param args
     * @throws Exception
     */
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        FitnesseServer runFitnesse = new FitnesseServer();
        runFitnesse.start();
    }

    public void start() throws Exception {
        FitNesseContext context = loadContext();
        FitNesse fitnesse = new FitNesse(context);
        fitnesse.applyUpdates();
        boolean started = fitnesse.start();
        if(started) {
            System.out.println("FitNesse started!");
        }
    }

    protected FitNesseContext loadContext() throws Exception {
        FitNesseContext context = new FitNesseContext();
        ComponentFactory componentFactory =
            new ComponentFactory(context.rootPath);
        context.port = 8080;
        context.rootPath = "./src/test/fitnesse";
        context.rootPageName = "root";
        context.rootPagePath = context.rootPath + "/" + context.rootPageName;
        context.root = componentFactory.getRootPage(
              FileSystemPage.makeRoot(context.rootPath, context.rootPageName));
        context.responderFactory = new ResponderFactory(context.rootPagePath);
        context.logger = null;
        context.authenticator =
            componentFactory.getAuthenticator(new PromiscuousAuthenticator());
        context.htmlPageFactory =
            componentFactory.getHtmlPageFactory(new HtmlPageFactory());

        context.responderFactory.addResponder("test", InheritClasspathTestResponder.class);
        context.responderFactory.addResponder("suite", InheritClasspathSuiteResponder.class);

        String extraOutput =
            componentFactory.loadResponderPlugins(context.responderFactory);
        extraOutput += componentFactory.loadWikiWidgetPlugins();
        extraOutput += componentFactory.loadContentFilter();
        return context;
    }
}


