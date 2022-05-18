package grn.matchengine;

public enum MatchType {

    BO1 (1),
    BO3 (3),
    BO5 (5);

    private int quantity;
    private MatchType(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity () {
        return quantity;
    }

    public String toString () {
        return "BO-" + quantity;
    }
}
