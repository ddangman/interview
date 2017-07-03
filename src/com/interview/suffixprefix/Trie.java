package com.interview.suffixprefix;

import java.util.HashMap;
import java.util.Map;

/**
 * Date 04/25/2016
 * @author Tushar Roy
 *
 * Insert/delete/search into trie data structure
 *
 * Reference
 * https://en.wikipedia.org/wiki/Trie
 */
public class Trie {

    private class TrieNode {
        Map<Character, TrieNode> children;
        boolean endOfWord;
        public TrieNode() {
            children = new HashMap<>();
            endOfWord = false;
        }
    }

    private final TrieNode root;
    // initialize root in constructor
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Iterative implementation of insert into trie
     */
    public void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode nextNode = current.children.get(ch);
            if (nextNode == null) { // check if char is already in map
                // char does not exist, generate and add node to parent
                nextNode = new TrieNode();
                current.children.put(ch, nextNode);
            }
            current = nextNode; // traverse to nextNode
        }
        //last character reached
        //mark the current nodes endOfWord as true
        current.endOfWord = true;
    }

    /**
     * Recursive implementation of insert into trie
     */
    public void insertRecursive(String word) {
        insertRecursive(root, word, 0);
    }


    private void insertRecursive(TrieNode current, String word, int index) {
        if (index == word.length()) {
            //if end of word is reached then mark endOfWord as true on current node
            current.endOfWord = true;
            return;
        }
        char ch = word.charAt(index);
        TrieNode nextNode = current.children.get(ch);

        //if node does not exists in map then create one and put it into map
        if (nextNode == null) {
            nextNode = new TrieNode();
            current.children.put(ch, nextNode);
        }
        insertRecursive(nextNode, word, index + 1);
    }

    /**
     * Iterative implementation of search into trie.
     */
    public boolean searchWhole(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode nextNode = current.children.get(ch);
            //if node does not exist for given char then return false
            if (nextNode == null) {
                return false;
            }
            current = nextNode; // traverse to nextNode
        }
        //return true of current's endOfWord is true else return false.
        // if searching whole word match, boolean matters
        // when only checking prefix, completion of loop should return true
        return current.endOfWord;
    }

        public boolean searchPrefix(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode nextNode = current.children.get(ch);
            //if node does not exist for given char then return false
            if (nextNode == null) {
                return false;
            }
            current = nextNode; // traverse to nextNode
        }
        // if searching whole word match, boolean matters
        // when only checking prefix, completion of loop should return true
        return true;
    }

    /**
     * Recursive implementation of search into trie.
     */
    public boolean searchRecursive(String word) {
        return searchRecursive(root, word, 0);
    }
    private boolean searchRecursive(TrieNode current, String word, int index) {
        if (index == word.length()) {
            //return true of current's endOfWord is true else return false.
            return current.endOfWord;
        }
        char ch = word.charAt(index);
        TrieNode nextNode = current.children.get(ch);
        //if node does not exist for given char then return false
        if (nextNode == null) {
            return false;
        }
        return searchRecursive(nextNode, word, index + 1);
    }

    /**
     * Delete word from trie.
     */
    public void delete(String word) {
        delete(root, word, 0);
    }

    /**
     * Returns true if parent should delete the mapping
     */
    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            //when end of word is reached only delete if current.endOfWord is true.
            if (!current.endOfWord) {
                // whole word is not matched
                return false;
            }
            // TrieNode not empty, do not delete node
            // change endOfWord to false so future searchWhole will return false
            // searchPrefix can still return true
            current.endOfWord = false;
            //if current has no other mapping then return true
            // so calling function can delete this reference from their map
            return current.children.size() == 0;
        }
        char ch = word.charAt(index);
        TrieNode nextNode = current.children.get(ch);
        if (nextNode == null) {
            // character not found, word does not exist
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(nextNode, word, index + 1);

        //if true is returned then delete the mapping of character and trienode reference from map.
        if (shouldDeleteCurrentNode) {
            current.children.remove(ch);
            //return true if no mappings are left in the map.
            // if this node has no other character, it will be deleted
            return current.children.size() == 0;
        }
        return false;
    }
}
