class Solution {
    public int singleNonDuplicate(int[] nums) {
        int element = 0  ;
        for(int e: nums) {
            element = element ^ e ;
        }
        return element ;


    }
}
