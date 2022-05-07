````java
static final class Node<K,V> {
    final K key; // currently, never detached
    V val;
    Node<K,V> next;
    Node(K key, V value, Node<K,V> next) {
        this.key = key;
        this.val = value;
        this.next = next;
    }
}

static final class Index<K,V> {
    final Node<K,V> node;  // currently, never detached
    final Index<K,V> down;
    Index<K,V> right;
    Index(Node<K,V> node, Index<K,V> down, Index<K,V> right) {
        this.node = node;
        this.down = down;
        this.right = right;
    }
}
````

