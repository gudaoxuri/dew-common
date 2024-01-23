package org.apache.commons.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A TreeMap that supports fast access to the underlying TreeMap.
 * <p>
 * This class is useful when you want to access the underlying TreeMap
 * directly, but still want to benefit from the fast access provided by
 * the FastHashMap.
 * <p>
 * This class is not thread-safe.
 */
public class FastTreeMap extends TreeMap {
    /**
     * The underlying TreeMap.
     */
    protected TreeMap map = null;
    /**
     * Whether the fast access is enabled.
     */
    protected boolean fast = false;

    /**
     * Creates a new FastTreeMap.
     */
    public FastTreeMap() {
        this.map = new TreeMap();
    }

    /**
     * Creates a new FastTreeMap.
     *
     * @param comparator the comparator to use
     */
    public FastTreeMap(Comparator comparator) {
        this.map = new TreeMap(comparator);
    }

    /**
     * Creates a new FastTreeMap.
     *
     * @param map the map to copy
     */
    public FastTreeMap(Map map) {
        this.map = new TreeMap(map);
    }

    /**
     * Creates a new FastTreeMap.
     *
     * @param map the map to copy
     */
    public FastTreeMap(SortedMap map) {
        this.map = new TreeMap(map);
    }

    /**
     * Returns whether the fast access is enabled.
     *
     * @return whether the fast access is enabled
     */
    public boolean getFast() {
        return this.fast;
    }

    /**
     * Enables or disables the fast access.
     *
     * @param fast whether to enable the fast access
     */
    public void setFast(boolean fast) {
        this.fast = fast;
    }

    /**
     * Returns the value for the given key.
     *
     * @param key the key
     * @return the value
     */
    @Override
    public Object get(Object key) {
        if (this.fast) {
            return this.map.get(key);
        } else {
            synchronized (this.map) {
                return this.map.get(key);
            }
        }
    }

    /**
     * Returns the size of the map.
     *
     * @return the size of the map
     */
    @Override
    public int size() {
        if (this.fast) {
            return this.map.size();
        } else {
            synchronized (this.map) {
                return this.map.size();
            }
        }
    }

    /**
     * Returns whether the map is empty.
     *
     * @return whether the map is empty
     */
    @Override
    public boolean isEmpty() {
        if (this.fast) {
            return this.map.isEmpty();
        } else {
            synchronized (this.map) {
                return this.map.isEmpty();
            }
        }
    }

    /**
     * Returns whether the map contains the given key.
     *
     * @param key the key
     * @return whether the map contains the given key
     */
    @Override
    public boolean containsKey(Object key) {
        if (this.fast) {
            return this.map.containsKey(key);
        } else {
            synchronized (this.map) {
                return this.map.containsKey(key);
            }
        }
    }

    /**
     * Returns whether the map contains the given value.
     *
     * @param value the value
     * @return whether the map contains the given value
     */
    @Override
    public boolean containsValue(Object value) {
        if (this.fast) {
            return this.map.containsValue(value);
        } else {
            synchronized (this.map) {
                return this.map.containsValue(value);
            }
        }
    }

    /**
     * Returns the comparator of the map.
     *
     * @return the comparator of the map
     */
    @Override
    public Comparator comparator() {
        if (this.fast) {
            return this.map.comparator();
        } else {
            synchronized (this.map) {
                return this.map.comparator();
            }
        }
    }

    /**
     * Returns the first key of the map.
     *
     * @return the first key of the map
     */
    @Override
    public Object firstKey() {
        if (this.fast) {
            return this.map.firstKey();
        } else {
            synchronized (this.map) {
                return this.map.firstKey();
            }
        }
    }

    /**
     * Returns the last key of the map.
     *
     * @return the last key of the map
     */
    @Override
    public Object lastKey() {
        if (this.fast) {
            return this.map.lastKey();
        } else {
            synchronized (this.map) {
                return this.map.lastKey();
            }
        }
    }

    /**
     * Puts the given key-value pair into the map.
     *
     * @param key   the key
     * @param value the value
     * @return the previous value associated with the key
     */
    @Override
    public Object put(Object key, Object value) {
        if (this.fast) {
            synchronized (this) {
                TreeMap temp = (TreeMap) this.map.clone();
                Object result = temp.put(key, value);
                this.map = temp;
                return result;
            }
        } else {
            synchronized (this.map) {
                return this.map.put(key, value);
            }
        }
    }

