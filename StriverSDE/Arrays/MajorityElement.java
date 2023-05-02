class Solution {
    public int majorityElement(int[] nums) {
        if(nums.length==1 || nums.length == 2) return nums[0] ;
        HashMap<Integer,Integer> map = new HashMap<>() ;
        for (int num:nums
             ) {
            if(!map.containsKey(num)) {
                map.put(num,1) ;
            }
            else {
                int count = map.get(num) + 1  ;
                if (count > nums.length / 2) {
                    return  num ;
                }
                else {
                    map.put(num,count) ;
                }
            }
        }
        return  -1 ;

    }
}
