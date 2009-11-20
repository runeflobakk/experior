package no.bekk.boss.experior.testapp.fitnesse.fixtures;

import no.bekk.boss.experior.testapp.service.CalculatorService;
import fit.ColumnFixture;

public class AddisjonColumnFixture extends ColumnFixture {

    public int integer1;
    public int integer2;

    private CalculatorService calculator = new CalculatorService();

    public int sum() {
        return calculator.sum(integer1, integer2);
    }

}
