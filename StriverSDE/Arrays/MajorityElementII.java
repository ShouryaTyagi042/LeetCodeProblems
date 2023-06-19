class Solution {
    public List<Integer> majorityElement(int[] v) {
        HashMap<Integer,Integer> map = new HashMap<>() ;
        for(int i  = 0 ; i < v.length ; i++ ) {
            if(map.containsKey(v[i])) {
                map.put(v[i],map.get(v[i])++) ;
            }
            else{
                map.put(v[i],1) ;
            }
        }
        List<Integer> ans = new ArrayList<>() ;
        for(int k: map.keySet()) {
            if(map.get(k) > v.length/3 ) {
                ans.add(k) ;
            }
        }
        return ans ;

    }
}
