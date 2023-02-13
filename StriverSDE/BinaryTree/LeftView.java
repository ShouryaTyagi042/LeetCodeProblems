class Tree
{
    //Function to return list containing elements of left view of binary tree.
    ArrayList<Integer> leftView(Node root)
    {
      if (root == null)
        return;
      Queue<Node> queue = new LinkedList<>();
      ArrayList<Integer> ans = new ArrayList<>();
      queue.add(root);
      while(!queue.isEmpty()) {
        n = queue.size() ;
        for(int i = 1 ;  i <= n ; i++ ) {
          Node temp = queue.poll() ;
          if(i==1) {
            ans.add(temp.data) ;
          }
          if(temp.left != null) {
            queue.add(temp.left) ;
          }
          if(temp.right != null) {
            queue.add(temp.right) ;
          }
        }
      }
      return ans ;
    }
}
