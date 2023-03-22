class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer,Integer> map = new HashMap<>() ;
        for(int i = 0 ; i < nums.length ; i++ ) {
            int num = nums[i] ;
            int left = target - nums[i] ;
            if(map.containsKey(left)) {
                return [i,map.get(left)] ;
            }
            map.add(arr[i],i) ;
        }
        return null ;

    }
}
