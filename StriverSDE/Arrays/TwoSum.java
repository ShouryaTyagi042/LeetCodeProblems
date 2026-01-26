import java.util.* ;
public class TwoSum {
    public static void main(String[] args) {
        Solution solve = new Solution() ;
        int[] nums = {2,7,11,15} ;
        int target = 9 ;
        System.out.println(Arrays.toString(solve.twoSum(nums, target)));
    }
}

class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>() ;
        int[] ans = new int[2] ;
        for (int i = 0 ; i < nums.length ; i++ ) {
            int num = nums[i] ;
            int check = target - nums[i] ;
            if(map.containsKey(check)) {
                return new int[]{num, check} ;
            }
            map.put(num, i) ;
        }
        return ans ;
    }
}
