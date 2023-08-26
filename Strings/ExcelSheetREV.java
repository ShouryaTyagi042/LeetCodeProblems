//{https://leetcode.com/problems/excel-sheet-column-number/description/}

class Solution {
    public int titleToNumber(String columnTitle) {
        int l = columnTitle.length() ;
        int ans = 0 ;
        int factor = 1 ;
        for(int i = l-1 ; i >= 0 ; i-- ) {
            ans += ((int)columnTitle.charAt(i) - 64)*factor ;
            factor *= 26 ;
        }
        return ans ;


    }
}
