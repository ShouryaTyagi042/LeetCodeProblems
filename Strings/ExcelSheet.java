// {https://leetcode.com/problems/excel-sheet-column-title/description/}

class Solution {
    public String convertToTitle(int columnNumber) {
        if(columnNumber < 27 ) {
            return Character.toString((char) 65 + columnNumber - 1 ) ;
        }
        StringBuilder ans = new StringBuilder() ;
        while(columnNumber > 0) {
            if(columnNumber % 26 == 0 ) {
                ans.append("Z") ;
                columnNumber--;
            }
            else {
                ans.append(Character.toString((char) 65 + columnNumber%26 - 1 )) ;
            }
            columnNumber = columnNumber / 26 ;
        }
        StringBuilder rev = ans.reverse() ;
        return rev.toString() ;

    }
}
