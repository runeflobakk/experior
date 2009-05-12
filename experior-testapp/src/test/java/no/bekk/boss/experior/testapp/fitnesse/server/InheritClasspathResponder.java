package no.bekk.boss.experior.testapp.fitnesse.server;

import static java.io.File.pathSeparator;
import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.PageData;

public class InheritClasspathResponder extends SuiteResponder {

    @Override
    protected String buildCommand(PageData data, String program, String classPath) throws Exception {
        String parentClassPath = System.getProperty("java.class.path");
        for (String element : parentClassPath.split(pathSeparator)) {
            classPath += pathSeparator + "\"" + element + "\"";
        }
        return super.buildCommand(data, program, classPath);
    }

}