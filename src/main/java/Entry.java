import java.util.Map;

public class Entry<K, V> implements Map.Entry<K, V> {
    K key;
    V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        this.value = value;
        return value;
    }

    @Override
    public int hashCode() {
        int prime = 13;
        int mul = 11;
        if (key != null) {
            int hashCode = prime * mul + key.hashCode();
            return hashCode;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass().getName() != o.getClass().getName()) {
            return false;
        }
        Entry e = (Entry) o;
        if (this.key == e.key) {
            return true;
        }
        return false;
    }
}