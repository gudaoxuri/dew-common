package org.apache.commons.collections;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * fastHashMap
 */
public class FastHashMap extends HashMap {

    /**
     * map
     */
    protected HashMap map = null;

    /**
     * fast
     */
    protected boolean fast = false;

    /**
     * Constructs an empty <code>FastHashMap</code> with the default initial capacity (16) and load
     */
    public FastHashMap() {
        this.map = new HashMap();
    }

    /**
     * Constructs an empty <code>FastHashMap</code> with the specified initial capacity and load
     * factor.
     *
     * @param capacity the initial capacity
     */
    public FastHashMap(int capacity) {
        this.map = new HashMap(capacity);
    }

    /**
     * Constructs an empty <code>FastHashMap</code> with the specified initial capacity and load
     *
     * @param capacity capacity
     * @param factor   factor
     */
    public FastHashMap(int capacity, float factor) {
        this.map = new HashMap(capacity, factor);
    }

    /**
     * Constructs an empty <code>FastHashMap</code> with the specified initial capacity and load
     *
     * @param map map
     */
    public FastHashMap(Map map) {
        this.map = new HashMap(map);
    }

    /**
     * get fast
     *
     * @return fast fast
     */
    public boolean getFast() {
        return this.fast;
    }

    /**
     * set fast
     *
     * @param fast fast
     */
    public void setFast(boolean fast) {
        this.fast = fast;
    }

