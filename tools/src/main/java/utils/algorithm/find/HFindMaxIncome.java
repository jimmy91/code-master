package utils.algorithm.find;

import java.util.Scanner;

/**
 * @author Jimmy
 * https://www.nowcoder.com/practice/ba3c096c19e04afbbbd59250e909ac68?tpId=230&tqId=2364648&ru=%2Fpractice%2F36d613e0d7c84a9ba3af3ab0047a35e0&qru=%2Fta%2Fdynamic-programming%2Fquestion-ranking&sourceUrl=%2Fexam%2Foj%2Fta%3FtpId%3D37
 * DP33 买卖股票的最好时机
 */
public class HFindMaxIncome {

    /**
     * 假设你有一个数组 prices，长度为 其中 prices[i]是某只股票在第i天的价格，请根据这个价格数组，返回买卖股票能获得的最大收益
     * 1. 你最多可以对该股票有 k笔交易操作，一笔交易代表着一次买入与一次卖出，但是再次购买前必须卖出之前的股票
     * 2. 如果不能获取收益，请返回0
     * 3. 假设买入卖出均无手续费
     * 输入描述：
     * 第一行输入一个正整数 n 和一个正整数 k。表示数组 prices 的长度和 交易笔数
     * 第二行输入 n 个正整数表示数组的所有元素值。
     */

    /**
     *  我们使用两个嵌套的循环，分别枚举交易次数i和天数j。在内层循环中，我们需要维护一个变量maxDiff，用来记录第j天进行第i次交易时的最大差价，
     *  即在第j天买入并在之后的某一天卖出所能获得的最大利润。
     *  具体来说，我们需要计算出第j天进行i次交易时的最大利润dp[j][i]，它要么是第j天不进行交易时的最大收益dp[j-1][i]，
     *  要么是第j天进行一次交易的最大收益prices[j] + maxDiff（其中maxDiff是从前一天的i-1次交易中获得的最大差价）。
     *  在计算完dp[j][i]之后，我们需要更新变量maxDiff，在第j天进行i次交易时，其最大差价为dp[j-1][i-1] - prices[j]，
     *  即在前一天进行了i-1次交易并在第j天买入所能获得的最大收益减去第j天的股票价格。
     *  最后，我们输出dp[n-1][k]即可，它表示在最后一天进行了k次交易所能获得的最大收益。
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        int[] prices = new int[n];
        for (int i = 0; i < n; i++) {
            prices[i] = in.nextInt();
        }
        int[][] dp = new int[n][k+1];  // 定义一个n行k+1列的二维整数数组dp，用来记录每天进行了i次交易时的最大收益
        for (int i = 1; i <= k; i++) {  // 外层循环，枚举交易次数i
            int maxDiff = -prices[0];  // 初始化maxDiff为-prices[0]，表示第0天进行了一次交易时的最大差价，即买入价为-prices[0]
            for (int j = 1; j < n; j++) {  // 内层循环，枚举天数j
                /**
                 * 具体来说，dp[j][i]表示第j天进行了i次交易时的最大收益。这个最大收益可以有两种情况：
                 *    (1) 在第j天不进行交易时的最大收益，即dp[j-1][i]。
                 *    (2) 在第j天进行一次交易的最大收益，即prices[j] + maxDiff。
                 * 我们需要取这两种情况的最大值作为dp[j][i]的值，即dp[j][i] = max(dp[j-1][i], prices[j] + maxDiff)。
                 */
                dp[j][i] = Math.max(dp[j-1][i], prices[j] + maxDiff);  // 状态转移方程：dp[j][i] = max(dp[j-1][i], prices[j] + maxDiff)
                /**
                 * 变量maxDiff，用来记录第j天进行第i次交易时的最大差价，即在第j天买入并在之后的某一天卖出所能获得的最大利润。
                 * 具体来说，我们需要使用这个maxDiff来计算在第j天进行一次交易的最大收益prices[j] + maxDiff。那么，如何计算maxDiff呢？
                 * 我们可以通过比较在前一天进行了i-1次交易并在第j天买入所能获得的最大收益dp[j-1][i-1]和在第j天卖出所能获得的最大收益prices[j]，来更新maxDiff的值。
                 * 具体来说，我们需要取这两者的差的最大值，即maxDiff = max(maxDiff, dp[j-1][i-1] - prices[j])。
                 * 这样，我们就可以通过maxDiff来计算在第j天进行一次交易的最大收益prices[j] + maxDiff，从而更新dp[j][i]的值。
                 */
                maxDiff = Math.max(maxDiff, dp[j-1][i-1] - prices[j]);  // 更新maxDiff：maxDiff = max(maxDiff, dp[j-1][i-1] - prices[j])
            }
        }
        System.out.println(dp[n-1][k]);  // 输出dp[n-1][k]，即在最后一天进行了k次交易所能获得的最大收益

    }

}
