package dsacoursework2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author conor
 */
public class AutoCompletionTrie {
    protected TrieNode root;
    //These two vars are helpers for Depth First
    Stack<String> stack = new Stack<>();
    static int stackAmount = 0;

    /**
     * Constructor for Trie class
     */
    public AutoCompletionTrie(){
        root = new TrieNode();
    }
    
    /**
     * Add a word into the Trie Data structure
     * @param word Word that is to be added to Trie
     */
    public void add(String word){
        TrieNode node = root;
        //loops though length of String
        for (int i = 0; i < word.length(); i++) {
            //gets char at current loop index,
            char c = word.charAt(i);
            //subtracts 'a' (lowercase) from current char, to access
            //TriNode index.
            int index = c - 'a';
            //if array char pos is null, the words has not previously been added
            if (node.arr[index]==null) {
                //makes new temp to hold current char.
                TrieNode temp = new TrieNode();
                node.arr[index]=temp;
                //sets this TrideNode index to temp once char is stored.
                node = temp;
            }else{
                node=node.arr[index];
            }
        }
        node.addFreq();
        node.isEnd = true;
    }
    
    /**
     * Checks if word is in trie
     * @param word Word that wanted to be checked
     * @return If word is found, Return true
     */
    public boolean contains(String word){
        //sets p to root trie
        TrieNode p = root;
        //loops though string argument
        for (int i = 0; i < word.length(); i++) {
            //gets char at current loop index,
            char c = word.charAt(i);
            //subtracts 'a' (lowercase) from current char, to access
            //TriNode index
            int index = c - 'a';
            //check if current index in the trie is empty
            if(p.arr[index]!=null){
                p = p.arr[index];
            }else{
                return false;
            }
        }
        //checks if the word is the root.
        if (p==root) {
            return false;
        }
        //returns the isEnd boolean.
        return p.isEnd;
    }
    
    /**
     * gets the freq of a word in the Trie
     * @param word the word to be searched
     * @return freq of word
     */
    public int freq(String word){
        int result = 0;
        TrieNode p = root;
        //loops though string argument
        for (int i = 0; i < word.length(); i++) {
            //gets char at current loop index,
            char c = word.charAt(i);
            //subtracts 'a' (lowercase) from current char, to access
            //TriNode index
            int index = c - 'a';
            //check if current index in the trie is empty
            if(p.arr[index]!=null){
                p = p.arr[index];
            }else{
                return 0;
            }
        }
        return p.freq;
    }
    
