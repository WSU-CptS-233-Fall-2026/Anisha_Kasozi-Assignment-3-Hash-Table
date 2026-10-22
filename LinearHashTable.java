import java.util.ArrayList;

/**
 * LinearHashTable implements an Open Addressing Hash Table with Linear Probing.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class LinearHashTable<K, V> extends HashTableBase<K, V> {

    protected boolean needsResize() {
        if (_number_of_elements > (0.7 * _primes[_local_prime_index])) {
            return true;
        }
        return false;
    }

    protected void resizeCheck() {
        if (needsResize()) {
            _local_prime_index++;

            HasherBase<K> hasher = _hasher;
            LinearHashTable<K, V> new_hash = new LinearHashTable<K, V>(hasher, _primes[_local_prime_index]);

            for (HashItem<K, V> item : _items) {
                if (item != null && !item.isEmpty()) {
                    new_hash.addElement(item.getKey(), item.getValue());
                }
            }

            _items = new_hash._items;
        }
    }

    public LinearHashTable() {
        super();
    }

    public LinearHashTable(HasherBase<K> hasher) {
        super(hasher);
    }

    public LinearHashTable(HasherBase<K> hasher, int number_of_elements) {
        super(hasher, number_of_elements);
    }

    public LinearHashTable(LinearHashTable<K, V> other) {
        super(other);
    }

    /**
     * Inserts or updates key-value pair using linear probing.
     */
    @Override
    public void addElement(K key, V value) {
        int startingIndex = getHash(key);
        int capacity = _items.size();
        int firstTombstoneIndex = -1;

        for (int i = 0; i < capacity; i++) {
            int index = (startingIndex + i) % capacity;
            HashItem<K, V> current = _items.get(index);

            // True empty slot (never used)
            if (current.isTrueEmpty()) {
                if (firstTombstoneIndex != -1) {
                    _items.set(firstTombstoneIndex, new HashItem<K, V>(key, value, false));
                } else {
                    _items.set(index, new HashItem<K, V>(key, value, false));
                }
                _number_of_elements++;
                resizeCheck();
                return;
            }

            // Existing active item with matching key
            if (!current.isEmpty() && current.getKey().equals(key)) {
                current.setValue(value);
                return;
            }

            // Tombstone slot encountered
            if (current.isEmpty() && firstTombstoneIndex == -1) {
                firstTombstoneIndex = index;
            }
        }

        // Table full or reuse tombstone
        if (firstTombstoneIndex != -1) {
            _items.set(firstTombstoneIndex, new HashItem<K, V>(key, value, false));
            _number_of_elements++;
            resizeCheck();
        }
    }

    /**
     * Lazy deletes element matching key (marks tombstone).
     */
    @Override
    public void removeElement(K key) {
        int startingIndex = getHash(key);
        int capacity = _items.size();

        for (int i = 0; i < capacity; i++) {
            int index = (startingIndex + i) % capacity;
            HashItem<K, V> current = _items.get(index);

            if (current.isTrueEmpty()) {
                return; // Key not present
            }

            if (!current.isEmpty() && current.getKey().equals(key)) {
                current.setIsEmpty(true); // Tombstone creation
                _number_of_elements--;
                return;
            }
        }
    }

    /**
     * Checks if key exists and is non-empty (skips tombstones).
     */
    @Override
    public boolean containsElement(K key) {
        int startingIndex = getHash(key);
        int capacity = _items.size();

        for (int i = 0; i < capacity; i++) {
            int index = (startingIndex + i) % capacity;
            HashItem<K, V> current = _items.get(index);

            if (current.isTrueEmpty()) {
                return false;
            }

            if (!current.isEmpty() && current.getKey().equals(key)) {
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
        int startingIndex = getHash(key);
        int capacity = _items.size();

        for (int i = 0; i < capacity; i++) {
            int index = (startingIndex + i) % capacity;
            HashItem<K, V> current = _items.get(index);

            if (current.isTrueEmpty()) {
                return null;
            }

            if (!current.isEmpty() && current.getKey().equals(key)) {
                return current.getValue();
            }
        }
        return null;
    }
}
