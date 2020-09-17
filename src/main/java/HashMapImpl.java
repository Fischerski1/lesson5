import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashMapImpl<K, V> implements Map {
    private float loadfactor = 0.75f;
    private int capacity = 100;
    private int size = 0;
    private Entry<K, V> table[] = new Entry[capacity];

    private int Hashing(int hashCode) {
        int location = hashCode % capacity;
        System.out.println("Location:" + location);
        return location;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        if (this.size == 0) {
            return true;
        }
        return false;
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            if(table[0].getKey() == null) {
                return true;
            }
        }
        int location = Hashing(key.hashCode());
        Entry e = null;
        try {
            e = table[location];
        } catch(NullPointerException ex) {

        }

        if (e!= null && e.getKey() == key) {
            return true;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (int i=0; i<table.length;i++) {
            if (table[i] != null && table[i].getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public V get (Object key) {
        V ret = null;
        if(key == null) {
            Entry<K, V> e = null;
            try {
                e = table[0];
            } catch(NullPointerException ex) {

            }
            if(e != null) {
                return (V) e.getValue();
            }
        } else {
            int location = Hashing(key.hashCode());
            Entry<K, V> e = null;
            try {
                e = table[location];
            } catch (NullPointerException ex) {

            }
            if (e!= null && e.getKey() == key) {
                return (V) e.getValue();
            }
        }
        return ret;
    }

    public V put (Object key, Object value) {
        V ret = null;
        if (key == null) {
            ret = putForNullKey((V) value);
            return ret;
        } else {
            int location = Hashing(key.hashCode());
            if (location >= capacity) {
                System.out.println("Rehashing required");
                return null;
            }

            Entry<K, V> e = null;
            try {
                e = table[location];
            } catch (NullPointerException ex) {

            }
            if (e!= null && e.getKey() == key) {
                ret = (V) e.getValue();
            } else {
                Entry<K, V> eNew = new Entry<K,V>();
                eNew.setKey(key);
                eNew.setValue(value);
                table[location] = eNew;
                size++;
            }
        }
        return ret;
    }

    private V putForNullKey (V value) {
        Entry<K, V> e = null;
        try {
            e = table[0];
        } catch (NullPointerException ex) {

        }
        V ret = null;
        if (e != null && e.getKey() == null) {
            ret = (V) e.getValue();
            e.setValue(value);
            return ret;
        } else {
            Entry<K, V> eNew = new Entry<K,V>();
            eNew.setKey(null);
            eNew.setValue(value);
            table[0] = eNew;
            size++;
        }
        return ret;
    }

    public V remove (Object key) {
        int location = Hashing(key.hashCode());
        V ret = null;
        if (table[location].getKey() == key) {
            for (int i=location; i<table.length;i++) {
                table[i] = table[i+1];
            }
        }
        return ret;
    }

    @Override
    public void putAll (Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry> entrySet() {
        return null;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer action) {

    }

    @Override
    public void replaceAll(BiFunction function) {

    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        return false;
    }

    @Override
    public Object replace(Object key, Object value) {
        return null;
    }

    @Override
    public Object computeIfAbsent(Object key, Function mappingFunction) {
        return null;
    }

    @Override
    public Object computeIfPresent(Object key, BiFunction remappingFunction) {
        return null;
    }

    @Override
    public Object compute(Object key, BiFunction remappingFunction) {
        return null;
    }

    @Override
    public Object merge(Object key, Object value, BiFunction remappingFunction) {
        return null;
    }

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


}