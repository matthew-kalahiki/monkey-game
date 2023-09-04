package game.model.DataStructures;

import java.util.ArrayList;
import java.util.Comparator;

public class MinHeap<E> implements Searcher<E> {
    private ArrayList<E> stuff;
    private Comparator<E> comparator;

    public MinHeap(Comparator<E> comparator) {
        stuff = new ArrayList<>();
        this.comparator = comparator;
    }

    public int size() {
        return stuff.size();
    }

    public int capacity() {
        return stuff.size();
    }

    // Exists only for unit testing purposes.
    void backdoorAdd(E value) {
        stuff.add(value);
    }

    // Exists only for unit testing purposes.
    E get(int i) {
        return stuff.get(i);
    }

    /**
     * Given an index, return the index of its parent.
     */
    static int parent(int i) {
        // TODO Step 1 Implement the correct calculation.
        return (i - 1)/2;
    }

    /**
     * Given an index, return the index of its left child.
     */
    static int left(int i) {
        // TODO Step 1 Implement the correct calculation.
        return (2 * i) + 1;
    }

    /**
     * Given an index, return the index of its right child.
     */
    static int right(int i) {
        // TODO Step 1 Implement the correct calculation.
        return (2 * i) + 2;
    }

    /**
     * Given an index, return true if it is a legal array index, and false otherwise.
     */
    boolean legal(int i) {
        // TODO Step 1 Implement the correct calculation.
        if (i >= 0 && i < stuff.size()){
            return true;
        }
        return false;
    }

    /**
     * Verify that each element is smaller than its children elements.
     * Because of our use of an ArrayList to track the elements, we
     * will always assume that the elements form a Heap that is as
     * compact as possible.
     *
     * Your solution can be simplified by using the legal(), left(), and right()
     * methods.
     */
    boolean isHeap() {
        // TODO Step 2 Implement the correct calculation.
        for(int i = 0; i < stuff.size(); i++){
            if (legal(left(i)) && comparator.compare(stuff.get(i),stuff.get(left(i))) > 0){
                return false;
            }
            if (legal(right(i)) && comparator.compare(stuff.get(i),stuff.get(right(i))) > 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Swaps the values at indices i and j.
     */
    void swap(int i, int j) {
        // TODO Step 3 Implement swapping.
        E a = stuff.get(i);
        E b = stuff.get(j);
        stuff.set(i,b);
        stuff.set(j,a);
    }

    /**
     * Return the root element, which will always be stored in
     * the first position of the ArrayList.
     */
    public E element() {
        // TODO Step 4 Return the correct value.
        return stuff.get(0);
    }

    /**
     * New elements are added to the end of the ArrayList, and then
     * filtered up repeatedly when the element is found to be less
     * than its parent. Use the Comparator object to calculate if an
     * element is greater or less than another element.
     *
     * Your solution can be simplified by using the swap() and parent() methods.
     */
    public void add(E data) {
        // TODO Step 4 implement add
        stuff.add(data);
        int i = stuff.size()-1;
        while (comparator.compare(data,stuff.get(parent(i))) < 0){
            swap(i,parent(i));
            i = parent(i);
        }
    }

    /**
     * Given the index of a parent, this method checks the value at that index
     * against the values of its children. If either child is smaller than the
     * parent, the index of the smallest child is returned. If no children are
     * present, or the parent has the lowest value among the three, the parent's
     * own index is returned.
     *
     * Your solution can be simplified by using the legal(), left(),
     * and right() methods.
     */
    int indexOfLowestInFamily(int parent) {
        // TODO Step 5 Implement solution
        int a = 0;
        if (legal(left(parent)) && comparator.compare(stuff.get(left(parent)),stuff.get(parent)) < 0){
            a = left(parent);
        }
        else{
            a = parent;
        }
        if (legal(right(parent)) && comparator.compare(stuff.get(right(parent)),stuff.get(a)) < 0){
            a = right(parent);
        }
        return a;
    }

    /**
     * First, swap the first and last elements. Next, remove the last element,
     * and save its value to be returned at the end of the method. This could
     * cause a violation of the Heap property that all parents must
     * be smaller than their children. If the element is less than
     * only one of its children, swap these two elements. If the element
     * is less than both of its children, swap the smaller of the two
     * children with the element, so that we don't break the Heap
     * property any further. Finally, when a swap was made, repeatedly
     * check the subsequent descendants to guarantee that the Heap
     * property is always preserved.
     *
     * Your solution can be simplified by using the indexOfLowestInFamily()
     * and swap() methods.
     */
    public E remove() {
        // TODO Step 6 Implement remove().
        swap(0,stuff.size()-1);
        E a = stuff.get(stuff.size()-1);
        stuff.remove(stuff.size()-1);
        int i = 0;
        while(indexOfLowestInFamily(i) != i){
            int b = indexOfLowestInFamily(i);
            swap(i,b);
            i = b;
        }
        return a;
    }

    public void update(E changed){
        remove();
        add(changed);
    }

}
