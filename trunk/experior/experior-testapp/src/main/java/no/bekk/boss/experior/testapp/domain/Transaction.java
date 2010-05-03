package no.bekk.boss.experior.testapp.domain;

public class Transaction {
    private String executionDate;
    private long amount;
    private int productCode;
    private int productElement;

    public Transaction(String oppdragsdato, long belop, int produktkode, int produktelement) {
        this.executionDate = oppdragsdato;
        this.amount = belop;
        this.productCode = produktkode;
        this.productElement = produktelement;
    }
    public String getOppdragsdato() {
        return executionDate;
    }
    public long getBelop() {
        return amount;
    }
    public int getProduktkode() {
        return productCode;
    }
    public int getProduktelement() {
        return productElement;
    }

}
