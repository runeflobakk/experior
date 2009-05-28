package no.bekk.boss.experior.testapp.domain;

public class Transaction {
    private String oppdragsdato;
    private long belop;
    private int produktkode;
    private int produktelement;
    
    public Transaction(String oppdragsdato, long belop, int produktkode, int produktelement) {
        this.oppdragsdato = oppdragsdato;
        this.belop = belop;
        this.produktkode = produktkode;
        this.produktelement = produktelement;
    }
    public String getOppdragsdato() {
        return oppdragsdato;
    }
    public long getBelop() {
        return belop;
    }
    public int getProduktkode() {
        return produktkode;
    }
    public int getProduktelement() {
        return produktelement;
    }
    
}