    /**
     * Puts all key-value pairs from the given map into the map.
     *
     * @param in the map to copy
     */
    @Override
    public void putAll(Map in) {
        if (this.fast) {
            synchronized (this) {
                TreeMap temp = (TreeMap) this.map.clone();
                temp.putAll(in);
                this.map = temp;
            }
        } else {
            synchronized (this.map) {
                this.map.putAll(in);
            }
        }

    }

    /**
     * Removes the given key from the map.
     *
     * @param key the key
     * @return the previous value associated with the key
     */
    @Override
    public Object remove(Object key) {
        if (this.fast) {
            synchronized (this) {
                TreeMap temp = (TreeMap) this.map.clone();
                Object result = temp.remove(key);
                this.map = temp;
                return result;
            }
        } else {
            synchronized (this.map) {
                return this.map.remove(key);
            }
        }
    }

    /**
     * Clears the map.
     */
    @Override
    public void clear() {
        if (this.fast) {
            synchronized (this) {
                this.map = new TreeMap();
            }
        } else {
            synchronized (this.map) {
                this.map.clear();
            }
        }

    }

    /**
     * Compares the map to the given object.
     *
     * @param o the object to compare to
     * @return whether the map is equal to the given object
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Map)) {
            return false;
        } else {
            Map mo = (Map) o;
            Object key;
            if (this.fast) {
                if (mo.size() != this.map.size()) {
                    return false;
                } else {
                    Iterator i = this.map.entrySet().iterator();

                    label64:
                    do {
                        do {
                            if (!i.hasNext()) {
                                return true;
                            }

                            Map.Entry e = (Map.Entry) i.next();
                            key = e.getValue();
                            if (key == null) {
                                continue label64;
                            }
                        } while (key.equals(mo.get(key)));

                        return false;
                    } while (mo.get(key) == null && mo.containsKey(key));

                    return false;
                }
            } else {
                synchronized (this.map) {
                    if (mo.size() != this.map.size()) {
                        return false;
                    } else {
                        Iterator i = this.map.entrySet().iterator();

                        label77:
                        do {
                            Object value;
                            do {
                                if (!i.hasNext()) {
                                    return true;
                                }

                                Map.Entry e = (Map.Entry) i.next();
                                key = e.getKey();
                                value = e.getValue();
                                if (value == null) {
                                    continue label77;
                                }
                            } while (value.equals(mo.get(key)));

                            return false;
                        } while (mo.get(key) == null && mo.containsKey(key));

                        return false;
                    }
                }
            }
        }
    }

    /**
     * Returns the hash code of the map.
     *
     * @return the hash code of the map
     */
    @Override
    public int hashCode() {
        if (this.fast) {
            return 0;
        } else {
            synchronized (this.map) {
                return 0;
            }
        }
    }

    /**
     * Clones the map.
     *
     * @return the cloned map
     */
    @Override
    public Object clone() {
        FastTreeMap results = null;
        if (this.fast) {
            results = new FastTreeMap(this.map);
        } else {
            synchronized (this.map) {
                results = new FastTreeMap(this.map);
            }
        }

        results.setFast(this.getFast());
        return results;
    }

    /**
     * Returns the head map of the map.
     *
     * @param key the key
     * @return the head map
     */
    @Override
    public SortedMap headMap(Object key) {
        if (this.fast) {
            return this.map.headMap(key);
        } else {
            synchronized (this.map) {
                return this.map.headMap(key);
            }
        }
    }

    /**
     * Returns the sub map of the map.
     *
     * @param fromKey the from key
     * @param toKey   the to key
     * @return the sub map
     */
    @Override
    public SortedMap subMap(Object fromKey, Object toKey) {
        if (this.fast) {
            return this.map.subMap(fromKey, toKey);
        } else {
            synchronized (this.map) {
                return this.map.subMap(fromKey, toKey);
            }
        }
    }

    /**
     * Returns the tail map of the map.
     *
     * @param key the key
     * @return the tail map
     */
    @Override
    public SortedMap tailMap(Object key) {
        if (this.fast) {
            return this.map.tailMap(key);
        } else {
            synchronized (this.map) {
                return this.map.tailMap(key);
            }
        }
    }

    /**
     * Returns the entry set of the map.
     *
     * @return the entry set
     */
    @Override
    public Set entrySet() {
        return new EntrySet();
    }

