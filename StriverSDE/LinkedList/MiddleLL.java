/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode middleNode(ListNode head) {
        ListNode current = head ;
        int count = 0 ;
        while(current != null ) {
            count++ ;
            current = current.next ;
        }
        // System.out.print(count);
        ListNode ans = head ;
        for(int i = 0 ; i <= count/2 ; i++ ) {
            ans = ans.next ;
        }
        return ans ;

    }
}


class Solution {
    public ListNode middleNode(ListNode head) {
        ListNode ptr1 = head  ;
        ListNode ptr2 = head ;
        while(ptr2!=null && ptr2.next!=null)  {

            ptr1 = ptr1.next.next ;
            ptr2 = ptr2.next ;
        }
        return ptr2 ;

    }
}
