import java.util.* ;

public class ThreeSum {
    public static void main(String[] args) {
    Solution solve = new Solution() ;
    int[] nums = {-1,0,1,2,-1,-4} ;
    System.out.println(solve.threeSum(nums));
    }
}

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums) ;
        List<List<Integer>> ans = new ArrayList<>();
        HashSet<List<Integer>> set = new HashSet<>() ;
        for(int i = 0 ; i < nums.length ; i++) {
            if(i != 0 && nums[i] == nums[i-1]) {
                continue ;
            }
            int start = i + 1 ;
            int end = nums.length - 1 ;
            int target = -1 * nums[i] ;
            while(start < end) {
                int sum = nums[start] + nums[end] ;
                if ( sum == target ) {
                    List<Integer> addToList = new ArrayList<>() ;
                    addToList.add(nums[i]);
                    addToList.add(nums[start]);
                    addToList.add(nums[end]);
                    if (!set.contains(addToList)) {
                        ans.add(addToList) ;
                        set.add(addToList) ;
                    }
                    start ++ ;
                    end -- ;
                } else if ( sum > target) {
                    end = end - 1;
                } else {
                    start = start + 1 ;
                }
            }
        }
        return ans ;

    }
}
