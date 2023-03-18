/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int diameterOfBinaryTree(TreeNode root) {
        int diameter = 0 ;

    }
    public int height(TreeNode root, diameter) {
        if(node == null) {
            return 0 ;
        }
        int left = height(root.left,diameter) ;
        int right = height(root.right,diameter);
        diameter = Math.max(diameter,left+right);
        return 1 + Math.max(left,right) ;
    }
}
