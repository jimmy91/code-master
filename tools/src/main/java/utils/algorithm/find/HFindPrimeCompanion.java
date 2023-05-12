package utils.algorithm.find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * https://www.nowcoder.com/practice/b9eae162e02f4f928eac37d7699b352e?tpId=37&tqId=21251&rp=1&ru=/exam/oj/ta&qru=/exam/oj/ta&sourceUrl=%2Fexam%2Foj%2Fta%3FtpId%3D37&difficulty=5&judgeStatus=undefined&tags=&title=
 * HJ28 素数伴侣
 * @author Jimmy
 */
public class HFindPrimeCompanion {

    /**
     * 描述
     * 题目描述
     * 若两个正整数的和为素数，则这两个正整数称之为“素数伴侣”，如2和5、6和13，它们能应用于通信加密。
     * 现在密码学会请你设计一个程序，从已有的 N （ N 为偶数）个正整数中挑选出若干对组成“素数伴侣”，挑选方案多种多样，
     * 例如有4个正整数：2，5，6，13，如果将5和6分为一组中只能得到一组“素数伴侣”，而将2和5、6和13编组将得到两组“素数伴侣”，
     * 能组成“素数伴侣”最多的方案称为“最佳方案”，当然密码学会希望你寻找出“最佳方案”。
     * 输入:
     * 有一个正偶数 n ，表示待挑选的自然数的个数。后面给出 n 个具体的数字。
     * 输出:
     * 输出一个整数 K ，表示你求得的“最佳方案”组成“素数伴侣”的对数。
     *
     * 输出描述：
     * 求得的“最佳方案”组成“素数伴侣”的对数。
     */

    /**
     * 解题思路：
     * 这道题是一个典型的图论问题，可以使用二分图最大匹配算法求解。具体过程如下：
     * 1. 将所有奇数和偶数分别建立两个点集，通过判断两个点所代表的数是否为素数，将其连通。
     * 2. 对于每一个偶数点 u，在其能够到达的所有奇数点 v 中任选一个进行匹配，即 u->v。
     * 3. 最后统计匹配的对数即可。对
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] nums = new int[n];

        // 读入所有数字
        for (int i = 0; i < n; i++) {
            nums[i] = scanner.nextInt();
        }

        // 建立二分图，奇数点和偶数点分别为两个点集
        List<Integer>[] g = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
            if (nums[i] % 2 == 0) {
                for (int j = 0; j < n; j++) {
                    if (nums[j] % 2 == 1 && isPrime(nums[i] + nums[j])) {
                        g[i].add(j);
                    }
                }
            }
        }

        int[] match = new int[n];
        Arrays.fill(match, -1);
        int ans = 0;

        // 二分图最大匹配
        for (int u = 0; u < n; u++) {
            boolean[] vis = new boolean[n];
            if (find(u, g, match, vis)) {
                ans++;
            }
        }

        System.out.println(ans);
    }

    // 判断是否为素数
    private static boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        for (int i = 2; i * i <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    // DFS查找增广路
    private static boolean find(int u, List<Integer>[] g, int[] match, boolean[] vis) {
        for (int v : g[u]) {
            if (!vis[v]) {
                vis[v] = true;
                if (match[v] == -1 || find(match[v], g, match, vis)) {
                    match[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

}
