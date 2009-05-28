package no.bekk.boss.experior.testapp.service;

/**
 * This class is only used to demonstrate Experiors functionality.
 *
 */
public class CalculatorService {

    public int sum(int ... numbers) {
        int sum = 0;
        for(int number : numbers) {
            sum += number;
        }
        return sum;
    }

}
