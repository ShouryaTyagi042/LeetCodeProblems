import java.util.* ;

public class MergeIntervals {
    public static void main(String[] args) {
        Solution solve = new Solution();
        int[][] intervals = {{1,4},{0,2},{3,5}} ;
        System.out.println(Arrays.deepToString(solve.merge(intervals)));
    }
}

class Solution {
    public int[][] merge(int[][] intervals) {
        if(intervals.length ==1) return intervals ;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0])) ;
        int start = intervals[0][0] ;
        int end = intervals[0][1] ;
        List<int []> temp = new ArrayList<>();
        for(int i = 1 ; i < intervals.length ; i++) {
            if (intervals[i][0] <= end && intervals[i][0] >= start) {
                start = Math.min(start, intervals[i][0]) ;
                end = Math.max(end , intervals[i][1]) ;
            } else {
                temp.add(new int[]{start, end}) ;
                start = intervals[i][0] ;
                end = intervals[i][1] ;
            }
        }
        temp.add(new int[]{start, end}) ;
        int[][] ans =  temp.toArray(new int[temp.size()][]) ;
        return ans ;

    }
}
