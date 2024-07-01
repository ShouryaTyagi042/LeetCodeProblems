class Solution {
    static ArrayList<ArrayList<Integer>> ans ;
    static ArrayList<Integer> curr ;

    public void getPath(Node root, ArrayList<Integer> curr, ArrayList<ArrayList<Integer>> ans ) {
        if (root == null) return  ;
        curr.add(root.data) ;
        if(root.left == null && root.right == null) {
            ans.add(new ArrayList<>(curr)) ;

        }else {
                getPath(root.left, curr, ans) ;
                getPath(root.right, curr, ans) ;
        }


        curr.remove(curr.size() - 1 ) ;
    }
    public static ArrayList<ArrayList<Integer>> Paths(Node root) {
        ans = new ArrayList<>() ;
        curr = new ArrayList<>() ;
        return getPath(root, curr , ans);

    }
}
