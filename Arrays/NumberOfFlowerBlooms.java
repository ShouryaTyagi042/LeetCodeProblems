class Solution {
    public int[] fullBloomFlowers(int[][] flowers, int[] people) {
        Arrays.sort(flowers, (a, b) -> Integer.compare(a,b));
        int[] sortedPeople = Arrays.copyOf(people,people.length) ;
        Arrays.sort(sortedPeople) ;
        HashMap<Integer,Integer> map = new HashMap() ;
        PriorityQueue<Integer> heap = new PriorityQueue() ;
        int i = 0 ;
        for(int person : sortedPeople) {
            while (i < flowers.length && person >= flowers[i][0]) {
                heap.push(flowers[i][1]) ;
                i++ ;
            }
            while(!heap.isEmpty() && heap.peek() < person) {
                heap.remove() ;
            }
            map.put(person,heap.size()) ;
        }
        int[] ans = new int[people.length] ;
        for(int i = 0 ; i < people.length ; i++) {
            ans[i] = map.get(people[i]) ;
        }
        return ans ;


    }
}
