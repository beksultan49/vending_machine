package model;

public abstract class Acceptor implements Payable {
    private int amount;

    public Acceptor(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
