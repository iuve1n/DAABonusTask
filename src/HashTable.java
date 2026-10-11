import java.util.LinkedList;

public class HashTable {
    private LinkedList<Integer>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        buckets = (LinkedList<Integer>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public boolean insert(int value) {
        LinkedList<Integer> bucket = buckets[indexFor(value)];
        if (bucket.contains(value)) {
            return false;
        }
        bucket.add(value);
        size++;
        return true;
    }

    public boolean contains(int value) {
        return buckets[indexFor(value)].contains(value);
    }

    public boolean delete(int value) {
        boolean removed = buckets[indexFor(value)].remove(Integer.valueOf(value));
        if (removed) {
            size--;
        }
        return removed;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    private int indexFor(int value) {
        return Math.floorMod(value, buckets.length);
    }
}