    /**
     * get
     *
     * @param key key
     * @return Object Object
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
     * size
     *
     * @return int int
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
     * isEmpty
     *
     * @return boolean
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
     * containsKey
     *
     * @param key key
     * @return boolean
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
     * containsValue
     *
     * @param value value
     * @return boolean
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
     * put
     *
     * @param key   key
     * @param value value
     * @return Object Object
     */
    @Override
    public Object put(Object key, Object value) {
        if (this.fast) {
            synchronized (this) {
                HashMap temp = (HashMap) this.map.clone();
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
     * putAll
     *
     * @param in in
     */
    @Override
    public void putAll(Map in) {
        if (this.fast) {
            synchronized (this) {
                HashMap temp = (HashMap) this.map.clone();
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
     * remove
     *
     * @param key key
     * @return Object Object
     */
    @Override
    public Object remove(Object key) {
        if (this.fast) {
            synchronized (this) {
                HashMap temp = (HashMap) this.map.clone();
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
     * clear
     */
    @Override
    public void clear() {
        if (this.fast) {
            synchronized (this) {
                this.map = new HashMap();
            }
        } else {
            synchronized (this.map) {
                this.map.clear();
            }
        }

    }

    /**
     * equals
     *
     * @param o o
     * @return boolean
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
                            key = e.getKey();
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
     * hashCode
     *
     * @return int
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
     * clone
     *
     * @return object object
     */
    @Override
    public Object clone() {
        FastHashMap results = null;
        if (this.fast) {
            results = new FastHashMap(this.map);
        } else {
            synchronized (this.map) {
                results = new FastHashMap(this.map);
            }
        }

        results.setFast(this.getFast());
        return results;
    }

    /**
     * entrySet
     *
     * @return set set
     */
    @Override
    public Set entrySet() {
        return new EntrySet();
    }

    /**
     * keySet
     *
     * @return set set
     */
    @Override
    public Set keySet() {
        return new KeySet();
    }

    /**
     * values
     *
     * @return collection collection
     */
    @Override
    public Collection values() {
        return new Values();
    }

    /**
     * CollectionView
     */
    private class EntrySet extends CollectionView implements Set {
        /**
         * EntrySet
         */
        private EntrySet() {
            super();
        }

        /**
         * get
         *
         * @param map map
         * @return collection collection
         */
        @Override

        protected Collection get(Map map) {
            return map.entrySet();
        }

        /**
         * iteratorNext
         *
         * @param entry entry
         * @return object object
         */
        @Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry;
        }
    }

    /**
     * values
     */
    private class Values extends CollectionView {
        private Values() {
            super();
        }

        /**
         * get
         *
         * @param map map
         * @return collection collection
         */
        @Override
        protected Collection get(Map map) {
            return map.values();
        }

        /**
         * iteratorNext
         *
         * @param entry entry
         * @return object object
         */
        @Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry.getValue();
        }
    }

    /**
     * keySet
     */
    private class KeySet extends CollectionView implements Set {

        /**
         * KeySet
         */
        private KeySet() {
            super();
        }

        /**
         * get
         *
         * @param map map
         * @return collection collection
         */
        @Override
        protected Collection get(Map map) {
            return map.keySet();
        }

        /**
         * iteratorNext
         *
         * @param entry entry
         * @return object object
         */
        @Override
        protected Object iteratorNext(Map.Entry entry) {
            return entry.getKey();
        }
    }

    /**
     * CollectionView
     */
    private abstract class CollectionView implements Collection {
        /**
         * CollectionView
         */
        protected CollectionView() {
        }

        /**
         * get
         *
         * @param var1 map
         * @return collection collection
         */
        protected abstract Collection get(Map var1);

        /**
         * iteratorNext
         *
         * @param var1 entry
         * @return object object
         */
        protected abstract Object iteratorNext(Map.Entry var1);

        /**
         * clear
         */
        @Override
        public void clear() {
            if (FastHashMap.this.fast) {
                synchronized (FastHashMap.this) {
                    FastHashMap.this.map = new HashMap();
                }
            } else {
                synchronized (FastHashMap.this.map) {
                    this.get(FastHashMap.this.map).clear();
                }
            }

        }

        /**
         * remove
         *
         * @param o o
         * @return boolean boolean
         */
        @Override
        public boolean remove(Object o) {
            if (FastHashMap.this.fast) {
                synchronized (FastHashMap.this) {
                    HashMap temp = (HashMap) FastHashMap.this.map.clone();
                    boolean r = this.get(temp).remove(o);
                    FastHashMap.this.map = temp;
                    return r;
                }
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).remove(o);
                }
            }
        }

        /**
         * removeAll
         *
         * @param o o
         * @return boolean boolean
         */
        @Override
        public boolean removeAll(Collection o) {
            if (FastHashMap.this.fast) {
                synchronized (FastHashMap.this) {
                    HashMap temp = (HashMap) FastHashMap.this.map.clone();
                    boolean r = this.get(temp).removeAll(o);
                    FastHashMap.this.map = temp;
                    return r;
                }
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).removeAll(o);
                }
            }
        }

        /**
         * retainAll
         *
         * @param o o
         * @return boolean boolean
         */
        @Override
        public boolean retainAll(Collection o) {
            if (FastHashMap.this.fast) {
                synchronized (FastHashMap.this) {
                    HashMap temp = (HashMap) FastHashMap.this.map.clone();
                    boolean r = this.get(temp).retainAll(o);
                    FastHashMap.this.map = temp;
                    return r;
                }
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).retainAll(o);
                }
            }
        }

        /**
         * size
         *
         * @return int int
         */
        @Override
        public int size() {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).size();
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).size();
                }
            }
        }

        /**
         * isEmpty
         *
         * @return boolean boolean
         */
        @Override
        public boolean isEmpty() {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).isEmpty();
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).isEmpty();
                }
            }
        }

        /**
         * contains
         *
         * @param o o
         * @return boolean boolean
         */
        @Override
        public boolean contains(Object o) {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).contains(o);
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).contains(o);
                }
            }
        }

        /**
         * addAll
         *
         * @param o c
         * @return boolean boolean
         */
        @Override
        public boolean containsAll(Collection o) {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).containsAll(o);
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).containsAll(o);
                }
            }
        }

        /**
         * toArray
         *
         * @param o o
         * @return object[] object[]
         */
        @Override
        public Object[] toArray(Object[] o) {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).toArray(o);
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).toArray(o);
                }
            }
        }

        /**
         * toArray
         *
         * @return object[] object[]
         */
        @Override
        public Object[] toArray() {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).toArray();
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).toArray();
                }
            }
        }

        /**
         * equals
         *
         * @param o o
         * @return boolean boolean
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).equals(o);
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).equals(o);
                }
            }
        }

        /**
         * hashCode
         *
         * @return int int
         */
        @Override
        public int hashCode() {
            if (FastHashMap.this.fast) {
                return this.get(FastHashMap.this.map).hashCode();
            } else {
                synchronized (FastHashMap.this.map) {
                    return this.get(FastHashMap.this.map).hashCode();
                }
            }
        }

        /**
         * add
         *
         * @param o o
         * @return boolean boolean
         */
        @Override
        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        /**
         * addAll
         *
         * @param c c
         * @return boolean boolean
         */
        @Override
        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        /**
         * iterator
         *
         * @return iterator iterator
         */
        @Override
        public Iterator iterator() {
            return new CollectionViewIterator();
        }

        /**
         * private class CollectionViewIterator
         */
        private class CollectionViewIterator implements Iterator {
            /**
             * expected
             */
            private Map expected;
            /**
             * lastReturned
             */
            private Map.Entry lastReturned = null;
            /**
             * iterator
             */
            private Iterator iterator;

            /**
             * CollectionViewIterator
             */
            protected CollectionViewIterator() {
                this.expected = FastHashMap.this.map;
                this.iterator = this.expected.entrySet().iterator();
            }

            /**
             * hasNext
             *
             * @return boolean boolean
             */
            @Override
            public boolean hasNext() {
                if (this.expected != FastHashMap.this.map) {
                    throw new ConcurrentModificationException();
                } else {
                    return this.iterator.hasNext();
                }
            }

            /**
             * next
             *
             * @return object
             */
            @Override
            public Object next() {
                if (this.expected != FastHashMap.this.map) {
                    throw new ConcurrentModificationException();
                } else {
                    this.lastReturned = (Map.Entry) this.iterator.next();
                    return CollectionView.this.iteratorNext(this.lastReturned);
                }
            }

            /**
             * remove
             */
            @Override
            public void remove() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                } else {
                    if (FastHashMap.this.fast) {
                        synchronized (FastHashMap.this) {
                            if (this.expected != FastHashMap.this.map) {
                                throw new ConcurrentModificationException();
                            }

                            FastHashMap.this.remove(this.lastReturned.getKey());
                            this.lastReturned = null;
                            this.expected = FastHashMap.this.map;
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