    /**
     * Returns the key set of the map.
     *
     * @return the key set
     */
    @Override
    public Set keySet() {
        return new KeySet();
    }

    /**
     * Returns the values of the map.
     *
     * @return the values
     */
    @Override
    public Collection values() {
        return new Values();
    }

    /**
     * Returns the entry set of the map.
     *
     * @return the entry set
     */
    private class EntrySet extends CollectionView implements Set {
        /**
         * Constructs a new entry set.
         */
        private EntrySet() {
            super();
        }

        /**
         * Constructs a new entry set.
         */
        @Override
        protected Collection get(Map map) {
            return map.entrySet();
        }

        /**
         * Returns the next object in the iteration.
         *
         * @param entry the entry
         * @return the next object in the iteration
         */
        @Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry;
        }
    }

    /**
     * Returns the values of the map.
     *
     * @return the values
     */
    private class Values extends CollectionView {
        /**
         * Constructs a new entry set.
         */
        private Values() {
            super();
        }

        /**
         * Constructs a new entry set.
         */
        @Override
        protected Collection get(Map map) {
            return map.values();
        }

        /**
         * Returns the next object in the iteration.
         *
         * @param entry the entry
         * @return the next object in the iteration
         */
        @Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry.getValue();
        }
    }

    /**
     * Returns the key set of the map.
     *
     * @return the key set
     */
    private class KeySet extends CollectionView implements Set {
        /**
         * Constructs a new entry set.
         */
        private KeySet() {
            super();
        }

        /**
         * Constructs a new entry set.
         */
        @Override
        protected Collection get(Map map) {
            return map.keySet();
        }

        /**
         * Returns the next object in the iteration.
         *
         * @param entry the entry
         * @return the next object in the iteration
         */
        @Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry.getKey();
        }
    }

    /**
     * Abstract class for collection views.
     */
    private abstract class CollectionView implements Collection {
        /**
         * Constructs a new entry set.
         */
        protected CollectionView() {
        }

        /**
         * Returns the next object in the iteration.
         *
         * @param var1 the entry
         * @return the next object in the iteration
         */
        protected abstract Collection get(Map var1);

        /**
         * Returns the next object in the iteration.
         *
         * @param var1 the entry
         * @return the next object in the iteration
         */
        protected abstract Object iteratorNext(Map.Entry var1);

        /**
         * Removes all of the elements from this collection.
         */
        @Override
        public void clear() {
            if (FastTreeMap.this.fast) {
                synchronized (FastTreeMap.this) {
                    FastTreeMap.this.map = new TreeMap();
                }
            } else {
                synchronized (FastTreeMap.this.map) {
                    this.get(FastTreeMap.this.map).clear();
                }
            }

        }

        /**
         * Removes the specified element from this collection.
         *
         * @param o the object to remove
         * @return true if this collection contained the specified element
         */
        @Override
        public boolean remove(Object o) {
            if (FastTreeMap.this.fast) {
                synchronized (FastTreeMap.this) {
                    TreeMap temp = (TreeMap) FastTreeMap.this.map.clone();
                    boolean r = this.get(temp).remove(o);
                    FastTreeMap.this.map = temp;
                    return r;
                }
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).remove(o);
                }
            }
        }

        /**
         * Removes the specified element from this collection.
         *
         * @param o the object to remove
         * @return true if this collection contained the specified element
         */
        @Override
        public boolean removeAll(Collection o) {
            if (FastTreeMap.this.fast) {
                synchronized (FastTreeMap.this) {
                    TreeMap temp = (TreeMap) FastTreeMap.this.map.clone();
                    boolean r = this.get(temp).removeAll(o);
                    FastTreeMap.this.map = temp;
                    return r;
                }
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).removeAll(o);
                }
            }
        }

        /**
         * Removes the specified element from this collection.
         *
         * @param o the object to remove
         * @return true if this collection contained the specified element
         */
        @Override
        public boolean retainAll(Collection o) {
            if (FastTreeMap.this.fast) {
                synchronized (FastTreeMap.this) {
                    TreeMap temp = (TreeMap) FastTreeMap.this.map.clone();
                    boolean r = this.get(temp).retainAll(o);
                    FastTreeMap.this.map = temp;
                    return r;
                }
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).retainAll(o);
                }
            }
        }

        /**
         * Returns the number of elements in this collection.
         *
         * @return the number of elements in this collection
         */
        @Override
        public int size() {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).size();
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).size();
                }
            }
        }

        /**
         * Returns true if this collection contains no elements.
         *
         * @return true if this collection contains no elements
         */
        @Override
        public boolean isEmpty() {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).isEmpty();
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).isEmpty();
                }
            }
        }

        /**
         * Returns true if this collection contains the specified element.
         *
         * @param o the object to check for
         * @return true if this collection contains the specified element
         */
        @Override
        public boolean contains(Object o) {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).contains(o);
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).contains(o);
                }
            }
        }

        /**
         * Returns true if this collection contains all of the elements of the specified collection.
         *
         * @param o the collection to check against
         * @return true if this collection contains all of the elements of the specified collection
         */
        @Override
        public boolean containsAll(Collection o) {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).containsAll(o);
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).containsAll(o);
                }
            }
        }

        /**
         * Returns an array of the elements in this collection.
         *
         * @return an array of the elements in this collection
         */
        @Override
        public Object[] toArray(Object[] o) {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).toArray(o);
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).toArray(o);
                }
            }
        }

        /**
         * Returns an array of the elements in this collection.
         *
         * @return an array of the elements in this collection
         */
        @Override
        public Object[] toArray() {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).toArray();
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).toArray();
                }
            }
        }

        /**
         * Returns true if this collection contains all of the elements in the specified collection.
         *
         * @param o the collection to check against
         * @return true if this collection contains all of the elements in the specified collection
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).equals(o);
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).equals(o);
                }
            }
        }

        /**
         * Returns the hash code value for this collection.
         *
         * @return the hash code value for this collection
         */
        @Override
        public int hashCode() {
            if (FastTreeMap.this.fast) {
                return this.get(FastTreeMap.this.map).hashCode();
            } else {
                synchronized (FastTreeMap.this.map) {
                    return this.get(FastTreeMap.this.map).hashCode();
                }
            }
        }

        /**
         * Adds the specified element to this collection.
         *
         * @param o the object to add
         * @return true if this collection did not already contain the specified element
         */
        @Override
        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        /**
         * Adds all of the elements in the specified collection to this collection.
         *
         * @param c the collection to add
         * @return true if this collection changed as a result of the call
         */
        @Override
        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        /**
         * Returns an iterator over the elements in this collection.
         *
         * @return an iterator over the elements in this collection
         */
        @Override
        public Iterator iterator() {
            return new CollectionViewIterator();
        }

        /**
         * CollectionViewIterator
         */
        private class CollectionViewIterator implements Iterator {
            /**
             * The map that we expect to be modified.
             */
            private Map expected;
            /**
             * The last element returned by next().
             */
            private Map.Entry lastReturned = null;
            /**
             * The iterator over the map.
             */
            private Iterator iterator;

            /**
             * Constructs a new instance.
             */
            protected CollectionViewIterator() {
                this.expected = FastTreeMap.this.map;
                this.iterator = this.expected.entrySet().iterator();
            }

            /**
             * Returns true if this collection contains any more elements.
             *
             * @return true if this collection contains any more elements
             */
            @Override
            public boolean hasNext() {
                if (this.expected != FastTreeMap.this.map) {
                    throw new ConcurrentModificationException();
                } else {
                    return this.iterator.hasNext();
                }
            }

            /**
             * Returns the next element in this collection.
             *
             * @return the next element in this collection
             */
            @Override
            public Object next() {
                if (this.expected != FastTreeMap.this.map) {
                    throw new ConcurrentModificationException();
                } else {
                    this.lastReturned = (Map.Entry) this.iterator.next();
                    return CollectionView.this.iteratorNext(this.lastReturned);
                }
            }

            /**
             * Removes the last element returned by this iterator.
             */
            @Override
            public void remove() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                } else {
                    if (FastTreeMap.this.fast) {
                        synchronized (FastTreeMap.this) {
                            if (this.expected != FastTreeMap.this.map) {
                                throw new ConcurrentModificationException();
                            }

                            FastTreeMap.this.remove(this.lastReturned.getKey());
                            this.lastReturned = null;
                            this.expected = FastTreeMap.this.map;
                        }
                    } else {
                        this.iterator.remove();
                        this.lastReturned = null;
                    }

                }
            }
        }
    }
}
