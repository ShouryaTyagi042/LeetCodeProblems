class Solution {
    public void nextPermutation(int[] nums) {
        //step 1 : find the breakpoint
        int index = -1 ;
        for(int i = nums.length - 1  ; i >  0 ; i--) {
            if(nums[i] > nums[i--]  ){
                index = i-- ;
                break ;
            }
        }
        if(index == -1) {
            reverse(nums)
        }
        for(int i = nums.length - 1 ; i > index ; i-- ) {
            if(nums[i] > nums[index]) {
                int temp =  nums[i] ;
                nums[i] = nums[index] ;
                nums[index] = temp ;
                break;
            }
        }
        reverse(nums,index+1);
        return
        
        
    }
    void swap(int[] nums,int i,int j){
        int temp=nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }
    void reverse(int[] nums,int start){
        int i=start;
        int j=nums.length-1;
        while(i<j){
            swap(nums,i,j);
            i++;
            j--;
        }
    }
}
