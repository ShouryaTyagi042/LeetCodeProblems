class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> list = new ArrayList<>() ;
        List<List<Integer>> ans = new ArrayList<>() ;
        boolean[] check = new boolean[nums.length] ;
        recur(nums, list, ans, check);
        return ans;

    }
    private static void recur(int[] nums, List<Integer> list, List<List<Integer>> ans, boolean[] check ){
        if(list.size() == nums.length) {
            ans.add(new ArrayList<>(list)) ;
            return;
        }
        for (int i = 0 ; i < nums.length ; i++ ) {
            if(!check[i]) {
                list.add(nums[i] ) ;
                check[i] = true ;
                recur(nums, list, ans, check);
                list.remove(list.size()-1) ;
                check[i] = false ;
            }
        }
    }
}
