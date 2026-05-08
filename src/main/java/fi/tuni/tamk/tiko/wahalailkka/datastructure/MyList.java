package fi.tuni.tamk.tiko.wahalailkka.datastructure;

/**
 * A simple generic list interface.
 *
 * @param <T> the type of elements in this list
 */
public interface MyList<T> {
    /**
     * Adds all elements from the given list to this list.
     *
     * @param otherList the list whose elements are to be added
     */
    default void addAll(MyList<? extends T> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            this.add(otherList.get(i));
        }
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if the list is empty, false otherwise
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param element the element to be added
     */
    void add(T element);

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element to return
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    T get(int index);

    /**
     * Replaces the element at the specified index with the given element.
     *
     * @param index the index of the element to replace
     * @param element the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void set(int index, T element);

    /**
     * Removes the element at the specified index.
     *
     * @param index the index of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    T remove(int index);

    /**
     * Removes the first occurrence of the specified element from the list.
     *
     * @param element the element to remove
     * @return true if the element was found and removed, otherwise false
     */
    boolean remove(T element);

    /**
     * Removes all elements from the list.
     */
    void clear();

    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list
     */
    int size();
}
