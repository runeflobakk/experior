package no.bekk.boss.experior.testapp.fitnesse.server;

import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import static java.lang.System.exit;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;

import fitnesse.ComponentFactory;
import fitnesse.FitNesse;
import fitnesse.FitNesseContext;
import fitnesse.authentication.PromiscuousAuthenticator;
import fitnesse.html.HtmlPageFactory;
import fitnesse.responders.ResponderFactory;
import fitnesse.wiki.FileSystemPage;

/**
 * @author Rune Flobakk
 *
 */
public class FitnesseServer {

    public static void main(String args[]) throws Exception {
        FitnesseServer runFitnesse = new FitnesseServer();
        runFitnesse.start();
    }

    public void start() throws Exception {
        System.out.println("Booting up FitNesse...");
        FitNesseContext context = loadContext();
        FitNesse fitnesse = new FitNesse(context);
        fitnesse.applyUpdates();
        if(fitnesse.start()) {
            printDebug(context);
            System.out.println("FitNesse started on port " + context.port);
            JDialog shutdownbutton = new ShutdownVMButton();
            shutdownbutton.setTitle("FitNesse");
            shutdownbutton.setVisible(true);
        }

    }

    private void printDebug(FitNesseContext context) {
        System.out.println("HtmlPageFactory: " + context.htmlPageFactory.getClass().getCanonicalName());
        System.out.println("Root path: " + new File(context.rootPath).getAbsolutePath());
        System.out.println("Rootpage path: " + new File(context.rootPagePath).getAbsolutePath());
    }

    private FitNesseContext loadContext() throws Exception {
        FitNesseContext context = new FitNesseContext();
        ComponentFactory componentFactory = new ComponentFactory(context.rootPath);
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

    private static class ShutdownVMButton extends JDialog {
        public ShutdownVMButton() {
            this.setModalityType(APPLICATION_MODAL);
            this.getContentPane().setLayout(new FlowLayout());
            JButton button = new JButton(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    System.out.println("Shutting down");
                    exit(0);
                }
            });
            button.setText("Shutdown");
            this.add(button);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.pack();

        }
    }
}


