package utils.algorithm.bloomfilter;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Random;

public class BloomFilter {

    private static final int DEFAULT_SIZE = 2 << 24;  // 布隆过滤器的比特长度，默认为 2 的 24 次方
    private static final int[] SEEDS = new int[]{3, 5, 7, 11, 13, 31, 37, 61};  // 哈希函数种子，可以使用多个不同的值

    private static BitSet bits = new BitSet(DEFAULT_SIZE);
    private static Random random = new Random();

    private static void add(String value) {
        for (int seed : SEEDS) {
            bits.set(hash(value, seed), true);
        }
    }

    private static boolean contains(String value) {
        if (value == null) {
            return false;
        }
        for (int seed : SEEDS) {
            if (!bits.get(hash(value, seed))) {
                return false;
            }
        }
        return true;
    }

    private static int hash(String value, int seed) {
        int result = 0;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            result = seed * result + value.charAt(i);
        }
        return (DEFAULT_SIZE - 1) & result;
    }

    private static int size = 1000000;//预计要插入多少数据
    public static void main(String[] args) {
        // 插入数据
        for (int i = 0; i < size; i++) {
            BloomFilter.add(i+"");
        }

        // 实际存在的过滤
        int fppCount1 = 0;
        for (int i = 0; i < 1000000; i++) {
            boolean mightContain = BloomFilter.contains(i+"");
            if (!mightContain) {
                fppCount1++;
                System.out.println("误判了：" + i);
            }
        }

        System.out.println("总共误判了" + fppCount1 + "次");

        // 实际不存在的过滤
        int fppCount2 = 0;
        for (int i = 1000000; i < 2000000; i++) {
            boolean mightContain = BloomFilter.contains(i+"");
            if (mightContain) {
                fppCount2++;
            }
        }
        BigDecimal f = new BigDecimal(fppCount2);
        BigDecimal s = new BigDecimal(size);

        System.out.println("总共误判了" + fppCount2 + "次，ffp:" + f.divide(s).toString());

    }
}
