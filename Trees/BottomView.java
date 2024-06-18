class Solution
{
      class Pair{
    Node node ;
    int row ;
    public Pair(Node node, int row) {
      this.node = node ;
      this.row = row ;
    }
  }
    //Function to return a list containing the bottom view of the given tree.
    public ArrayList <Integer> bottomView(Node root)
    {
           Map<Integer, Integer> mpp = new TreeMap<>();
      Queue<Pair> queue = new LinkedList<>();
      ArrayList<Integer> ans = new ArrayList<>();
      if (root == null) return ans;
      queue.add(new Pair(root,0));
      while(!queue.isEmpty()) {
        int n = queue.size() ;
        for(int i = 1 ;  i <= n ; i++ ) {
            Pair pair = queue.poll();
            Node node = pair.node;
            int line = pair.row;
            mpp.put(line, node.data) ;
           // Process left child
            if (node.left != null) {
                // Push the left child with a decreased
                // vertical position into the queue
                q.add(new Pair(node.left, line - 1));
            }

            // Process right child
            if (node.right != null) {
                // Push the right child with an increased
                // vertical position into the queue
                q.add(new Pair(node.right, line + 1));
            }
        }
      }
      for(int values: mpp.values()) {
        ans.add(values) ;
      }
      return ans;
    }
}
