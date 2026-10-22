import java.util.ArrayList;

public abstract class HashTableBase<K, V> {
    protected HasherBase<K> _hasher;
    protected ArrayList<HashItem<K, V>> _items;
    protected int _local_prime_index;
    protected int _number_of_elements;

    protected static final int[] _primes = {
        11, 23, 47, 97, 193, 389, 769, 1543, 3079, 6151, 12301, 24611, 49211
    };

    public HashTableBase() {
        this(null, 11);
    }

    public HashTableBase(HasherBase<K> hasher) {
        this(hasher, 11);
    }

    public HashTableBase(HasherBase<K> hasher, int number_of_elements) {
        _hasher = hasher;
        _local_prime_index = 0;
        _number_of_elements = 0;

        while (_local_prime_index < _primes.length && _primes[_local_prime_index] < number_of_elements) {
            _local_prime_index++;
        }
        if (_local_prime_index >= _primes.length) {
            _local_prime_index = _primes.length - 1;
        }

        _items = new ArrayList<>(_primes[_local_prime_index]);
        for (int i = 0; i < _primes[_local_prime_index]; i++) {
            _items.add(new HashItem<K, V>());
        }
    }

    public HashTableBase(HashTableBase<K, V> other) {
        _hasher = other._hasher;
        _items = new ArrayList<>(other._items);
        _local_prime_index = other._local_prime_index;
        _number_of_elements = other._number_of_elements;
    }

    protected int getHash(K item) {
        return _hasher.getHash(item, _primes[_local_prime_index]);
    }

    protected abstract boolean needsResize();
    protected abstract void resizeCheck();
    public abstract void addElement(K key, V value);
    public abstract void removeElement(K key);
    public abstract boolean containsElement(K key);
    public abstract V getElement(K key);

    public int size() {
        return _number_of_elements;
    }
}
