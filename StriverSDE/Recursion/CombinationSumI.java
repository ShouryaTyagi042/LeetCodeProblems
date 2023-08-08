class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        ArrayList<Integer> list = new ArrayList<>() ;
        HashSet<List<Integer>> ans = new HashSet<>() ;
        recur(list, ans, target, candidates, 0, 0 ) ;
        ArrayList<List<Integer>> dup = new ArrayList<>(ans) ;
        return dup ;

    }

    private static void recur(ArrayList<Integer> list, HashSet<List<Integer>> ans, int target, int[] candidates, int sum, int index) {
        if(index == candidates.length) {
            if(sum == target)  ans.add(new ArrayList<>(list)) ;
            return;
        }
        list.add(candidates[index]) ;
        sum += candidates[index] ;
        if(sum <= target)  recur(list, ans, target, candidates, sum, index  ) ;
        list.remove(list.size()-1) ;
        sum -= candidates[index] ;
        recur(list, ans, target, candidates, sum, index+1 );

    }
}
