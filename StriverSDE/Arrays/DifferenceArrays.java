class Solution {
    public List<List<Integer>> findDifference(int[] nums1, int[] nums2) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>() ;
        HashSet<Integer> n1 = new HashSet<Integer>() ;
        HashSet<Integer> n2 = new HashSet<Integer>() ;
        for(int num: nums1) {
            n1.add(num);
        }
        for(int num: nums2) {
            n2.add(num);
        }
        List<Integer> s1 = new ArrayList<Integer>() ;
        List<Integer> s2 = new ArrayList<Integer>() ;
        for(int num: n1) {
            if(n2.contains(num)){
                continue;
            }
            else {
                s1.add(num);
            }
        }
        for(int num: n2) {
            if(n1.contains(num)){
                continue;
            }
            else {
                s2.add(num);
            }
        }
        ans.add(s1);
        ans.add(s2);
        return ans;





    }
}
