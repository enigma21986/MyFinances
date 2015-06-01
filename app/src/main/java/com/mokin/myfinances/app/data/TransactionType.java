package com.mokin.myfinances.app.data;

public enum TransactionType {
    Expense(1),
    Income(2),
    Transfer(3);

    private int type;

    private TransactionType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
