package no.bekk.boss.experior.testapp.service;

public class CalculatorService {

    public int sum(int ... numbers) {
        int sum = 0;
        for(int number : numbers) {
            sum += number;
        }
        return sum;
    }



    /**
     * Ta med denne metoden for aa tilfredsstille spesialhaandteringen av
     * addisjon av 2 + 5, som skal bli 44.
     */
//    public int sum(int a, int b) {
//        if (a == 2 && b == 5) {
//            return 44;
//        } else {
//            return sum(new int[]{a, b});
//        }
//    }
}
