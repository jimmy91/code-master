package utils.algorithm.greedy;

import java.util.*;

/**
 * @author Jimmy
 * 贪心算法的使用场景包括：
 *
 * 1.最小生成树：在一个图中，求一个包含所有节点的，边权值最小的生成树。
 * 2.最短路径：从一个源点出发，到达图中所有其他节点的最短路径。
 * 3.背包问题：选择若干个物品放入背包中，使得背包中物品总价值最大，且不超过背包的容量。
 * 4.活动安排问题：在一定时间内安排尽可能多的活动，使得活动之间不冲突。
 * 5.任务调度问题：将若干个任务分配给不同的机器，使得任务的完成时间最短。
 *
 */
public class GreedyDemo {

    public static void main(String[] args) {
        System.out.println("========  完成最多的任务  ========");
        System.out.println(maxTasks(new int[][]{{1,3},{2,3},{3,6},{3,4},{6,9},{4,6},{4,7}}));


        System.out.println("========  子符串拆解成多个不重复包含的子串  ========");
        System.out.println(maxSubString("aadbosafmlouwwjkkz"));
    }

    /**
     * 假设你有一个包含n个任务的任务列表，每个任务都有一个开始时间和结束时间，用一个二元组(start, end)表示。
     * 你需要从这个任务列表中选择一些任务来完成，但是由于任务之间可能有冲突，因此你不能在同一时间完成两个任务。
     * 你的任务是选择最多的任务，使得你能够顺利完成它们。请输出能完成的最大任务数。
     * 示例：
     * 输入：[(1,3),(2,4),(3,6)]
     * 输出：2
     * 解释：我们可以选择(1,3)和(3,6)这两个任务，它们之间没有冲突，能够顺利完成。
     *
     * @param tasks
     * @return
     */
    public static int maxTasks(int[][] tasks) {
        int n = tasks.length;
        Arrays.sort(tasks, Comparator.comparingInt(a -> a[1]));  //按照结束时间排序
        List<int[]> selectedTasks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (selectedTasks.isEmpty() || tasks[i][0] >= selectedTasks.get(selectedTasks.size()-1)[1]) {
                //和上一个已选任务不冲突，加入已选列表中
                selectedTasks.add(tasks[i]);
            }
        }
        StringJoiner sj = new StringJoiner(", ");
        selectedTasks.stream().map(Arrays::toString).forEach(sj::add);
        System.out.println(sj);
        return selectedTasks.size();
    }


    /**
     * 题目描述：给定一个长度为n的字符串s，现在想要将其分成尽量多的子串，要求每个子串中只包含一种字符。请你输出最多可以将s分成多少个子串。
     * 输入格式：输入一行，包含一个字符串s，字符串s长度<=1000。
     * 输出格式：输出一个整数，表示最多可以将s分成多少个子串。
     * @param s
     * @return
     */
    public static int maxSubString(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            // 记录每一个字符在字符串最最后出现的位置
            last[s.charAt(i) - 'a'] = i;
        }

        int start = 0, end = 0;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            // 遍历end之前的字符最后位置是不是都出现在end之前，不是end后移
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                list.add(s.substring(start, end + 1));
                start = end + 1;
            }
        }
        list.forEach(p -> System.out.print(p + "  "));
        return list.size();
    }





}
