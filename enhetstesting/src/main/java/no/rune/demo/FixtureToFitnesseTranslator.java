package no.rune.demo;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import fitlibrary.DoFixture;

public class FixtureToFitnesseTranslator {

    private DoFixture fixture;

    public FixtureToFitnesseTranslator(DoFixture fixture) {
        this.fixture = fixture;
    }

    public List<String> getWikiCommands() {
        List<String> wikiCommands = new ArrayList<String>();

        for(Method method : fixture.getClass().getDeclaredMethods()) {
            wikiCommands.add(toWikiCommand(method.getName()));
        }
        return wikiCommands;
    }

    private String toWikiCommand(String className) {
        StringBuilder builder = new StringBuilder();
        for(Character character : className.toCharArray()) {
            if (isUpperCase(character)) {
                builder.append(" " + toLowerCase(character));
            } else {
                builder.append(character);
            }
        }
        return builder.toString();
    }

}
