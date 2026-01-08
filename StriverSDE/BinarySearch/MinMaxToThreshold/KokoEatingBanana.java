import java.util.*;
import java.lang.Math;


public class KokoEatingBanana {
    public static void main(String[] args) {
        int[] piles = {312884470} ;
        int h = 312884469 ;
        Solution solve = new Solution() ;
        System.out.println(solve.minEatingSpeed(piles, h));
    }

}

class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        // find max value
        int max = 0 ;
        for (int val : piles) {
            if(val > max) {
                max = val ;
            }
        }

        return this.doBinarySearch(1, max, h, piles) ;

    }

    public int doBinarySearch(int low, int high, int target, int[] piles) {
    while (low < high) {
        int mid = low + (high - low) / 2;
        int time = this.findTimeForCurrentSpeed(mid, piles);

        if (time > target) {
            low = mid + 1;   // need faster speed
        } else {
            high = mid;     // mid works, try smaller
        }
    }
    return low; // or high (same h    }
}


    public int findTimeForCurrentSpeed(int speed, int[] piles) {
        int time = 0 ;
        for(int val: piles) {
            time += val/speed ;
            if (val % speed != 0) time += 1 ;
        }
        return time ;
    }
}
