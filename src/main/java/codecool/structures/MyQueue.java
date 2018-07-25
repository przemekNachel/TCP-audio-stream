package codecool.structures;

import java.util.NoSuchElementException;

public class MyQueue<T> {

    private Node<T> head;
    private Node<T> tail;

    public void enqueue(T element) {
        if (head == null) {
            head = new Node<>(element);
            tail = head;
        } else {
            tail.setNext(new Node<>(element));
            tail = tail.getNext();
        }
    }

    public T peek() {
        if(head == null) throw new NoSuchElementException();
        return head.getValue();
    }

    public T dequeue() {
        if(head == null) throw new NoSuchElementException();
        T element = head.getValue();
        head = head.getNext();
        return element;
    }

    public int size() {
        if (isEmpty()) return 0;
        int i = 1;
        for(Node current = head; current.getNext() != null; current = current.getNext()) i++;
        return i;
    }

    public boolean isEmpty() {
        return head == null;
    }
}
