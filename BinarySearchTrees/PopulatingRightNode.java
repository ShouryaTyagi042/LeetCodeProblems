/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
*/

class Solution {
    public Node connect(Node root) {
        Queue<Node> nodes_queue = new LinkedList<>() ;
        if(root == null) {
            return ;
        }
        nodes_queue.offer(root) ;
        while(!nodes_queue.isEmpty()) {
            int n = nodes_queue.size() ;
            for(int i = 0 ; i < n ;  i++) {
                Node current = nodes_queue.poll();
                if(i == n-1 ) {
                    current.next = null ;
                }
                else {
                    current.next = nodes_queue.peek() ;
                }
                if(current.left != null) {
                    nodes_queue.offer(current.left) ;
                }
                if(current.right != null) {
                    nodes_queue.offer(current.right) ;
                }
            }
        }

    }
}
