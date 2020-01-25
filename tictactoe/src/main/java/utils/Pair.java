package utils;

/**
 * Contains a pair of generic type T
 * 
 * @author rkm
 *
 * @param <T> generic type
 */
public class Pair<T> {

	/**
	 * First element
	 */
	private T first;

	/**
	 * Second element
	 */
	private T second;

	/**
	 * Default constructor
	 */
	public Pair() {

	}

	/**
	 * Initialise a pair by giving first and second element
	 * 
	 * @param newFirst
	 * @param newSecond
	 */
	public Pair(T newFirst, T newSecond) {
		first = newFirst;
		second = newSecond;
	}

	/**
	 * Returns the first element in the pair
	 * 
	 * @return first element
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * Sets the first element in the pair
	 * 
	 * @param new value for first element
	 */
	public void setFirst(T first) {
		this.first = first;
	}

	/**
	 * Gets the second element in the pair
	 * 
	 * @return second element
	 */
	public T getSecond() {
		return second;
	}

	/**
	 * Sets the second element in the pair
	 * 
	 * @param new value for second element
	 */
	public void setSecond(T second) {
		this.second = second;
	}

	/**
	 * Adds an item to the pair. If there's first element is null then item will be
	 * the first element. Else if there's already a first item, then adds to the
	 * second element. If the second element is not null than return -1 to indicate
	 * that no item has been added.
	 * 
	 * @param item to add
	 * @return 1 if item added to first, 2 if item added to second, -1 if item not
	 *         added.
	 */
	public int add(T item) {
		if (first == null) {
			first = item;
			return 1;
		} else if (second == null) {
			second = item;
			return 2;
		} else {
			return -1;
		}
	}

	/**
	 * Checks if the item is the first or second element, and set first or second to
	 * null accordingly
	 * 
	 * @param item item to remove
	 */
	public void remove(T item) {
		if (item.equals(first)) {
			first = null;
		} else if (item.equals(second)) {
			second = null;
		}
	}

	/**
	 * Checks whether the pair is full (first and second both not null)
	 * 
	 * @return true if the pair is full, false otherwise
	 */
	public boolean isFull() {
		return (first != null && second != null);
	}

	/**
	 * Checks whether the pair is empty (first and second both null)
	 * 
	 * @return true if the pair is comptely null, false otherwise
	 */
	public boolean isEmpty() {
		return (first == null && second == null);
	}

	/**
	 * Completly unsets to pair, by setting both first and second to null
	 */
	public void empty() {
		first = null;
		second = null;
	}

}
