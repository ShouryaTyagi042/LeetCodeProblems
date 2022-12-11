package LinkedList;

public class InsertInDll {
    static class Node {
        int data;
        Node prev, next;
        Node(int item)
        {data = item;
            next = prev = null;
        }
    }
    static void insert(int data) {

    }
    static void printList(Node head) {
        Node current = head ;
        while (current != null) {
            System.out.print(current.data+"->");
            current = current.next ;
        }
    }
    public static Node sortedInsert(Node head, int x)
    {
        Node newNode = new Node(x) ;
        if(head.data > x ) {
            newNode.next = head ;
            head.prev = newNode ;
            head = newNode ;
            return head ;
        }
        Node current = head ;
        Node temp = null ;
        while(current != null && current.data <= x ) {
            temp = current ;
            current = current.next ;
        }
        temp.next = newNode ;
        newNode.next = current ;

        if(current != null) {
            current.prev = newNode ;
        }
        newNode.prev = temp ;


        return head ;
    }
    public static void main(String[] args) {



    }
}
