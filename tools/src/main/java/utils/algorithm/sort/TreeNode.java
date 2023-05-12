package utils.algorithm.sort;

import lombok.Data;

/**
 * @author Jimmy
 */
@Data
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }
}
