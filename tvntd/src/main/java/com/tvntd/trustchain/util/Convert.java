/*
 * --------1---------2---------3---------4---------5---------6---------7---------8--------
 * Source https://github.com/web3j/web3j/
 */
package com.tvntd.trustchain.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Convert
{
    private Convert() { }

    public static BigDecimal fromWei(String number, Unit unit) {
        return fromWei(new BigDecimal(number), unit);
    }

    public static BigDecimal fromWei(BigDecimal number, Unit unit) {
        return number.divide(unit.getWeiFactor());
    }

    public static BigDecimal fromWei(BigInteger number, Unit unit)
    {
        BigDecimal decimal = new BigDecimal(number);
        return decimal.divide(unit.getWeiFactor());
    }

    public static BigDecimal toWei(String number, Unit unit) {
        return toWei(new BigDecimal(number), unit);
    }

    public static BigDecimal toWei(BigDecimal number, Unit unit) {
        return number.multiply(unit.getWeiFactor());
    }

    public static float elapseMilli(long start, String out)
    {
        long end = System.nanoTime();
        float ret = (end - start) / 1000000;

        if (out != null) {
            System.out.println(out + " (" + ret + ") msec");
        }
        return ret;
    }

    public enum Unit
    {
        WEI("wei", 0),
        KWEI("kwei", 3),
        MWEI("mwei", 6),
        GWEI("gwei", 9),
        SZABO("szabo", 12),
        FINNEY("finney", 15),
        ETHER("ether", 18),
        KETHER("kether", 21),
        METHER("mether", 24),
        GETHER("gether", 27);

        private String name;
        private BigDecimal weiFactor;

        Unit(String name, int factor)
        {
            this.name = name;
            this.weiFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getWeiFactor() {
            return weiFactor;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Unit fromString(String name)
        {
            if (name != null) {
                for (Unit unit : Unit.values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return Unit.valueOf(name);
        }
    }
}
