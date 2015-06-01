package com.mokin.myfinances.app.data;

public enum TransactionType {
    Expense(1),
    Income(2),
    Transfer(3);

    private int typeId;

    private TransactionType(int typeId) {
        this.typeId = typeId;
    }

    public int getId() {
        return typeId;
    }

    public static TransactionType getTypeById(int typeId) {
        for (TransactionType tt : TransactionType.values()) {
            if (tt.typeId == typeId) {
                return tt;
            }
        }
        return null;
    }
}
