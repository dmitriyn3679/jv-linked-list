package core.basesyntax;

import java.util.List;

public class MyLinkedList<T> implements MyLinkedListInterface<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    // --------------------
    // Public API
    // --------------------

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<>(tail, value, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    @Override
    public void add(T value, int index) {
        checkIndexForAdd(index);
        if (index == size) {
            add(value);
            return;
        }
        Node<T> nextNode = node(index);
        Node<T> prevNode = nextNode.prev;
        Node<T> newNode = new Node<>(prevNode, value, nextNode);
        nextNode.prev = newNode;
        if (prevNode == null) {
            head = newNode;
        } else {
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public void addAll(List<T> list) {
        for (T e : list) {
            add(e);
        }
    }

    @Override
    public T get(int index) {
        return node(index).value;
    }

    @Override
    public T set(T value, int index) {
        Node<T> n = node(index);
        T old = n.value;
        n.value = value;
        return old;
    }

    @Override
    public T remove(int index) {
        Node<T> n = node(index);
        return unlink(n);
    }

    @Override
    public boolean remove(T object) {
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            if ((object == null && cur.value == null)
                    || (object != null && object.equals(cur.value))) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // --------------------
    // Private helpers
    // --------------------

    private Node<T> node(int index) {
        checkIndex(index);
        if (index < (size >>> 1)) {
            Node<T> cur = head;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
            return cur;
        } else {
            Node<T> cur = tail;
            for (int i = size - 1; i > index; i--) {
                cur = cur.prev;
            }
            return cur;
        }
    }

    private T unlink(Node<T> n) {
        Node<T> next = n.next;
        Node<T> prev = n.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        // Оголошуємо елемент безпосередньо перед очищенням і поверненням
        final T element = n.value;

        // help GC
        n.value = null;
        n.next = null;
        n.prev = null;

        size--;
        return element;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // --------------------
    // Inner class at the end
    // --------------------

    private static class Node<T> {
        private T value;
        private Node<T> prev;
        private Node<T> next;

        private Node(Node<T> prev, T value, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }
}
