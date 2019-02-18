package com.viking.vikingdemo;


public class aa {


    /**
     * 元宵节猜谜活动
     */
    static public void getRiddle() {
        String[] arr = new String[] { "H", "C", "E", "I", "T", "N", "U", "B", "A", "Y", "M" };
        int[] index = new int[] { 4, 6, 8, 5, 9, 6, 8, 5, 10, 2, 3, 10, 8, 5 };
        StringBuilder str = new StringBuilder();
        for (int i : index) {
            str.append(arr[i]);
        }
        System.out.println("谜底：" + str);
    }

}
