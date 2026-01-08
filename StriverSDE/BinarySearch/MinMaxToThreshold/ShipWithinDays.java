public class ShipWithinDays {
    public static void main(String[] args) {
        Solution solve = new Solution() ;
        int[] weights = {1,2,3,1,1} ;
        int days = 4 ;
        System.out.println(solve.shipWithinDays(weights, days));
    }
}

class Solution {
    public int shipWithinDays(int[] weights, int days) {
        int maxCapacity = 0 ;
        int maxWeight = 0 ;
        for(int weight : weights) {
            maxCapacity += weight ;
            if(maxWeight <= weight) {
                maxWeight = weight ;
            }
        }


        return this.doBinarySearch(weights, days, maxWeight, maxCapacity) ;
    }


    public int doBinarySearch(int[] weights, int days, int start , int end) {
        while (start < end) {
            int mid = start + (end  - start) / 2 ;
            if(this.daysToShipWithGivenCapacity(mid, weights) <= days) {
                end = mid ;
            } else {
                start  = mid + 1 ;
            }
        }
        return end ;
    }



    public int daysToShipWithGivenCapacity(int cap, int[] weights) {
        int ans = 1 ;
        int currentWeight = 0 ;
        for(int weight : weights) {
            if (currentWeight + weight <= cap) {
                currentWeight += weight ;
            } else {
                currentWeight = weight ;
                ans ++ ;
            }
        }
        return ans ;
    }
}
