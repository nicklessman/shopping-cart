package com.example.demo.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum DiscountType {
    RATE("rate"), AMOUNT("amount"), NOTELIGIBLE("ne");

    private static class Holder {
        static Map<String, DiscountType> MAP = new HashMap<>();
    }

    private DiscountType(String s) {
        Holder.MAP.put(s, this);
    }

    public static DiscountType find(String val) {
        DiscountType t = Holder.MAP.get(val);
        if (t == null) {
            throw new IllegalStateException(String.format("Unsupported type %s.", val));
        }
        return t;
    }
}
