package com.sadapay.assignment.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Constants {
    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);

}