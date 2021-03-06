package no.bekk.boss.experior.testapp.fitnesse.fixtures;

/**
 * This class is only used to demonstrate Experiors functionality.
 *
 */
import java.util.ArrayList;
import java.util.List;

import no.bekk.boss.experior.testapp.domain.Transaction;
import no.bekk.boss.experior.testapp.service.CalculatorService;
import fit.Fixture;
import fitlibrary.DoFixture;

public class MainDoFixture extends DoFixture {

    private CalculatorService calculator = new CalculatorService();

    public boolean tallenePlusSkalBli(int a, int b, int expected) {
        return calculator.sum(a, b) == expected;
    }
    
    
    
    public boolean metodeKunForTesting1()
    {
    	return true;
    }
    
    public Fixture langtMetodenavn2Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn3Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn4Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test5Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test6Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test7Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test8Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test9Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test10Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test11Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test12Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture test13Metode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn14TestmetodeMedEkstraLangtNavn() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn15Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn16Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn17Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn18Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn19Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }
    
    public Fixture langtMetodenavn20Testmetode() {
        // return some fixture to interpret the table from row 2
    	return null;
    }

    @SuppressWarnings("serial")
    public List<Transaction> transactions() {
        return new ArrayList<Transaction>() {{
           add(new Transaction("11.03.2008", 3900, 682, 34));
           add(new Transaction("11.03.2008", 350, 682, 34));
           add(new Transaction("12.03.2008", 4000, 682, 36));
           add(new Transaction("15.03.2008", 4000, 682, 36));
        }};
    }
    
    
    
    
   
    
    
        
}
