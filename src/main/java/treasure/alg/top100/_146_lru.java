package treasure.alg.top100;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class _146_lru {
    
    /**
     * 不好意思。。不会搞
     *
     * 干！
     *
     * */
    public static class my {
        
        public static class LRUCache {
            
            LinkedList<Integer> list = new LinkedList<>();
            Map<Integer, Integer> map = new HashMap<>();
            int size;
            public LRUCache(int capacity) {
                size = capacity;
            }
        
            public int get(int key) {
                return 0;
            }
        
            public void put(int key, int value) {
            
            }
        }
    }
    
    public static class solution {
        
        public static class LRUCache {
            static class Node {
                Node prev, next;
                int val;
                int key;
            }
            Node head, tail;
            Map<Integer, Node> map = new HashMap<>();
            int maxSize;
            int curSize;
            public LRUCache(int capacity) {
                maxSize = capacity;
                head = new Node();
                tail = new Node();
                head.next = tail;
                tail.prev = head;
            }
            
            public int get(int key) {
                if (!map.containsKey(key))
                    return -1;
    
                Node node = map.get(key);
                move1(node);
                return node.val;
            }
            
            public void put(int key, int value) {
                Node node = map.get(key);
                if (node == null) {
                    node = new Node();
                    node.val = value;
                    node.key = key;
                    map.put(key, node);
                    add(node);
                    curSize++;
                    
                    if (curSize > maxSize) {
                        node = removeTail();
                        map.remove(node.key);
                        curSize--;
                    }
                    
                } else {
                    node.val = value;
                    move1(node);
                }
            }
            
            void move1(Node node) {
                remove(node);
                add(node);
            }
            
            void remove(Node node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
    
            Node removeTail() {
                Node res = tail.prev;
                remove(res);
                return res;
            }
            
            void add(Node node) {
                node.next = head.next;
                head.next.prev = node;
                node.prev = head;
                head.next = node;
            }
        }
    }
}
