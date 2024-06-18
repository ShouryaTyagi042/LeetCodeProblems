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
    class Pair {
        TreeNode node ;
        int index ;
        public Pair(TreeNode node, int index) {
            this.node = node ;
            this.index = index ;
        }
    }
    public int widthOfBinaryTree(TreeNode root) {
        Queue<Pair> q = new LinkedList<>() ;
        int ans = 0 ;
        if(root == null) {
            return ans ;
        }
        ans = 1 ;
        q.offer(new Pair(root, 0)) ;
        while(!q.isEmpty()) {
            int n = q.size() ;
            int first = 0  ;
            int last = 0  ;
            for(int i = 0 ; i < n ; i++) {
                Pair node_pair = q.poll() ;
                int current_index = node_pair.index ;
                TreeNode node = node_pair.node ;

                if(i == 0 ) {
                    first = current_index ;
                }

                if(i == n-1) {
                    last = current_index ;
                }

                if(node.left != null) {
                    q.offer(new Pair(node.left, current_index + 1 )) ;
                }

                if(node.right != null) {
                    q.offer(new Pair(node.right, current_index + 1 )) ;
                }
            }
            ans = Math.max(ans, first + last ) ;
        }
        return ans ;


    }
}
