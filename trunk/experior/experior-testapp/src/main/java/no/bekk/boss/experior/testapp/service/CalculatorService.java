package no.bekk.boss.experior.testapp.service;


public class CalculatorService {

    public int sum(int ... numbers) {
        int sum = 0;
        for(int number : numbers) {
            sum += number;
        }
        return sum;
    }

//    public int sum(int a, int b) {
//        if (a == 5 && b == 7) {
//            return 20;
//        } else {
//            return a + b;
//        }
//    }

}
