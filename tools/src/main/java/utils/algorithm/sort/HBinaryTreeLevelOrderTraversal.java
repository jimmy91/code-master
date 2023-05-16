package utils.algorithm.sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树的层序遍历是指从上到下、从左到右遍历二叉树的所有节点，按照节点所在的层次依次输出。层序遍历一般使用队列实现，具体步骤如下：
 * 1. 首先将根节点入队；
 * 2. 在队列不为空的情况下，依次取出队列中的节点，并将其左右子节点入队；
 * 3. 重复步骤2，直到队列为空。
 * @author Jimmy
 */
public class HBinaryTreeLevelOrderTraversal {

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        if (root == null) {
            return result;
        }
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.getVal());
                if (node.getLeft() != null) {
                    queue.offer(node.getLeft());
                }
                if (node.getRight() != null) {
                    queue.offer(node.getRight());
                }
            }
            result.add(level);
        }
        return result;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.left.left = new TreeNode(1);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(8);
        root.right.right.right = new TreeNode(12);
        root.right.right.left.left = new TreeNode(6);
        root.right.right.right.right = new TreeNode(19);


        System.out.println(levelOrder(root));
    }
}
