

//User function Template for Java


class Solution {
    public long kthElement( int array1[], int array2[], int m, int n, int k) {
        int p1 = 0, p2 = 0, counter = 0, answer = 0;

        while (p1 < m && p2 < n) {
            if (counter == k) return answer;
            else if (array1[p1] < array2[p2]) {
                answer = array1[p1];
                ++p1;
            } else {
                answer = array2[p2];
                ++p2;
            }
            ++counter;
        }
        if (counter != k) {
            if (p1 == m - 1)
                answer = array2[p2 + k - counter];
            else
                answer = array1[p1 + k - counter];
        }
        return answer;    }
}
