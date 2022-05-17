package treasure.alg.top100;

/**
 * 啪一下，很快啊，直接打开答案
 *
 * */
public class _208_trie {
    
    class Trie {
        
        Trie[] children = new Trie[26];
        boolean leaf;
        
        public void insert(String word) {
            Trie trie = this;
            for (int i = 0; i < word.length(); i++) {
                int idx = word.charAt(i) - 'a';
                if (trie.children[idx] == null) {
                    trie.children[idx] = new Trie();
                }
                trie = trie.children[idx];
            }
            trie.leaf = true;
        }
        
        public boolean search(String word) {
            Trie node = searchNode(word);
            return node != null && node.leaf;
        }
        
        public boolean startsWith(String prefix) {
            return searchNode(prefix) != null;
        }
        
        Trie searchNode(String word) {
            Trie node = this;
            for (int i = 0; i < word.length(); i++) {
                int idx = word.charAt(i) - 'a';
                if (node.children[idx] == null) {
                    return null;
                }
                node = node.children[idx];
            }
            return node;
        }
    }
    
    
}
