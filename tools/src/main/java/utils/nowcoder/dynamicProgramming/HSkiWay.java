package utils.nowcoder.dynamicProgramming;

import java.util.Scanner;

/**
 * https://www.nowcoder.com/practice/36d613e0d7c84a9ba3af3ab0047a35e0?tpId=230&rp=1&ru=%2Fexam%2Foj%2Fta&qru=%2Fexam%2Foj%2Fta&sourceUrl=%2Fexam%2Foj%2Fta%3FtpId%3D37&difficulty=4&judgeStatus=&tags=&title=&gioEnter=menu
 * DP18 滑雪
 *
 * @author Jimmy
 **/
public class HSkiWay {

    /**
     * 描述
     * 给定一个
     * n×m  的矩阵，矩阵中的数字表示滑雪场各个区域的高度，你可以选择从任意一个区域出发，并滑向任意一个周边的高度严格更低的区域（周边的定义是上下左右相邻的区域）。
     * 请问整个滑雪场中最长的滑道有多长？(滑道的定义是从一个点出发的一条高度递减的路线）。
     * (本题和矩阵最长递增路径类似，该题是当年NOIP的一道经典题)
     * <p>
     * 输入描述：
     * 第一行输入两个正整数 n 和 m 表示矩阵的长宽。
     * 后续 n 行输入中每行有 m 个正整数，表示矩阵的各个元素大小。
     * <p>
     * 输出描述：
     * 输出最长递减路线。
     */

    /**
     * 算法思路：
     *  这道题目可以采用深度优先搜索的方法。实现步骤如下：
     *  Step 1：遍历矩阵，找到高度最高的点，以该点为起点进行搜索。
     *  Step 2：对于每个待搜索的点，枚举它周围4个点，如果它们的高度比当前点低，就以它们为起点继续搜索。
     *  Step 3：统计搜索路径长度，取最大值。
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][] heights = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                heights[i][j] = scanner.nextInt();
            }
        }
        int maxLen = 0;
        boolean[][] visited = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!visited[i][j]) {
                    int len = dfs(heights, visited, i, j);
                    maxLen = Math.max(maxLen, len);
                }
            }
        }
        System.out.println(maxLen);
    }

    private static int dfs(int[][] heights, boolean[][] visited, int i, int j) {
        visited[i][j] = true;
        int len = 1;
        // 四种移动方向坐标变化(上、右、下、左)
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        for (int k = 0; k < 4; k++) {
            int x = i + dx[k];
            int y = j + dy[k];
            if (x >= 0 && x < heights.length && y >= 0 && y < heights[0].length && !visited[x][y]
                    && heights[x][y] < heights[i][j]) {
                int subLen = dfs(heights, visited, x, y) + 1;
                len = Math.max(len, subLen);
            }
        }
        visited[i][j] = false;
        return len;
    }

}
