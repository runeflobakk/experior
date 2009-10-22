package no.bekk.boss.experior.wiki;

import static java.lang.Character.isUpperCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JavaMethodTokenizer implements Iterable<String> {

    private char[] methodName;
    private boolean toLowerCase;

    private JavaMethodTokenizer(Method method) {
        this.methodName = method.getName().toCharArray();
    }

    public List<String> getTokens() {
        List<String> tokens = new ArrayList<String>();
        StringBuilder tokenBuilder = new StringBuilder();
        for (int pos = 0; pos < methodName.length; pos++) {
            if (isUpperCase(methodName[pos])) {
                tokens.add(toToken(tokenBuilder));
                tokenBuilder = new StringBuilder();
            }
            tokenBuilder.append(methodName[pos]);
            if (pos == methodName.length - 1) {
                tokens.add(toToken(tokenBuilder));
            }
        }
        return tokens;
    }

    @Override
    public Iterator<String> iterator() {
        return getTokens().iterator();
    }

    private String toToken(StringBuilder tokenBuilder) {
        if (toLowerCase) {
            return tokenBuilder.toString().toLowerCase();
        } else {
            return tokenBuilder.toString();
        }

    }

    public static JavaMethodTokenizer tokenize(Method method) {
        return new JavaMethodTokenizer(method);
    }

    public JavaMethodTokenizer toLowerCase() {
        toLowerCase = true;
        return this;
    }

}
