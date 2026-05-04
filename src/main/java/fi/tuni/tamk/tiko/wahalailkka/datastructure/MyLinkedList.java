package fi.tuni.tamk.tiko.wahalailkka.datastructure;

/**
 * Linked list implementation of the {@link MyList} interface.
 *
 * @param <T> the type of elements stored in this list
 */
public class MyLinkedList<T> implements MyList<T> {
    /**
     * Node used internally by {@link MyLinkedList}.
     *
     * @param <T> the type of data stored in the node
     */
    private static class Node<T> {
        private T data;
        private Node<T> next;

        Node(final T data) {
            this.data = data;
            next = null;
        }

        public T getData() {
            return data;
        }

        public void setData(final T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(final Node<T> next) {
            this.next = next;
        }
    }

    /** First Node of the list. */
    private Node<T> head;
    /** Last Node of the list. */
    private Node<T> tail;
    /** Number of elements currently stored in the list. */
    private int size = 0;

    /**
     * Adds the given element to the end of this list.
     *
     * @param element the element to be added
     * @throws IllegalArgumentException if the element is {@code null}
     */
    @Override
    public void add(final T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null.");
        }

        Node<T> newNode = new Node<>(element);

        if (size == 0) {
            head = newNode;
            tail = head;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }

        size++;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element to return
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public T get(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }

        Node<T> currentNode = head;

        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }

        return currentNode.getData();
    }

    /**
     * Replaces the element at the specified index with the given element.
     *
     * @param index the index of the element to replace
     * @param element the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws IllegalArgumentException if the element is {@code null}
     */
    @Override
    public void set(final int index, final T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }

        if (element == null) {
            throw new IllegalArgumentException("Element must not be null.");
        }

        Node<T> currentNode = head;

        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }

        currentNode.setData(element);
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index the index of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public T remove(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }

        Node<T> previousNode = null;
        Node<T> currentNode = head;

        for (int i = 0; i < index; i++) {
            previousNode = currentNode;
            currentNode = currentNode.getNext();
        }

        T removedData = currentNode.getData();

        if (index == 0) {
            head = head.getNext();
            if (size == 1) {
                tail = null;
            }
        } else {
            if (index == size - 1) {
                tail = previousNode;
            }
            previousNode.setNext(currentNode.getNext());
        }

        size--;
        return removedData;
    }

    /**
     * Removes the first occurrence of the given element from this list.
     *
     * @param element the element to remove
     * @return {@code true} if the element was found and removed,
     *         {@code false} otherwise
     * @throws IllegalArgumentException if the element is {@code null}
     */
    @Override
    public boolean remove(final T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null.");
        }

        Node<T> previousNode = null;
        Node<T> currentNode = head;

        while (currentNode != null) {
            if (currentNode.getData().equals(element)) {
                if (currentNode == head) {
                    head = head.getNext();
                    if (size == 1) {
                        tail = null;
                    }
                } else {
                    if (currentNode == tail) {
                        tail = previousNode;
                    }
                    previousNode.setNext(currentNode.getNext());
                }

                size--;
                return true;
            } else {
                previousNode = currentNode;
                currentNode = currentNode.getNext();
            }
        }

        return false;
    }

    /**
     * Removes all elements from this list.
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size; }
}
