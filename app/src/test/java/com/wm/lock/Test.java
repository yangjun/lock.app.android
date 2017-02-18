package com.wm.lock;

import com.wm.lock.core.utils.PinyinUtils;

import org.junit.Before;

import java.util.Comparator;
import java.util.Date;

import static cn.finalteam.toolsfinal.DateUtils.date;

public class Test {

    private final static int[] areaCode = {1601, 1637, 1833, 2078, 2274,
            2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};

    private final static String[] letters = {"a", "b", "c", "d", "e",
            "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "w", "x", "y", "z"};

    @Before
    public void setup() {
        System.out.println(".......setup......");
    }

    @org.junit.Test
    public void test() {
        System.out.print(new Date().getTime());
//        Calendar c = Calendar.getInstance();
//        c.set(2000, 1, 1);
//        Date date1 = c.getTime();
//        Date date2 = new Date();
//        System.out.println("date1: " + date1.getTime());
//        System.out.println("date2: " + date2.getTime());

        // 取第一个字符
        String str = "您好";
        str = str.substring(0, 1);
        str = PinyinUtils.getPinYinHeadChar(str);
        System.out.println(":::" + str);


//        // 排序
//        final String[] strs = new String[] {
//                "3", "1", "c", "a", "z", "e", "9", "a", "1", "k"
//        };
//        final List<String> list = Arrays.asList(strs);
//        Collections.sort(list, new CharComparator());
//        for (String item : list) {
//            System.out.println(":::" + item);
//        }

    }

    static class CharComparator implements Comparator<String> {

        @Override
        public int compare(String s, String t1) {
            int a1 = (int) s.charAt(0);
            int b1 = (int) t1.charAt(0);
            if (a1 > b1) {
                return 1;
            }
            if (b1 > a1) {
                return -1;
            }
            return 0;
        }
    }

}
