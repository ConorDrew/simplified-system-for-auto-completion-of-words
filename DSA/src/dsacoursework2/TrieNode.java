package dsacoursework2;

/**
 *
 * @author conor
 */
public class TrieNode {
    TrieNode[] arr;
    boolean isEnd;
    //For Q3
    int freq;
    
    //everytime a new node is made, give it an array of 26
    public TrieNode(){
        this.arr = new TrieNode[26];
    }
    
    //For Q3
    public void addFreq(){
        freq++;
    }
}
