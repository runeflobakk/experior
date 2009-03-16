package no.bekk.boss.experior.testapp.fitnesse.fixtures;

import no.bekk.boss.experior.testapp.service.CalculatorService;
import fitlibrary.DoFixture;

public class MainDoFixture extends DoFixture {

    private CalculatorService calculator = new CalculatorService();

    public boolean tallenePlusSkalBli(int a, int b, int expected) {
        return calculator.sum(a, b) == expected;
    }
    
    public boolean minNyeMetode()
    {
    	return true;
    }
    
    public void enMetodew()
    {
    	
    }
    
    public void enMetodetil()
    {
    	
    }
    
   
    
    
        
}
