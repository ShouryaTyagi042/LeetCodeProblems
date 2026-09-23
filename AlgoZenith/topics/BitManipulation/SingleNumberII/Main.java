class Solution {
    public int singleNumber(int[] nums) {
        int ans = 0 ;
        for(int i = 0 ; i < 32 ; i++ ){
            int currentBit = 0 ;
            for(int val : nums) {
                if((val & (1 << i)) != 0) {
                    currentBit += 1 ;
                }
            }
            ans += currentBit % 3 != 0 ? (1 << i ) : 0 ;
        }
        return ans ;
    }
}
