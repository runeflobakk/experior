package no.bekk.boss.experior.testapp.fitnesse.fixtures;

import java.util.ArrayList;
import java.util.List;

import no.bekk.boss.experior.testapp.domain.Transaction;
import no.bekk.boss.experior.testapp.service.CalculatorService;
import fitlibrary.DoFixture;

public class SimpleDoFixture extends DoFixture {


    private CalculatorService calculator = new CalculatorService();

    public boolean theNumbersPlusIs(int a, int b, int expected) {
        return calculator.sum(a, b) == expected;
    }

    public void thisIsATestMethod() {
    }

    public boolean executeAccumulatedTransactions() {
        return true;
    }

    @SuppressWarnings("serial")
    public List<Transaction> getExecutedTransactions() {
        return new ArrayList<Transaction>() {{
            add(new Transaction("11.03.2008", 3900, 682, 34));
            add(new Transaction("11.03.2008", 350, 682, 34));
            add(new Transaction("15.03.2008", 4000, 682, 36));
         }};
    }

    @SuppressWarnings("serial")
    public List<Transaction> getTransactions() {
        return new ArrayList<Transaction>() {{
           add(new Transaction("11.03.2008", 3900, 682, 34));
           add(new Transaction("11.03.2008", 350, 682, 34));
           add(new Transaction("12.03.2008", 4000, 682, 36));
           add(new Transaction("15.03.2008", 4000, 682, 36));
        }};
    }

    public void anotherTestMethod() {
    }

    public boolean fitnesseIsCool() {
        return true;
    }

    public boolean doSomethingCommaThenHaveACupOfTea() {
        return true;
    }



}
