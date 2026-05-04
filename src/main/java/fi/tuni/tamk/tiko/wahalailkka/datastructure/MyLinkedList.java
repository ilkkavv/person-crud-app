package fi.tuni.tamk.tiko.wahalailkka.datastructure;

public class MyLinkedList<T> implements MyList<T> {
    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(final T data) {
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

    /**
     * @param element the element to be added
     */
    @Override
    public void add(final T element) {

    }

    /**
     * @param index the index of the element to return
     * @return
     */
    @Override
    public T get(final int index) {
        return null;
    }

    /**
     * @param index   the index of the element to replace
     * @param element the new element
     */
    @Override
    public void set(final int index, final T element) {

    }

    /**
     * @param index the index of the element to remove
     * @return
     */
    @Override
    public T remove(final int index) {
        return null;
    }

    /**
     * @param element the element to remove
     * @return
     */
    @Override
    public boolean remove(final T element) {
        return false;
    }

    /**
     *
     */
    @Override
    public void clear() {

    }

    /**
     * @return
     */
    @Override
    public int size() {
        return 0;
    }
}
