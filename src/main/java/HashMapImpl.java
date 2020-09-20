import java.util.*;

public class HashMapImpl<K, V> implements Map<K, V> {
    private final int DEFAULT_CAPACITY = 16;
    private Entry<K, V>[] table = new Entry[DEFAULT_CAPACITY];
    private int threshold = (int) (table.length * 0.75);
    private int size;
    private EntrySet<Entry<K, V>> EntrySet = new EntrySet<>();


    public static void main (String[] args) {
        HashMapImpl<Object, Object> hashMap = new HashMapImpl<Object, Object>();
        hashMap.put(10, "Apple");
        hashMap.put(1, "Orange");
        hashMap.put(79, "Grape");
        System.out.println("Value at 79 " + hashMap.get(79));
        System.out.println("Value at 1 " + hashMap.get(1));
        System.out.println("Value at 10 " + hashMap.get(10));
        System.out.println("Value at 2 " + hashMap.get(2));
        hashMap.put(null, "Pear");
        System.out.println("Val at null " + hashMap.get(null));
        System.out.println("Hashmap has key at null:" + hashMap.containsKey(null));
        System.out.println("Hashmap has value at null:" + hashMap.containsValue("Pear"));
        System.out.println("Size of Map:" + hashMap.size());
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private Entry<K, V> nodeThrough(Entry<K, V> Entry, K key) {
        while (Entry != null) {
            if (Entry.getKey() != null) {
                if (Entry.getKey().equals(key)) {
                    return Entry;
                }
            } else {
                if (key == null)
                    return Entry;
            }
            Entry = Entry.getNext();
        }
        return null;
    }

    private Entry<K, V> addEntry(K key, V value) {
        return new Entry<>(key, value);
    }

    private void putForNullKey(V value) {
        if (table[0] != null) {
            Entry<K, V> Entry = nodeThrough(table[0], null);
            if (Entry != null) {
                EntrySet.remove(Entry);
                Entry.setValue(value);
                EntrySet.add(Entry);
            } else {
                Entry<K, V> newEntry = addEntry(null, value);
                EntrySet.add(newEntry);
                newEntry.setNext(table[0]);
                table[0] = newEntry;
                size++;
            }
        }
        else {
            table[0] = addEntry(null, value);
            EntrySet.add(table[0]);
            size++;
        }
    }

    /**
     * Returns true if map is empty, false otherwise
     * @return true if map is empty, false otherwise
     */

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified key
     * @param key The key whose presence in this map is to be tested
     * @@return <tt>true</tt> if this map contains a mapping for the specified key
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Returns true if hashmap has an entry with the specified value, false otherwise.
     * @param value entry value matching a specific key
     * @return true if hashmap has an entry with the specified value, false otherwise
     */
    @Override
    public boolean containsValue(Object value) {
        HashMapImpl.Entry<K, V>[] tab;
        if ((tab = table) != null && size > 0) {

            for(int i = 0; i < tab.length; ++i) {
                for(HashMapImpl.Entry<K, V> entry = tab[i]; entry != null; entry = entry.next) {
                    Object v;
                    if ((v = entry.value) == value || value != null && value.equals(v)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     */

    public V put(K key, V value) {
        resize();
        if (key != null) {
            int hash = hash(key.hashCode());
            int index = indexFor(hash, table.length - 1);

            if (table[index] != null) {
                Entry<K, V> Entry = nodeThrough(table[index], key);
                if (Entry != null) {
                    EntrySet.remove(Entry);
                    Entry.setValue(value);
                    EntrySet.add(Entry);
                } else {
                    Entry<K, V> newEntry = addEntry(key, value);
                    EntrySet.add(newEntry);
                    newEntry.setNext(table[index]);
                    table[index] = newEntry;
                    size++;
                }
            } else {
                table[index] = addEntry(key, value);
                EntrySet.add(table[index]);
                size++;
            }
        } else {
            putForNullKey(value);
        }
        return value;
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after this call returns
     */
    @Override
    public void clear() {
        HashMapImpl.Entry<K, V>[] tab;
        if ((tab = table) != null && size > 0) {
            size = 0;

            for(int i = 0; i < tab.length; ++i) {
                tab[i] = null;
            }
        }
    }

    class EntrySet<E> extends HashSet<E> {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    class MyKeySet<E> extends HashMap<K, V> {

    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    /**
     * Returns the entry mapped to key in the HashMap.
     */
    @Override
    public V get(Object k) {
        Entry<K, V> Entry;
        if (k == null) {
            Entry = nodeThrough(table[0], (K) k);
        } else {
            int hash = hash(k.hashCode());
            int index = indexFor(hash, table.length - 1);
            Entry = nodeThrough(table[index], (K) k);
        }
        if (Entry != null) {
            return Entry.getValue();
        }
        return null;
    }

    private void transfer(Entry<K, V> entry, Entry<K, V>[] newTable) {
        while (entry != null) {
            int index = 0;
            if (entry.getKey() != null) {
                int hash = hash(entry.getKey().hashCode());
                index = indexFor(hash, table.length - 1);
            }
            if (newTable[index] != null) {
                Entry<K, V> newEntry = addEntry(entry.getKey(), entry.getValue());
                newEntry.setNext(newTable[index]);
                newTable[index] = newEntry;
            } else {
                newTable[index] = addEntry(entry.getKey(), entry.getValue());
            }
            entry = entry.getNext();
        }
        table = newTable;
    }

    private void resize() {
        if (size >= threshold) {
            Entry<K, V>[] newTable = new Entry[table.length * 2];
            for (Entry<K, V> entry : table) {
                if (entry != null) {
                    transfer(entry, newTable);
                }
            }
        }
    }

    /**
     * Removes an entry with a specific key.
     * @param key unique entry identifier
     * @return value matching a key that was removed
     */
    @Override
    public V remove(Object key) {
        Entry<K, V> result = removeEntry(key);
        return result.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    private Entry<K, V> removeEntry(Object key) {
        int index = 0;
        if (key != null) {
            int hash = hash(key.hashCode());
            index = indexFor(hash, table.length - 1);
        }
        Entry<K, V> Entry = table[index];
        Entry<K, V> result = null;

        if (Entry != null) {
            if (key != null) {
                if (Entry.getKey().equals(key) || Entry.getKey() == key) {
                    result = Entry;
                    table[index] = Entry.getNext();
                    EntrySet.remove(Entry);
                    size--;
                    return result;
                }
                while (Entry.getNext() != null) {
                    if (Entry.getNext().getKey().equals(key) || Entry.getNext().getKey() == key) {
                        break;
                    }
                    Entry = Entry.getNext();
                }
                if (Entry.getNext() != null) {
                    Entry<K, V> myNewEntry = Entry.getNext().getNext();
                    Entry.setNext(myNewEntry);
                    result = Entry;
                    EntrySet.remove(Entry);
                    size--;
                    return result;
                }
            } else {
                if (Entry.getKey() == null) {
                    table[index] = Entry.getNext();
                    EntrySet.remove(Entry);
                    size--;
                    return Entry;
                } else {
                    while (Entry.getNext() != null) {
                        if (Entry.getNext().getKey() == null) {
                            break;
                        }
                        Entry = Entry.getNext();
                    }
                    if (Entry.getNext() != null) {
                        Entry<K, V> myNewEntry = Entry.getNext().getNext();
                        Entry.setNext(myNewEntry);
                        result = Entry;
                        EntrySet.remove(Entry);
                        size--;
                        return result;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(table) + ", size=" + size + ", table length=" + table.length;
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Entry<K, V> getNext() {
            return next;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        /**
         * Returns entry hashcode.
         * @return int value - entry hashcode
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (key == null ? 0 : key.hashCode());
            result = prime * result + (value == null ? 0 : value.hashCode());
            return result;
        }

        /**
         * Checks if Entry objects are equal.
         * @return true if objects are equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Entry<?, ?> Entry = (Entry<?, ?>) obj;
            return Objects.equals(getKey(), Entry.getKey()) &&
                    Objects.equals(getValue(), Entry.getValue());
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "K=" + key +
                    ",V=" + value +
                    ",N=" + next +
                    "} ";
        }
    }

}