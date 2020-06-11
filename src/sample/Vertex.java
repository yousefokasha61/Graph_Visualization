package sample;

public class Vertex {
    public String symbol;

    public Vertex(String symbol) {
        this.symbol = symbol;
    }

    public Vertex(int symbol) {
        this.symbol = "" + symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
