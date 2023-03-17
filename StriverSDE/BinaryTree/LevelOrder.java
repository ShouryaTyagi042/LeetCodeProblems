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

class solution {
    public List<List<Integer>> levelorder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<TreeNode>() ;
        List<List<Integer>> wraplist = new LinkedList<List<Integer>>() ;
        if(root == null) return wraplist ;
        queue.offer(root) ;
        while(!queue.isEmpty()) {
            int levelSize = queue.size() ;
            System.out.println(levelSize);
            List<Integer> sublist = new LinkedList<Integer>() ;
            for(int i = 0 ; i < levelSize ; i++ ){
                if(queue.peek().left != null) queue.offer(queue.peek().left) ;
                if(queue.peek().right != null) queue.offer(queue.peek().right) ;
                sublist.add(queue.poll().val) ;
            }
            wraplist.add(sublist) ;
        }
        return wraplist ;
    }
}
