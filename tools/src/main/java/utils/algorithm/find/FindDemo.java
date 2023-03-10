package utils.algorithm.find;

import java.util.*;

/**
 * @author Jimmy
 */
public class FindDemo {

    public static void main(String[] args) {
        List<Integer> lst = Arrays.asList(3,2,3,50,5,8,6,11,50,15,8,9,7,6,11,5,2,7,9);
        System.out.println(findSingleValue01(lst));
        System.out.println(findSingleValue02(lst));
        System.out.println(findSingleValue03(lst));
    }

    /**
     * 有一个很长的list列表，里中保存了大量成对出现的数值，其中只有一个不是成对出来的，请用最快的方式找出那个值，并写出java代码
     * @param values
     * @return
     */
    public static int findSingleValue01(List<Integer> values) {
        int result = 0;
        for (int value : values) {
            result ^= value;
        }
        return result;
    }


    public static int findSingleValue02(List<Integer> values) {
        Map<Integer, Integer> map = new HashMap<>(16);
        for (int value : values) {
            if(Objects.nonNull(map.putIfAbsent(value, value))){
                // 已经存在过，则删除
                map.remove(value);
            }
        }
        for (Map.Entry<Integer, Integer> e : map.entrySet()){
            return e.getValue();
        }
        return -1;
    }

    /**
     * 具体的实现方式是，定义一个长度为32的整型数组bits，表示每一位上出现的次数，
     * 遍历整个数组，对于每一个数值，将其每一位上的二进制数值加到bits数组的相应位置上。
     * 最后，遍历bits数组，将每一位的值模2得到的结果拼接起来，就是只出现一次的数值。
     * @param values
     * @return
     * 这个实现方式的时间复杂度为O(N)，空间复杂度为常量，可以解决空间复杂度高的问题。
     *
     * 这个解法的思路是遍历所有数字的每一位，将每一位出现的次数保存到一个长度为32的数组中，然后再次遍历数组的每一位，
     * 将出现次数为奇数的位的结果置为1，并使用按位或运算，最终得到单独出现的数字。
     */
    public static int findSingleValue03(List<Integer> values) {
        int[] bits = new int[32];
        for (int value : values) {
            for (int i = 0; i < 32; i++) {
                // 将数字右移i位，然后和1进行按位与运算，结果为1则表示该位为1，将该位出现次数加1
                bits[i] += (value >> i) & 1;
            }
        }
        int result = 0;
        for (int i = 0; i < 32; i++) {
            // 如果该位出现次数为奇数，则将该位的结果置为1，使用按位或运算，最终得到单独出现的数字
            result |= (bits[i] % 2) << i;
        }
        return result;
    }



}
