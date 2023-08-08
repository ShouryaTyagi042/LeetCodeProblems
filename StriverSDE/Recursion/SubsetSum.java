class Solution{
    ArrayList<Integer> subsetSums(ArrayList<Integer> arr, int N){
        ArrayList<Integer> ans = new ArrayList<>() ;
        return printSub(arr, 0, 0, N, ans );

    }

    static ArrayList<Integer> printSub(ArrayList<Integer> array ,int sum, int index ,int n, ArrayList<Integer> ans) {
        if(index == n) {
            ans.add(sum) ;
            return ans;
        }
        sum += array.get(index) ;
        printSub(array, sum, index+1, n, ans);

        sum -= array.get(index) ;
            printSub(array, sum, index+1, n, ans);

        return ans ;

    }
}
