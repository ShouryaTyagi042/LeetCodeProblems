class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        ArrayList<Integer> list = new ArrayList<>() ;
        ArrayList<ArrayList<Integer>> dup = new ArrayList<>() ;
        fun(0, nums, list, dup) ;
        HashSet<ArrayList<Integer>> set = new HashSet<>(dup) ;
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>(set) ;
        return ans;

    }

        public static ArrayList<ArrayList<Integer>> fun(int index, int[] arr, ArrayList<Integer> current, ArrayList<ArrayList<Integer>> ans) {
            if(index == arr.length ) {
                ans.add(new ArrayList<>(current));
                //new ArrayList<>(list) will make a copy of list, which will make sure the elements be stored into res.
                return ans;
            }
            current.add(arr[index] ) ;
            fun(index+1, arr, current, ans);
            current.remove(current.size()-1) ;
        fun(index+1, arr, current, ans);
        return ans ;
    }
}
