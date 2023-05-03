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

//Moore Voting Algorithm

class Solution {
    public int majorityElement(int[] nums) {
        int element = nums[0] , count = 0 ;
        for (int num : nums) {
            if(count == 0 ) {
                count = 1 ;
                element = num ;
            }
            else if (element == num) count ++ ;
            else {
                count-- ;
            }
        }
        int cnt1 = 0;
        for(int n: nums) {
            if (n == element) cnt1++ ;
        }
        if(cnt1>nums.length/2) return element ;
        return -1 ;

    }
}
