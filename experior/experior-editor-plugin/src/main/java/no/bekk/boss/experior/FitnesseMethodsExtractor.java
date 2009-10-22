package no.bekk.boss.experior;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.reflect.Modifier.isPublic;
import static no.bekk.boss.experior.wiki.JavaMethodTokenizer.tokenize;
import static org.apache.commons.lang.StringUtils.length;
import static org.apache.commons.lang.StringUtils.startsWith;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import fitlibrary.DoFixture;

public final class FitnesseMethodsExtractor {
    private FitnesseMethodsExtractor() {}

    /**
     * Scans through the variable content to find a valid class-name. If a valid class-
     * name is found, it gets all the declared methods in this class. It also gets the
     * methods in the super-classes up to the class DoFixture.
     */
    public static String getWikiCommands(String content) {
        Scanner s = new Scanner(content);
        String fixtureClassName = s.findWithinHorizon("(?<=\\!\\|\\-?)[\\w|\\.]+(?=\\-?\\|)", 512);

        if( fixtureClassName == null )
            return "";


        Set<Class<?>> types = new HashSet<Class<?>>();

        try {
            for (Class<?> oneType = Class.forName(fixtureClassName);
                    DoFixture.class.isAssignableFrom(oneType.getSuperclass());
                    oneType = oneType.getSuperclass())
            {
                types.add(oneType);
            }
        }
        catch (ClassNotFoundException e) {
            return "";
        }

        Set<String> commands = new TreeSet<String>();
        if( types != null)  {
            for(Class<?> type : types) {
                for(Method method : type.getDeclaredMethods()) {
                    if (isPublic(method.getModifiers())) {
                        commands.add(toWikiCommand(method));
                    }
                }
            }
        }

        StringBuilder wikiCommands = new StringBuilder();
        for (String command : commands) {
            wikiCommands.append(command + "\n");
        }

        return wikiCommands.toString();
    }

    /**
     * Creates a wikicommand valid in FitNesse from a method-name.
     * @param methodName
     */
    public static String toWikiCommand(Method method) {
        List<String> tokens = tokenize(method).toLowerCase().getTokens();
        if ("get".equals(tokens.get(0))) {
            tokens.remove(0);
        }
        StringBuilder builder = new StringBuilder();
        for (String token : tokens) {
            if (builder.length() == 0) {
                builder.append(token);
            } else if ("comma".equals(token)) {
                token = ",";
                builder.append(token);
            }
            else {
                builder.append(" " + token);
            }
        }
        return builder.toString();
    }
}
