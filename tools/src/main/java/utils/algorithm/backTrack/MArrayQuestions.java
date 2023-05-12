package utils.algorithm.backTrack;

import java.util.*;

/**
 * @author Jimmy
 * 回溯算法常用于解决以下问题：
 *  1. 排列、组合、子集问题：如全排列、N皇后、组合总和等问题，这类问题需要枚举所有排列、组合或子集，回溯算法可以通过不同的选择来生成所有可能的结果。
 *  2. 搜索问题：如单词搜索、数独等问题，这类问题需要对所有可能的搜索路径进行遍历，回溯算法可以通过一步一步的试探来寻找路径。
 *  3. 优化问题：如旅行商问题、背包问题等，这类问题需要对所有可能的解进行搜索，并在过程中不断更新最优解，回溯算法可以通过剪枝操作来减少搜索空间，提高算法效率。
 *  4. 递归问题：如递归调用栈回退等问题，这类问题需要保存递归栈的状态，回溯算法可以通过保存状态来实现回溯操作。
 *  总之，回溯算法适用于需要枚举所有可能解的问题，通常复杂度为指数级别，因此需要结合剪枝等优化手段进行优化。
 */
public class MArrayQuestions {


    public static void main(String[] args) {
        // 能否跳跃到最后
        System.out.println("========  能否跳跃到最后，最大跳跃数不在大于当值数  ========");
        System.out.println(canJump(new int[]{2,3,0,0,1,3,0,4}));
        System.out.println(canJump1(new int[]{2,3,1,1,1,3,0,4}));
        System.out.println(canJump0(new int[]{2,3,0,0,0,3,0,4}));
        // 输出所有长度为4、和为target的数据
        System.out.println("========  输出所有长度为4、和为target的数据  ========");
        System.out.println(fourSum(new int[]{1, 5, 0, -1, -2, 0, -2, -3}, 0));
        // 输出所有组合(可重复出现)和为8的所有数组
        System.out.println("========  输出所有组合(可重复出现)和为8的所有数组  ========");
        System.out.println(combinationSum(new int[]{2, 3, 1, 5}, 8));

        // 输出某个字符串在数组中能否连成一条不交叉的线
        System.out.println("========  输出某个字符串在数组中能否连成一条不交叉的线  ========");
        char[][] input =
                {{'A','B','C','E'},
                 {'S','F','C','S'},
                 {'A','D','E','E'}};
        findWord(input, "ASABFDECCESE");

        // 给定一个没有重复数字的序列，返回其所有可能的全排列。
        System.out.println("======== 返数组其所有可能的全排列  ========");
        System.out.println(permute(new int[]{2,4,6}));



    }

