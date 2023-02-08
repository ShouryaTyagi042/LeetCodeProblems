class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>() ;
        List<Integer> pre = new ArrayList<>() ;
        for (int i = 1 ; i <= numRows ; i++  ) {
            List<Integer> row = new ArrayList<Integer>() ;
            for(int j = 1 ; j <= i ; j++ ) {
                if(j==1 || j==i) row.add(1) ;
                else {
                    row.add(pre.get(j-1)+pre.get(j)) ;
                }
            }
            pre = row ;
            ans.add(row) ;
        }
        return ans ;

    }
}
