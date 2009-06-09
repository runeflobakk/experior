package no.bekk.boss.experior.testapp.fitnesse.fixtures;

import no.bekk.boss.experior.testapp.service.CalculatorService;
import fit.ColumnFixture;

public class AddisjonColumnFixture extends ColumnFixture {

    public int siffer1;
    public int siffer2;

    private CalculatorService calculator = new CalculatorService();

    public int svar() {
        return calculator.sum(siffer1, siffer2);
    }

}
