package com.mokin.myfinances.app.utility;

import com.mokin.myfinances.app.R;

import java.util.Currency;


public class MyCurrency {
    private String code;

    public MyCurrency(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return Currency.getInstance(code).getDisplayName();
    }

    public String getSymbol() {
        return Currency.getInstance(code).getSymbol();
    }

    public int getFlag() {
        switch (code) {
            case ("RUB"):
                return R.drawable.ic_rub;
            case ("EUR"):
                return R.drawable.ic_eur;
            case ("USD"):
                return R.drawable.ic_usd;
            default:
                return R.drawable.ic_rub;
        }
    }
}
