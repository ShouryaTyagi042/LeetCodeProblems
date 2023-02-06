class Solution {
    public void sortColors(int[] nums) {
        int count0 = 0 , count1 = 0 ;
        for(int i = 0 ; i < nums.length ; i++ ) {
            if (nums[i] == 0) {
                count0++ ;
            }
            if (nums[i] == 1) {
                count1++;
            }
        }
        for (int j=0;j < count0 ; j++ ) {
            nums[j] = 0 ;
        }
                for (int k=count0 ;k < count0 + count1 ; k++ ) {
            nums[k] = 1 ;
        }
                for (int l= count0 + count1 - 1 ;l <= nums.length  ; l++ ) {
            nums[l] = 0 ;
        }

    }
}
