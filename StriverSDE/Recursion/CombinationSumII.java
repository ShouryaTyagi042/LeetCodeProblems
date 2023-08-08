class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<Integer> list = new ArrayList<>() ;
        List<List<Integer>> ans = new ArrayList<>() ;
        findCombination(candidates, target, ans, list, 0 ) ;
        return ans;

    }
    private static void findCombination(int[] candidates, int target, List<List<Integer>> ans, List<Integer> list, int index)  {
        if(target == 0 ) {
            ans.add(new List<>(list)) ;
        }
        for(int i = index; i < candidates.length-1 ; i++) {
            if(i > index && candidates[i] == candidates[i-1] ) continue;
            if(candidates[i] > target) break;
            list.add(candidates[i]) ;
            findCombination(candidates, target-candidates[i] , ans, list , i + 1);
            list.remove(list.size()-1) ;
        }
    }
}
