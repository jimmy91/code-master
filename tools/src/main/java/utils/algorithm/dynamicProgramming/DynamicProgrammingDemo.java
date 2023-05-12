package utils.algorithm.dynamicProgramming;

import java.util.*;

/**
 * @author Jimmy
 * 动态规则
 */
public class DynamicProgrammingDemo {


    public static void main(String[] args) {
        // 实现最大子数组和
        System.out.println("========  最大子数组和  ========");
        System.out.println(maxSubArraySum(new int[]{2, -4, 5, -3, 6, -8, 4, 1, -2, 6, -2, -1, 2}));
        System.out.println(maxSubArraySum(new int[]{-5, -3, -6, -8, -4, -10, -2, -6, -9, -16, -3}));

        // 最长回文子串
        System.out.println("========  最长回文子串  ========");
        System.out.println(longestPalindrome("abcbafghgfabccba"));
        System.out.println(longestPalindrome("abcbafghhgfabccba"));

        // 回文子串组合
        System.out.println("========  所有回文子串组合(连续子串)  ========");
        findAllSubstrings("bccbab");

        System.out.println("========  完全平方数,任何一个整数都可以拆解成几个完全平方数之和，求最少拆解方式  ========");
        System.out.println(Arrays.toString(getSquareSumArray(12)));

        // 青蛙走法
        System.out.println("========  青蛙走法，从网络的左上角走到右下角，有多少种走法(仅可向右、向下) ========");
        System.out.println(uniquePaths(3, 3));

        // 爬楼问题
        System.out.println("========  爬楼问题，可以爬1步或2步，到达N层有多少种走法  ========");
        System.out.println(climbStairs(4));

        System.out.println("========  最长上升子序列的长度  ========");
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}));

        System.out.println("========  滑雪问题，输出滑雪路径 ========");
        int[][] input =
                {{6,9,11,12},
                 {4,18,14,10},
                 {1,16,15,4}};
        ski(input).forEach(p -> System.out.print(p + " -> "));

    }

    /**
     * 实现最大子数组和
     * <p>
     * 这里使用了一种常见的动态规划算法，即通过计算以当前数字结尾的最大子数组和来得到全局最大子数组和。具体流程如下：
     * - 最大子数组和的初始值为数组中的第一个数字。
     * - 定义一个变量 currentSum，表示以当前数字结尾的最大子数组和，初始值也为数组中的第一个数字。
     * - 从数组的第二个数字开始遍历，计算以当前数字结尾的最大子数组和：如果当前数字加上 currentSum 大于当前数字本身，则更新 currentSum 为当前数字加上 currentSum；否则，以当前数字为起点重新计算 currentSum。
     * - 每次计算出 currentSum 后，将其与全局最大子数组和 maxSum 进行比较，取较大值作为新的全局最大子数组和。
     * - 遍历结束后，全局最大子数组和即为所求。
     *
     * @param nums
     * @return
     */
    public static int maxSubArraySum(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        int currentStart = 0;
        int start = 0;
        int end = 0;

        for (int i = 1; i < nums.length; i++) {
            if (currentSum + nums[i] < nums[i]) {
                // 前面和还不如当前值大，直接将开始位置移到当前位置
                start = i;
            }
            currentSum = Math.max(currentSum + nums[i], nums[i]);

            if (currentSum > maxSum) {
                // 当前和变大了，end才进行变更位置
                currentStart = start;
                end = i;
            }
            maxSum = Math.max(maxSum, currentSum);
        }
        System.out.println("最大子数据和：" + Arrays.toString(Arrays.copyOfRange(nums, currentStart, end + 1)));
        return maxSum;
    }


    /**
     * 最长回文子串
     * 1. 定义一个二维布尔型数组 dp，dp[i][j] 表示从 i 到 j 的子串是否是回文子串。
     * 2. 初始化 dp 数组，对于所有的 i = j，dp[i][j] 都为 true。同时，对于相邻的两个字符 i 和 i+1，如果 s[i]=s[i+1]，则 dp[i][i+1] 为 true。
     * 3. 通过递推公式 dp[i][j] = dp[i+1][j-1] && s[i]==s[j]，计算出所有长度大于等于 3 的子串是否为回文子串。
     * 递推时，需要先计算出长度更短的子串，因此循环顺序为枚举子串长度 len，然后在子串长度确定情况下，枚举子串的起始位置 i。
     * 4. 最长回文子串的长度和起点可以通过 dp 数组的值计算得到。
     *
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        boolean[][] dp = new boolean[s.length()][s.length()];
        int start = 0;
        int maxLength = 1;
        // 初始化
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = true;
            if (i < s.length() - 1 && s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                start = i;
                maxLength = 2;
            }
        }
        // 递推
        for (int len = 3; len <= s.length(); len++) {
            for (int i = 0; i < s.length() - len + 1; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    if (len > maxLength) {
                        maxLength = len;
                        start = i;
                    }
                }
            }
        }
        return s.substring(start, start + maxLength);
    }

    /**
     * 最长回文子串
     * <p>
     * 使用中心扩展法。这种算法的基本思想是，从字符串的每个字符和每两个相邻字符之间出发，向外扩展，直到不能扩展为止。具体步骤如下：
     * 1. 从字符串的第一个字符开始，以每个字符为中心向外扩展，找到以该字符为中心的最长回文子串，记录下该子串的长度和起始位置。
     * 2. 对于每两个相邻字符之间的位置，以这两个字符为中心向外扩展，找到以这两个字符为中心的最长回文子串，记录下该子串的长度和起始位置。
     * 3. 比较所有以字符和相邻字符为中心的回文子串，取最长的一个。
     *
     * @param s
     * @return
     */
    public static String longestPalindrome02(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        int start = 0, end = 0;
        // 以每个字符为中心扩展
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    /**
     * 任何一个正整数都可以分解成几个完全平方数之和，给定一个正整数，输出长度最小和数组
     * 什么是完全平方数，例如 1, 4, 9, 16, 25, … 任何正整数可以表示为完全平方数之和，
     * 例如：
     * 7 = 4 + 1 + 1 + 1
     * 12 = 4 + 4 + 4 / 而不是 9 + 1 + 1 + 1
     * 状态转移方程：  dp[i] = min(dp[i], dp[i - j * j] + 1) ，其中 j * j <= i
     */
    public static int[] getSquareSumArray(int n) {
        // dp[n]  就是正整数n最少可以由多少个完全平方数组成
        // res[n] 表示满足最少完全平方数时的最后一个平方数
        int[] dp = new int[n + 1];
        int[] res = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE); // 初始化为最大值
        dp[0] = 0; // 0可以由0个完全平方数组成
        // 计算dp数组
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                if (dp[i - j * j] + 1 < dp[i]) {
                    res[i] = j * j; // 记录最后一个完全平方数
                }
                dp[i] = Math.min(dp[i - j * j] + 1, dp[i]);
            }
        }
        // 回溯查找完全平方数序列
        int[] result = new int[dp[n]];
        int index = dp[n] - 1;
        int m = n;
        while (m > 0) {
            result[index--] = res[m];
            m -= res[m];
        }
        return result;
    }


    /**
     * 全部由小写字母组成的字符串 s ，输出所有回文子串(连续子串)
     * 请你返回一个列表，其中每个元素表示 s 中的非空回文子序列。列表中的回文子序列应该按照字典序排列。
     * @param s
     * @return
     */
    public static List<String> findAllSubstrings(String s) {
        Set<String> set = new HashSet<>();
        boolean[][] memo;
        int n = s.length();
        memo = new boolean[n][n];

        // 枚举子串的左右端点，判断是否为回文串，memo[i][j] = true 表示从i->j为回文串
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    memo[i][j] = (j - i <= 2) || memo[i + 1][j - 1];
                }
            }
        }
        // 输出所有子回文串
        System.out.println("输出所有子回文串");
        for(int i = 0; i < memo.length; i++){
            for (int j = 0; j < memo.length; j++){
                if(memo[i][j]){
                    System.out.print(s.substring(i, j+1) + " | ");
                }
            }
        }
        System.out.println();
        return null;

    }

    /**
     * 假设有一只青蛙，它位于一个大小为 m×n 的网格的左上角。
     * 青蛙每次可以向下走一步或向右走一步，它要去网格的右下角。请问，青蛙通过这个网格，有多少种走法？
     * 可以使用二维数组 dp[i][j] 来表示到达点 (i, j) 的路径总数，则有：
     *                  dp[i][j] = dp[i-1][j] + dp[i][j-1]
     * @return
     */
    public static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        // 第一行和第一列均只有一种方式到达
        Arrays.fill(dp[0], 1);
        for (int i = 0; i < n; i++) {
            dp[i][0] = 1;
        }
        for(int i = 1 ; i < m ; i++){
            for(int j = 1 ; j < n ; j++){
                dp[i][j] = dp[i-1][j] + dp[i][j-1]; // 状态转移方程
            }
        }
        return dp[m-1][n-1];
    }

    /**
     * 假设你正在爬楼梯，需要n步才能爬到楼顶，每次你可以爬1步或2步。编写一个函数来计算到达楼顶的不同方式数量。
     * @param n
     * @return
     */
    public static int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int[] dp = new int[n+1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }

    /**
     * 给定一个无序的整数数组，找到其中最长上升子序列的长度。
     * 示例:
     * 输入: [10,9,2,5,3,7,101,18]
     * 输出: 4  解释: 最长的上升子序列是 [2,3,7,101]，长度为 4。
     * @param nums
     * @return
     */
    public static int lengthOfLIS(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        //  dp[i] 表示以第 i 个数字为结尾的最长上升子序列的长度
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        int maxIdx = 0;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                // 对于第 i 个数字，它前面的所有比它小的数字都可以作为它的上升子序列的一部分
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1); // 状态转移方程
                }
            }
            if (dp[i] > maxLen) {
                maxIdx = i;
            }
            maxLen = Math.max(maxLen, dp[i]);
        }

        // 构造最长上升子序列
        List<Integer> res = new ArrayList<>();
        res.add(nums[maxIdx]);
        int len = maxLen - 1;
        for (int i = maxIdx - 1; i >= 0; i--) {
            if (nums[i] < nums[maxIdx] && dp[i] == len) {
                res.add(nums[i]);
                len--;
            }
        }
        Collections.reverse(res);
        System.out.println("最长上升子序列：" + res);

        return maxLen;
    }


    /**
     * 给定一个非负整数数组 nums，你最初位于数组的第一个位置，数组中的每个元素代表你在该位置可以跳跃的最大长度。你的目标是使用最少的跳跃次数到达数组的最后一个位置。假设你总是可以到达数组的最后一个位置。
     * 例如：给定 nums = [2,3,1,1,4]，最少需要跳跃的次数为 2。
     * @param nums
     * @return
     */
    public int jump(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE-1);  //初始化dp数组，除了第一个位置，其余位置初始化为正无穷
        dp[0] = 0;  //初始化第一个位置的dp值为0
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j-i <= nums[i] && j < n; j++) {
                //i位置能够到达到j位置。 对于每个能够到达的位置j，更新其dp值
                dp[j] = Math.min(dp[j], dp[i]+1);
            }
        }
        return dp[n-1];
    }

    // 使用贪心算法

    /**
     * 具体实现如下：
     * - 定义变量end和maxPos，初始化值均为0。其中，end表示当前能够到达的最远位置，maxPos表示所有能够到达的位置中的最大值。
     * - 遍历数组nums：
     *   - 对于每个位置i，计算当前位置能够到达的最远位置curPos，即i+nums[i]。
     *   - 如果curPos大于maxPos，更新maxPos为curPos。
     *   - 如果i等于end，表示当前位置已经到达了能够到达的最远位置，因此需要进行一次跳跃，将end更新为maxPos，并将跳跃次数加1。
     *
     * 最后得到的跳跃次数即为最少需要跳跃的次数。
     * @param nums
     * @return
     */
    public int jump02(int[] nums) {
        int end = 0, maxPos = 0, jumps = 0;
        for (int i = 0; i < nums.length-1; i++) {  //最后一步不需要跳跃，因此遍历到倒数第二个位置即可
            maxPos = Math.max(maxPos, i+nums[i]);  //计算当前位置能够到达的最远位置
            if (i == end) {  //如果当前位置已经到达了能够到达的最远位置，需要进行一次跳跃
                end = maxPos;
                jumps++;
            }
        }
        return jumps;
    }


    /**
     * 给定一个
     * n×m  的矩阵，矩阵中的数字表示滑雪场各个区域的高度，你可以选择从任意一个区域出发，并滑向任意一个周边的高度严格更低的区域（周边的定义是上下左右相邻的区域）。
     * 请问整个滑雪场中最长的滑道有多长？(滑道的定义是从一个点出发的一条高度递减的路线）。
     * @param skis
     * @return
     */
    public static List<Integer> ski(int[][] skis) {
        int m = skis.length;
        int n = skis[0].length;
        boolean[][] used = new boolean[m][n];
        // 用于记录每个点的前驱节点, 以行数第几个数
        int[][] next = new int[m][n];
        int startIndex = 0;
        int maxLength = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int[][] curNext = new int[m][n];

                int len = skiNode(skis, i, j, used, curNext);
                if( len > maxLength){
                    startIndex = i * n + j + 1;
                    for (int i1 = 0; i1 < curNext.length; i1++) {
                        for (int j1 = 0; j1 < curNext[0].length; j1++) {
                            next[i1][j1] = curNext[i1][j1];
                        }
                    }
                }
                maxLength = Math.max(len, maxLength);
            }
        }

        // 输出滑雪路径
        List<Integer> path = new ArrayList<>();
        int index = startIndex;
        while (index > 0){
            int startNode = skis[index/n][index%n - 1];
            path.add(startNode);
            index = next[index/n][index%n - 1];
        }

        System.out.println("====最长路径长度：" +maxLength );
        return path;
    }

    static int[][] paths = new int[][]{{0,1},{0,-1},{1,0},{-1,0}} ;
    private static int skiNode(int[][] skis, int i, int j, boolean[][] used, int[][] next) {
        used[i][j] = Boolean.TRUE;
        // 以ij为起点的最大路径长度
        int maxLength = 1;
        for(int k1 = 0; k1 < paths.length; k1++){
            int ni = i + paths[k1][0];
            int nj = j + paths[k1][1];
            if( !(ni < skis.length && nj < skis[0].length && ni >= 0 && nj >= 0 )){
                continue;
            }
            if(!used[ni][nj] && skis[i][j] > skis[ni][nj]){
                int len = skiNode(skis, ni, nj, used, next);
                if(len + 1 > maxLength){
                    next[i][j] = ni*skis[0].length + nj  + 1;
                }
                maxLength = Math.max(maxLength, len + 1);
            }
        }
        used[i][j] = Boolean.FALSE; // 回溯
        return maxLength;
    }


}
