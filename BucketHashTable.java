import java.util.ArrayList;

/**
 * BucketHashTable implements a Hash Table using Separate Chaining (ArrayList of ArrayLists).
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class BucketHashTable<K, V> extends HashTableBase<K, V> {

    private ArrayList<ArrayList<HashItem<K, V>>> _buckets;

    protected int getHash(K item) {
        return _hasher.getHash(item, _buckets.size());
    }

    protected boolean needsResize() {
        if (_number_of_elements > 0.5 * _buckets.size())
            return true;
        return false;
    }

    @Override
    protected void resizeCheck() {
        if (needsResize()) {
            _local_prime_index++;

            HasherBase<K> hasher = _hasher;
            BucketHashTable<K, V> new_hash = new BucketHashTable<K, V>(hasher, _primes[_local_prime_index]);

            for (ArrayList<HashItem<K, V>> item : _buckets) {
                for (HashItem<K, V> sub_item : item) {
                    if (sub_item.isEmpty() == false) {
                        new_hash.addElement(sub_item.getKey(), sub_item.getValue());
                    }
                }
            }

            _buckets = new_hash._buckets;
        }
    }

    private void initBuckets(int number_of_elements) {
        _buckets = new ArrayList<>(number_of_elements);
        for (int i = 0; i < number_of_elements; i++) {
            ArrayList<HashItem<K, V>> sub_item = new ArrayList<>();
            _buckets.add(sub_item);
        }
    }

    public BucketHashTable(HasherBase<K> hasher) {
        this(hasher, 11);
    }

    public BucketHashTable(HasherBase<K> hasher, int number_of_elements) {
        initBuckets(number_of_elements);
        _hasher = hasher;
        _local_prime_index = 0;
        _number_of_elements = 0;

        while (_primes[_local_prime_index] < number_of_elements) {
            _local_prime_index++;
        }
    }

    public BucketHashTable(BucketHashTable<K, V> other) {
        _hasher = other._hasher;
        _items = new ArrayList<>(other._items);
        _local_prime_index = other._local_prime_index;
        _number_of_elements = other._number_of_elements;
    }

    /**
     * Adds key-value pair or updates existing key's value.
     */
    @Override
    public void addElement(K key, V value) {
        int index = getHash(key);
        ArrayList<HashItem<K, V>> bucket = _buckets.get(index);

        for (HashItem<K, V> item : bucket) {
            if (!item.isEmpty() && item.getKey().equals(key)) {
                item.setValue(value);
                return;
            }
        }

        bucket.add(new HashItem<K, V>(key, value, false));
        _number_of_elements++;
        resizeCheck();
    }

    /**
     * Removes the element matching key via lazy deletion.
     */
    @Override
    public void removeElement(K key) {
        int index = getHash(key);
        ArrayList<HashItem<K, V>> bucket = _buckets.get(index);

        for (HashItem<K, V> item : bucket) {
            if (!item.isEmpty() && item.getKey().equals(key)) {
                item.setIsEmpty(true);
                _number_of_elements--;
                return;
            }
        }
    }

    /**
     * Checks if key exists and is active.
     */
    @Override
    public boolean containsElement(K key) {
        int index = getHash(key);
        ArrayList<HashItem<K, V>> bucket = _buckets.get(index);

        for (HashItem<K, V> item : bucket) {
            if (!item.isEmpty() && item.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns value for the given key, or null if not found.
     */
    @Override
    public V getElement(K key) {
        int index = getHash(key);
        ArrayList<HashItem<K, V>> bucket = _buckets.get(index);

        for (HashItem<K, V> item : bucket) {
            if (!item.isEmpty() && item.getKey().equals(key)) {
                return item.getValue();
            }
        }
        return null;
    }

    public ArrayList<ArrayList<HashItem<K, V>>> getBuckets() {
        return this._buckets;
    }
}
