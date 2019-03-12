package dsacoursework2;

import static dsacoursework2.DictionaryFinder.readWordsFromCSV;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author conor
 */
public class AutoCompletion {
    
    /**
     * Splits the CSV that has the dict and freq into ArrayList
     * stores item was Words,freq
     * @param file dict file
     * @return ArrayList
     * @throws FileNotFoundException cant find the file
     */
    public static ArrayList<String> toList(String file)
                                                throws FileNotFoundException{
        Scanner sc=new Scanner(new File(file));
        //spits the file at the new line, leaving the commas in place
        sc.useDelimiter("\n");
        ArrayList<String> result = new ArrayList<>();
        String str;
        while(sc.hasNext()){
            str=sc.next();
            str=str.trim();
            str=str.toLowerCase();
            result.add(str);
        }
        
        return result;
    }
    
    /**
     * Takes the dict and adds it to a Trie ready for auto completion
     * @param file the dict file with Words and wordFreq
     * @return Trie will words filled in
     * @throws Exception 
     */
    public static AutoCompletionTrie dictToTrie(String file) throws Exception{
        AutoCompletionTrie result = new AutoCompletionTrie();
        ArrayList<String> words=toList(file);
        //loops though amount of words 
        for (int i = 0; i < words.size(); i++) {
            //splits word and freq up storing them in an array [word,freq]
            String temp = words.get(i);
            String[] parts = temp.split(",");
            String part1 = parts[0];
            //parse freq to int for calc
            int part2 = Integer.parseInt(parts[1]);
            //adds the word freq amount of times. e.g if word has freq of
            //5 word would be added 5 times.
            for (int j = 0; j < part2; j++) {
                result.add(part1);
            }
        }
        return result;
    }
    
    /**
     * Takes the list of Prefixes to be seached and a Trie to be searched from
     * adds counts the main sums and passes it to a probabilty calculator
     * @param prefixList list of prefixes to be searched
     * @param trie Trie to be searched
     * @throws IOException  
     */
    public static void prefix(List<String> prefixList, AutoCompletionTrie trie) 
                                                            throws IOException{
        
        String result = "";
        //loops the amount of prefixs 
        for (int i = 0; i < prefixList.size(); i++) {
            
            //store prefix in a string
            String prefix = prefixList.get(i);
            System.out.println("prefix = " + prefix);
            //creates SubTrie from prefix
            AutoCompletionTrie subTrie = trie.getSubTrie(prefix);
            
            List<String> allWords = subTrie.getAllWords();

            Map<String, Integer> totals = new HashMap<>();
            //stores how many words are in subtrie for probability 
            int freqSum = 0;

            //works out the prefix String here, making sure words like
            //THE are not ignored.
            if (subTrie.freq("") > 0) {
                totals.put(prefix, subTrie.freq(""));
                freqSum += subTrie.freq("");
            }

            //try loop though all subTrie, catch if no offspring.
            try{
                //loops though all subtrie words, storing in a map for calc
                for (int j = 0; j < allWords.size(); j++) {
                    totals.put(prefix + allWords.get(j), subTrie.freq(allWords.get(j)));
                    freqSum += subTrie.freq(allWords.get(j));
                }
            }catch(java.lang.NullPointerException exception){
            }
            //adds probability result to result.
            result += probability(totals, prefix, freqSum) + "\n";

            System.out.println("");
            
            //save result to file.
            saveToFile(result);
        }
    }
    
    /**
     * Sorts Map so Freq is acending
     * @param map map that needs sorting
     * @return return a sorted list
     */
    public static List<Entry<String, Integer>> sort(Map<String, Integer> map){
        Set<Entry<String, Integer>> set = map.entrySet();
        List<Entry<String, Integer>> list = 
                                new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>(){
                @Override
                public int compare( Map.Entry<String, Integer> o1, 
                                                Map.Entry<String, Integer> o2 )
                {
                    return (o2.getValue()).compareTo( o1.getValue() );
                }
            } 
        );
        
        return list;
    }
    
    /**
     * works out the probability of the word. 
     * @param map contains K=Word, V=Freq
     * @param prefix the prefix that is current being used
     * @param sum sum of all words in Prefix
     * @return String sorted as Prefix, Word, Probability
     */
    public static String probability
                        (Map<String, Integer> map, String prefix, int sum){
        //sorts the list Freq ascending
        List<Entry<String, Integer>> list = sort(map);
        //result has prefix to start with
        String result = prefix;
        int amountOfResults = 5;
        
        //if  list has less than 5, only loop that amount of time
        //else loop 5 times
        if (list.size() < amountOfResults) {
            for (int i = 0; i < list.size(); i++) {
                //probability = word Freq / words in document
                double probability = (double)list.get(i).getValue() / sum;
                probability = Math.ceil(probability* 10000) / 10000;
                
                //adds to result, the key and prob
                result += "," + list.get(i).getKey() + "," + probability;
            }
        }else{
            for (int i = 0; i < amountOfResults; i++) {
                double probability = (double)list.get(i).getValue() / sum;
                probability = Math.ceil(probability* 10000) / 10000;
                result += "," + list.get(i).getKey() + "," + probability;
            }
        }
        
        System.out.println(result);
        return result;
    }
    
    /**
     * Saves to file
     * @param toSave
     * @throws IOException 
     */
    public static void saveToFile(String toSave) throws IOException {
       FileWriter fileWriter = new FileWriter("lotrMatches.csv");
       PrintWriter printWriter = new PrintWriter(fileWriter);

       printWriter.println(toSave);
       
       printWriter.close();
    }
    
    public static void main(String[] args) throws Exception {
        //Creates a new dictonary file from CSV file
        DictionaryFinder df=new DictionaryFinder();
        ArrayList<String> in=readWordsFromCSV("lotr.csv");
        df.formDictionary(in);
        
        //Make Trie and fill it with csv file data
        AutoCompletionTrie trie = dictToTrie("dict.csv");
        ArrayList<String> queries=toList("lotrQueries.csv");
        
        AutoCompletion autoCompletion = new AutoCompletion();
        autoCompletion.prefix(queries, trie);

    }
}
