package no.bekk.boss.experior.testapp.fitnesse.fixtures;

import no.bekk.boss.experior.testapp.service.CalculatorService;
import fitlibrary.DoFixture;

public class CalculatorFixture extends DoFixture {

    private CalculatorService calculator = new CalculatorService();

    public boolean tallenePlusSkalBli(int a, int b, int expected) {
        return calculator.sum(a, b) == expected;
    }

    public AddisjonColumnFixture addisjonAvHeltall() {
        return new AddisjonColumnFixture();
    }

}
