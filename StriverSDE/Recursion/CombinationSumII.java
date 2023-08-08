class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        ArrayList<Integer> list = new ArrayList<>() ;
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>() ;
        findCombination(candidates, target, ans, list, 0 ) ;
        return ans;

    }
    private static void findCombination(int[] candidates, int target, ArrayList<ArrayList<Integer>> ans, ArrayList<Integer> list, int index)  {
        if(target == 0 ) {
            ans.add(new ArrayList<>(list)) ;
        }
        for(int i = index; i < candidates.length-1 ; i++) {
            if(i > index && candidates[i] == candidates[i-1] ) continue;
            if(candidates[i] > target) break;
            list.add(candidates[index]) ;
            findCombination(candidates, target-candidates[i] , ans, list , i + 1);
            list.remove(list.size()-1) ;
        }
    }
}
