class Solution {
    class Pair {
        int freq;
        char ch;
        Pair(int freq, char ch) {
            this.freq = freq;
            this.ch = ch;
        }
    }
    public String reorganizeString(String s) {
        HashMap<Character,Integer> map = new HashMap<>() ;
        int l = s.length() ;
        for(int i = 0 ; i < l ; i++){
            if(!map.containsKey(s.charAt(i))) {
                map.put(s.charAt(i),1) ;
            }
            else {
                int currentF = map.get(s.charAt(i)) ;
                map.put(s.charAt(i),++currentF) ;
                if(currentF  > (l+1)/2) return "" ;
            }
        }
        PriorityQueue<Pair> p = new PriorityQueue<>((a, b) -> b.freq - a.freq);
        for(Map.Entry<Character,Integer> entry: map.entrySet()) {
            p.offer(new Pair(entry.getValue(),entry.getKey())) ;
        }
        StringBuilder ans = new StringBuilder() ;
        while (p.size() >= 2) {
            Pair p1 = p.poll();
            Pair p2 = p.poll();
            ans.append(p1.ch);
            ans.append(p2.ch);
            if (p1.freq > 1) {
                p.offer(new Pair(p1.freq - 1, p1.ch));
            }
            if (p2.freq > 1) {
                p.offer(new Pair(p2.freq - 1, p2.ch));
            }
        }
        if (!p.isEmpty()) {
            ans.append(p.poll().ch);
        }
        return ans.toString();
    }
}
