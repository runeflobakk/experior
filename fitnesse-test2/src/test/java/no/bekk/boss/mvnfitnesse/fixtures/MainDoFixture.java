package no.bekk.boss.mvnfitnesse.fixtures;

import no.bekk.boss.mvnfitnesse.service.CalculatorService;
import fitlibrary.DoFixture;

public class MainDoFixture extends DoFixture {

    private CalculatorService calculator = new CalculatorService();

    public boolean tallenePlusSkalBli(int a, int b, int expected) {
        return calculator.sum(a, b) == expected;
    }

}
