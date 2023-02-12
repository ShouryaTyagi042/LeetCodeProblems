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
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>() ;
        return traverse(ans,root) ;
        }
    static List<Integer> traverse(List<Integer> ans , TreeNode current) {
        if(current!=null)
        {
            traverse(ans,current.left);

            traverse(ans,current.right);
             ans.add(current.val);
        }
        return ans;

    }
}
