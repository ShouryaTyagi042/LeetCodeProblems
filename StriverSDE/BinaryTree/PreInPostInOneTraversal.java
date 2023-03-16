public class Solution {
    class Pair {
    TreeNode node;
    int num;
    Pair(TreeNode _node, int _num) {
        num = _num;
        node = _node;
    }
}
    public static List<List<Integer>> getTreeTraversal(BinaryTreeNode<Integer> root) {
        Stack<Pair> st = new Stack<Pair>() ;
        st.push(new Pair(root,1)) ;
        List<Integer> inorder = new ArrayList<Integer>() ;
        List<Integer> preorder = new ArrayList<Integer>() ;
        List<Integer> postorder = new ArrayList<Integer>() ;
        List<List<Integer>> ans = new ArrayList<ArrayList<Integer>> ;

        if(root == null) return ;

        while(st.isEmpty()) {
            Pair it = st.pop() ;
            if(it.num == 1) {
                preorder.add(it.node.val);
                it.num++;
                st.push(it) ;
                if(it.node.left != null) {
                    st.push(new Pair(it.node.left , 1 )) ;
                }
            }
            if(it.num == 2) {
                inorder.add(it.node.val);
                it.num++;
                st.push(it) ;
                if(it.node.right != null) {
                    st.push(new Pair(it.node.right , 1 )) ;
                }
            }
            else {
                postorder.add(it.node.val) ;
            }
        }
        ans.add(inorder) ;
        ans.add(preorder) ;
        ans.add(postorder) ;

        return ans ;



    }
}
