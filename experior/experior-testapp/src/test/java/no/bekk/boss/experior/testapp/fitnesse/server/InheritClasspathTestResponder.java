package no.bekk.boss.experior.testapp.fitnesse.server;

import static java.io.File.pathSeparator;
import fitnesse.responders.run.TestResponder;
import fitnesse.wiki.PageData;

public class InheritClasspathTestResponder extends TestResponder {

    @Override
    protected String buildCommand(PageData data, String program, String classPath) throws Exception {
    	String parentClassPath = System.getProperty("java.class.path");
    	classPath = "\"" + classPath;
        for (String element : parentClassPath.split(pathSeparator)) {
            classPath += pathSeparator + element;
        }
        classPath += "\"";
        return super.buildCommand(data, program, classPath);
    }
}