    /**
     * 给定一个非负整数数组  nums ，你最初位于数组的第一个位置。
     * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
     * 判断你是否能够到达最后一个位置。
     * <p>
     * **示例 1:**
     * 输入:  [2,3,1,1,4]
     * 输出:  true
     * <p>
     * 从下标为  0  跳到下标为  1  的位置，跳  1  步，然后跳  3  步到达数组的最后一个位置。
     * **示例 2:**
     * 输入:  [3,2,1,0,4]
     * 输出:  false
     */
    public static boolean canJump(int[] nums) {
        // 数组中不包含0，那一定可到达
        if(!Arrays.asList(Arrays.stream(nums).boxed().toArray(Integer[]::new)).contains(0)){
            return true;
        }
        int lastPosition = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (i + nums[i] >= lastPosition) {
                lastPosition = i;
            }
        }
        return lastPosition == 0;
    }

    /**
     * 解题思路：
     * 这是一道贪心算法的经典问题。因为我们并不关心中间的具体路径，而只需要知道是否能够到达最后一个位置，
     * 因此可以采用贪心策略：每次计算出当前位置能够到达的最远位置，和已知的最远位置进行比较，如果当前的最远位置已经大于等于最后一个位置，说明可以到达终点，
     * 否则继续向前遍历，直到无法到达为止。
     * @param nums
     * @return
     */
    public static boolean canJump0(int[] nums) {
        int n = nums.length;
        int max_pos = 0;
        for (int i = 0; i < n; i++) {
            if (i <= max_pos) { //当前位置能到达
                max_pos = Math.max(max_pos, i + nums[i]); //更新最远能到达的位置
                if (max_pos >= n-1) { //可以到达终点
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 首先初始化dp数组，将起点的步数设为0，其它位置的步数设为正无穷大，表示一开始所有位置都不可达。
     * 然后从第二个位置开始遍历，对于每个位置i，都遍历它前面的所有位置j，如果j可以直接到达i，
     * 那么就更新到达i位置的最小步数，并记录j为i的前置节点。最后，从终点开始回溯，通过不断寻找前置节点来还原最短路径。
     * @param nums
     * @return  局部最优是满足全局最优解
     */
    public static List<Integer> canJump1(int[] nums) {
        if(canJump(nums)){
            int n = nums.length;
            int[] dp = new int[n]; //记录从起点到每个位置所需的最小步数
            int[] pre = new int[n]; // 记录每个位置的前置节点
            Arrays.fill(dp, Integer.MAX_VALUE);
            Arrays.fill(pre, -1);
            dp[0] = 0;
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (j + nums[j] >= i) { // j位置可以到达i位置
                        if (dp[j]+1 < dp[i]) { // 更新到达i位置所需的最小步数
                            dp[i] = dp[j] + 1;
                            pre[i] = j; // 更新i位置的前置节点为j
                        }
                    }
                }
            }
            List<Integer> res = new ArrayList<>();
            int cur = n-1;
            while (cur != 0) { // 从终点开始回溯
                res.add(cur);
                cur = pre[cur]; // 回溯到前置节点
            }
            res.add(0);
            Collections.reverse(res); //倒序输出位置
            return res;
        }
        return null;
    }


    /**
     * 给定一个包含  n  个整数的数组  nums  和一个目标值  target ，
     * 判断  nums  中是否存在四个元素  a，b，c  和  d ，使得  a + b + c + d  的值与  target  相等？
     * 找出所有满足条件且不重复的四元组。
     * **示例 1:**
     * 输入： nums = [1,0,-1,0,-2,2], target = 0
     * 输出： [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
     * **示例 2:**
     * 输入： nums = [], target = 0
     * 输出： []
     */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);  // 先将数组排序, 这样有利于去除重复数组结果
        List<List<Integer>> res = new ArrayList<>();
        int n = nums.length;

        // 递归查找可能的四元组
        findNsum(nums, 0, n - 1, target, 4, new ArrayList<>(), res);
        return res;
    }

    public static void findNsum(int[] nums, int left, int right, int target, int N, List<Integer> temp, List<List<Integer>> res) {
        for (int i = left; i <= right; i++) {
            if (i == left || (i > left && nums[i] != nums[i - 1])) {
                if (N == 1) {
                    if (nums[i] == target) {
                        temp.add(nums[i]);
                        res.add(new ArrayList<>(temp));
                        temp.remove(temp.size() - 1);  // 回溯
                        return;
                    }
                } else {
                    temp.add(nums[i]);
                    findNsum(nums, i + 1, right, target - nums[i], N - 1, temp, res);
                    temp.remove(temp.size() - 1);  // 回溯
                }
            }
        }
    }

    /**
     * 给定一个大于0正整数数组 nums 和一个目标值 target，请找出数组中所有的和为 target 的组合，其中的数字可以重复出现。
     * <p>
     * 示例：
     * 输入：nums = [2,3,6,7], target = 7
     * 输出：[[2,2,3], [7]]
     * 输入：nums = [2,3,5], target = 8
     * 输出：[[2,2,2,2], [2,3,3], [3,5]]
     */
    public static List<List<Integer>> combinationSum(int[] nums, int target) {
        Arrays.sort(nums);  // 先将数组排序, 这样有利于去除重复数组结果
        List<List<Integer>> res = new ArrayList<>();
        int n = nums.length;

        // 递归查找可能的四元组
        findsum(nums, 0, n - 1, target, new ArrayList<>(), res);
        return res;
    }

    public static void findsum(int[] nums, int left, int right, int target, List<Integer> temp, List<List<Integer>> res) {
        for (int i = left; i <= right; i++) {
            if (nums[i] == target) {
                temp.add(nums[i]);
                res.add(new ArrayList<>(temp));
                temp.remove(temp.size() - 1);  // 回溯
                return;
            } else {
                temp.add(nums[i]);
                if (target - 2 * nums[i] >= 0) {
                    // 重复本身
                    findsum(nums, i, right, target - nums[i], temp, res);
                } else {
                    findsum(nums, i + 1, right, target - nums[i], temp, res);
                }
                temp.remove(temp.size() - 1);  // 回溯
            }
        }
    }


    /**
     * 单词搜索
     * 给定一个二维网格和一个单词，找出该单词是否存在于网格中。
     * 通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。
     * @param input
     * @param word
     * @return
     */
    public static boolean findWord(char[][] input, String word){
        int index = 0;
        boolean[][] used = new boolean[input.length][input[0].length];
        for(int i = 0 ; i < input.length; i++){
            for(int j = 0 ; j < input[i].length; j++){
                if(findMatch(input, used, i, j, index, word)){
                    System.out.println("===成功匹配===:"+word);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    static int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static Boolean findMatch(char[][] input, boolean[][] used , int i, int j, int index, String word) {
        if(used[i][j] || !Objects.equals(input[i][j], word.charAt(index))) {
            return Boolean.FALSE;
        }
        used[i][j] = Boolean.TRUE;
        index++;
        // 退出条件，达到最后一个字符的匹配成功
        if(Objects.equals(index, word.length())){
            return Boolean.TRUE;
        }

        for (int[] direction : directions) {
            int ni = i + direction[0], nj = j + direction[1];
            if (ni >= 0 && ni < input.length && nj >= 0 && nj < input[0].length) {
                if (findMatch(input, used, ni, nj, index, word)) {
                    return Boolean.TRUE;
                }
                used[i][j] = Boolean.FALSE; // 回溯
            }
        }

       /* // 往右
        if(j + 1 < input[i].length){
           if(findMatch(input, used, i, ++j, index, word)){
               return Boolean.TRUE;
           }
           used[i][j] = Boolean.FALSE; // 回溯
           j--; // 回溯
        }
        // 往下
        if(i + 1 < input.length){
            if(findMatch(input, used, ++i, j, index, word)){
                return Boolean.TRUE;
            }
            used[i][j] = Boolean.FALSE;
            i--;
        }
        // 往左
        if(j - 1 >= 0){
            if(findMatch(input, used, i, --j, index, word)){
                return Boolean.TRUE;
            }
            used[i][j] = Boolean.FALSE;
            j++;
        }
        // 往上
        if(i - 1 >= 0){
            if(findMatch(input, used, --i, j, index, word)){
                return Boolean.TRUE;
            }
            used[i][j] = Boolean.FALSE;
            i++;
        }*/
        return Boolean.FALSE;
    }

    /**
     * 给定一个没有重复数字的序列，返回其所有可能的全排列。
     * @param nums
     * @return
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> permutation = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, visited, permutation, res);
        return res;
    }

    private static void backtrack(int[] nums, boolean[] visited, List<Integer> permutation, List<List<Integer>> res) {
        if (permutation.size() == nums.length) {
            res.add(new ArrayList<>(permutation));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                permutation.add(nums[i]);
                backtrack(nums, visited, permutation, res);
                permutation.remove(permutation.size() - 1);
                visited[i] = false;
            }
        }
    }



}
