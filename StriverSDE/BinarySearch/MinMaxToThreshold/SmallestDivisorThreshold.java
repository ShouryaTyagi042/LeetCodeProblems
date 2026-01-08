public class SmalletDivisorThreshold {
    public static void main(String[] args) {

    }
}

class Solution {
    public int smallestDivisor(int[] piles, int h) {
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