    /**
     * Print the Trie by traversing the Breadth First
     * @return The trie Breadth First
     */
    public String outputBreadthFirstSearch(){
        TrieNode node = root;
        
        Queue<TrieNode> nodeQueue = new LinkedList<>();
        String result = "";
        
        //Gets first round of queue data
        for (int i = 0; i < node.arr.length; i++) {
            if (node.arr[i] != null) {
                result += String.valueOf(toChar(i));
                nodeQueue.add(node.arr[i]);
            }
        }
        
        //loops though queue list until empty
        while(nodeQueue.size() > 0){
            node= nodeQueue.remove();
            for (int i = 0; i < node.arr.length; i++) {
                if (node.arr[i] != null) {
                    //queue.add(i);
                    result += String.valueOf(toChar(i));
                    nodeQueue.add(node.arr[i]);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Output the Depth first of the Trie, 
     * Recursive, calls same method, but has extra params
     * @return String of Trie (Depth First)
     */
    public String outputDepthFirstSearch(){
        StringBuilder sb = new StringBuilder();
        String result = "";
        
        for (int j = 0; j < root.arr.length; j++) {
            if (root.arr[j] != null) {
                outputDepthFirstSearch(root.arr[j], result);
                stack.push(String.valueOf(toChar(j)));
                stackAmount++;
            }
        }
        
        if (stack.size() == stackAmount) {
            //System.out.println("STACK IS :" + stack.size());
            int size = stack.size();
        
            for (int i = 0; i < size; i++) {
                sb.insert(0, stack.pop());
                //result += stack.pop();
            }
            result = sb.toString();
            //System.out.println(result);
        }
        stackAmount = 0;
        return result;
    }
    
    /**
     * Private method, does the same as first, but is the main work horse for
     * recursive method, 
     * @param trie the current node
     * @param result previous String
     * @return new string after prefix is added.
     */
    private String outputDepthFirstSearch(TrieNode trie, String result){
        for (int j = 0; j < trie.arr.length; j++) {
            if (trie.arr[j] != null) {
                outputDepthFirstSearch(trie.arr[j], result);
                stack.push(String.valueOf(toChar(j)));
                stackAmount++;
                //result += result + stack.pop();
            }
        }
                
        return result;
    }
   
    /**
     * creates a subtree based off the prefix, 
     * @param prefix the prefix you want to make a subtree from
     * @return a new trie 
     */
    public AutoCompletionTrie getSubTrie(String prefix){
        TrieNode node = root;
        //System.out.println("prefix = " + prefix);
        
        AutoCompletionTrie result = new AutoCompletionTrie();
        
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            int index = c - 'a';
            
            if (node.arr[index]==null) {
                return null;
            }else{
                node=node.arr[index];
            }
        }
        result.root = node;

        
        return result;
    }
    
    /**
     * Gets all words from Trie
     * @return a List of Strings 
     */
    public List<String> getAllWords(){
        List<String> result = new ArrayList<>();
        for (int i = 0; i < root.arr.length; i++) {
            if (root.arr[i] != null) {
                getAllWords(result, String.valueOf(toChar(i)) + "", root.arr[i]);
            }
        }
        if (result.isEmpty()) {
            return null;
        }else{
            return result;
        }
        
    }
    /**
     * Gets all the words from a Trie but the recursive bit
     * @param result the current List to be added too
     * @param c the word (char) to be added to list
     * @param n current Trie Node
     */
    private void getAllWords(List<String> result, String c, TrieNode n) {
        if (n.isEnd) {
            result.add(c);
        }
        
        for (int i = 0; i < n.arr.length; i++) {
            if (n.arr[i] != null) {
                getAllWords(result, c + String.valueOf(toChar(i)), n.arr[i]);
            }
        }
    }
    
    /**
     * A helper function to convert an Index to Char
     * @param index an int value to be converted
     * @return char
     */
    public char toChar(int index){
        char letter = (char)(index + 'a');
        return letter;
    }
    
//    public static void main(String[] args) {
//    
//        
//        //Part 1:
//        Trie trie = new Trie();
//        //Example given in document
//        trie.add("cheers");
//        trie.add("cheese");
//        trie.add("chat");
//        trie.add("cat");
//        trie.add("bat");
//        
//        //Part 2:
//        String word = "bat";
//        System.out.println(word + " = " + trie.contains(word));
//        word = "at";
//        System.out.println(word + " = " + trie.contains(word));
//        word = "cheese";
//        System.out.println(word + " = " + trie.contains(word));
//        
//        //Part 3:
//        System.out.println("Breadth First:::: " + trie.outputBreadthFirstSearch());
//        
//        //Part 4:
//        System.out.println("Depth First:::: " + trie.outputDepthFirstSearch());
//        
//        //Part 5:
//        //Subtree made the same as Document
//        System.out.println("\n ---- New Sub Trie created ---- \n");
//        Trie subTrie = trie.getSubTrie("ch");
//        //Same tests as original Trie
//        word = "at";
//        System.out.println(word + " = " + subTrie.contains(word));
//        word = "bat";
//        System.out.println(word + " = " + subTrie.contains(word));
//        word = "eese";
//        System.out.println(word + " = " + subTrie.contains(word));
//        
//        System.out.println("Breadth First:::: " + subTrie.outputBreadthFirstSearch());
//        System.out.println("Depth First:::: " + subTrie.outputDepthFirstSearch());
//        
//        //Part 6:
//        System.out.println("\n ---- All words ---- \n");
//        //Original Tree
//        List<String> trieResult = trie.getAllWords();
//        System.out.println("From Trie:: " + trieResult);
//        //Sub Tree
//        List<String> subTrieResult = subTrie.getAllWords();
//        System.out.println("From Sub Trie:: " + subTrieResult);
//        
//    }
}
