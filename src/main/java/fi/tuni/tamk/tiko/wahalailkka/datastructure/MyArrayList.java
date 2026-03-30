package fi.tuni.tamk.tiko.wahalailkka.datastructure;

/**
 * A simple dynamic array implementation of the {@link MyList} interface.
 * <p>
 * This list grows automatically when capacity is exceeded.
 * Elements are stored in an internal array.
 *
 * @param <T> the type of elements stored in this list
 */
public class MyArrayList<T> implements MyList<T> {
    /** Initial capacity of the list. */
    static final int INITIAL_SIZE = 10;
    /** Factor used to grow the internal array when capacity is exceeded. */
    static final double SIZE_MULTIPLIER = 1.5;

    /** Internal array storing the elements. */
    private T[] data = (T[]) new Object[INITIAL_SIZE];
    /** Number of elements currently stored in the list. */
    private int size = 0;

    /**
     * Adds an element to the end of the list.
     * <p>
     * If the internal array is full, its capacity is increased.
     *
     * @param element the element to add
     * @throws IllegalArgumentException if the element is null
     */
    @Override
    public void add(final T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null.");
        }
        if (size == data.length) {
            Object[] oldData = data.clone();
            data = (T[]) new Object[(int) (size * SIZE_MULTIPLIER)];
            System.arraycopy(oldData, 0, data, 0,
                    oldData.length);
        }
        data[size] = element;
        size++;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @Override
    public T get(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }
        return data[index];
    }

    /**
     * Replaces the element at the specified index with a new element.
     *
     * @param index the index of the element to replace
     * @param element the new element
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @Override
    public void set(final int index, final T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }
        data[index] = element;
    }

    /**
     * Removes the element at the specified index.
     * <p>
     * All subsequent elements are shifted to the left.
     *
     * @param index the index of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @Override
    public T remove(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }
        T element = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        return element;
    }

    /**
     * Removes the first occurrence of the specified element from the list.
     *
     * @param element the element to remove
     * @return true if the element was found and removed, otherwise false
     */
    @Override
    public boolean remove(final T element) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(element)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all elements from the list.
     * <p>
     * The internal array is reset to its initial capacity.
     */
    @Override
    public void clear() {
        data = (T[]) new Object[INITIAL_SIZE];
        size = 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of stored elements
     */
    @Override
    public int size() {
        return size;
    }
}
