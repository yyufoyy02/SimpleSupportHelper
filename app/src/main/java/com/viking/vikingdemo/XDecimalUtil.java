package com.viking.vikingdemo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数值处理
 *
 * @author Administrator
 */
public class XDecimalUtil {
    private XDecimalUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 保留2位小数小数
     *
     * @param decimal
     * @return
     */
    public static String fractionDigits(double decimal) {

        return fractionDigits(decimal, 2);

    }

    /**
     * 保留指定位数小数
     *
     * @param decimal
     * @return
     */
    public static String fractionDigits(double decimal, int num) {

        // return String.format("%." + num + "f", BigDecimal.valueOf(decimal));
        NumberFormat mDecimalFormat = DecimalFormat.getInstance();
        mDecimalFormat.setMaximumFractionDigits(num);
        mDecimalFormat.setMinimumFractionDigits(num);
        return mDecimalFormat.format(decimal).replace(",", "");
    }

    /**
     * 最多两位小数
     *
     * @param num
     * @return
     */
    public static String priceString(double num) {
        NumberFormat decimalFormat = new DecimalFormat("#.##");


        return decimalFormat.format(num);
    }


}
