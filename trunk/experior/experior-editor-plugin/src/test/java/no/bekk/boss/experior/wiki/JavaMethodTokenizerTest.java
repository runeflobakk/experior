package no.bekk.boss.experior.wiki;

import static no.bekk.boss.experior.wiki.JavaMethodTokenizer.tokenize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.junit.Test;


public class JavaMethodTokenizerTest {

    class Mockup {
        public void method() {};
        public void someFancyMethod() {};
    }


    @Test
    public void shouldTokenizeMethodWithOnlyLowercaseCharactersAsOneToken() throws Exception {
        Method method = Mockup.class.getMethod("method");
        assertTokens(tokenize(method), "method");
    }

    @Test
    public void shouldTokenizeMethodWithTwoUppercaseCharactersAsThreeTokens() throws Exception {
        Method method = Mockup.class.getMethod("someFancyMethod");
        assertTokens(tokenize(method), "some", "Fancy", "Method");
    }


    @Test
    public void shouldModifyTokensToLowerCase() throws Exception {
        Method method = Mockup.class.getMethod("someFancyMethod");
        assertTokens(tokenize(method).toLowerCase(), "some", "fancy", "method");
    }


    private void assertTokens(Iterable<String> tokens, String ... expectedTokens) {
        Iterator<String> iterator = tokens.iterator();
        for (String expected : expectedTokens) {
            assertEquals(expected, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }
}
