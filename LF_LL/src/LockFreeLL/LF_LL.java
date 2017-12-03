package LockFreeLL;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LF_LL<T> {

    private Node<T> head;
    private Node<T> tail;

    public LF_LL() {
        head = new Node<>(null);
        head.key = Integer.MIN_VALUE;

        tail = new Node<>(null);
        tail.key = Integer.MAX_VALUE;

        head.next.set(tail, false);
        tail.next.set(null, false);
    }

    public boolean insert(T item) {
        int key = item.hashCode();
        while (true) {
            Window<T> window = find(head, key);
            Node<T> pred = window.pred;
            Node<T> curr = window.curr;

            if (curr.key == key) {
                return false;
            } else {
                Node<T> node = new Node<>(item);
                node.next = new AtomicMarkableReference(curr, false);
                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }
    }

    public boolean search(T item) {
        boolean[] marked = {false};
        int key = item.hashCode();
        Node<T> curr = head;
        while (curr.key < key) {
            curr = curr.next.getReference();
            Node succ = curr.next.get(marked);
        }
        return (curr.key == key && !marked[0]);
    }

    public boolean delete(T item) {
        int key = item.hashCode();
        boolean snip;
        while (true) {
            Window<T> window = find(head, key);
            Node<T> pred = window.pred;
            Node<T> curr = window.curr;
            if (curr.key != key) {
                return false;
            } else {
                Node<T> succ = curr.next.getReference();
                snip = curr.next.attemptMark(succ, true);
                if (!snip) {
                    continue;
                }
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }


    public Window find(Node head, int key) {
        Node<T> pred, curr, succ;
        boolean[] marked = {false};
        boolean snip;
        retry:while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(marked);
                while (marked[0]) {
                    snip = pred.next.compareAndSet(curr, succ, false, false);
                    if (!snip) continue retry;
                    curr = succ;
                    succ = curr.next.get(marked);
                }
                if (curr.key >= key) {
                    return new Window<>(pred, curr);
                }
                pred = curr;
                curr = succ;
            }
        }
    }

    private class Node<P> {
        P data;
        int key;
        AtomicMarkableReference<Node<P>> next;

        public Node(P data) {
            this.data = data;
            if (data != null) {
                this.key = data.hashCode();
            }
            next = new AtomicMarkableReference<>(null, false);
        }

        public String toString() {
            return "" + data;
        }
    }

    private class Window<P> {
        Node<P> pred, curr;

        Window(Node<P> myPred, Node<P> myCurr) {
            pred = myPred;
            curr = myCurr;
        }

    }
}
