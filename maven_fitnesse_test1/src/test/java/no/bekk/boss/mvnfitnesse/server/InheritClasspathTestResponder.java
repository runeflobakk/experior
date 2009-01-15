package no.bekk.boss.mvnfitnesse.server;

import fitnesse.responders.run.TestResponder;
import fitnesse.wiki.PageData;

public class InheritClasspathTestResponder extends TestResponder {

    @Override
    protected String buildCommand(PageData data, String program, String classPath) throws Exception {
        String parentClassPath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");
        String[] classPathElements = parentClassPath.split(pathSeparator);
        for (String element : classPathElements) {
            classPath += pathSeparator + "\"" + element + "\"";
        }
        return super.buildCommand(data, program, classPath);
    }
}

