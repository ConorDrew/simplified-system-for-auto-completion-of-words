package dsacoursework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author ajb
 * 
 */
public class DictionaryFinder {
    
    public DictionaryFinder(){
    }
    /**
     * Reads all the words in a comma separated text document into an Array
     * @param f 
     */   
    public static ArrayList<String> readWordsFromCSV(String file) throws FileNotFoundException {
        Scanner sc=new Scanner(new File(file));
        sc.useDelimiter(" |,");
        ArrayList<String> words=new ArrayList<>();
        String str;
        while(sc.hasNext()){
            str=sc.next();
            str=str.trim();
            str=str.toLowerCase();
            words.add(str);
        }
        return words;
    }
     
    /**
     * forms a Dictionary checking if word is currently in map
     * if found increment value
     * @param in
     * @throws IOException
     */
    public void formDictionary(ArrayList<String> in) throws IOException{
       TreeMap<String, Integer> totals = new TreeMap<>();

       for (int i = 0; i < in.size(); i++) {
           String word = in.get(i);
           if (totals.containsKey(in.get(i))) {
               int count = totals.containsKey(word) ? totals.get(word) : 0;
               totals.put(word, count+1);
           }else{
               totals.put(word, 1);
           }
       }

       saveToFile(totals, "dict.csv");

    }

    /**
     *Saves to file
     * @param c
     * @param file
     * @throws IOException
     */
    public void saveToFile(TreeMap<String, Integer> c, String file) throws IOException {
       FileWriter fileWriter = new FileWriter(file);
       PrintWriter printWriter = new PrintWriter(fileWriter);

       for (int i = 0; i < c.size(); i++) {
           printWriter.println(c.keySet().toArray()[i] + 
                   "," + c.get(c.keySet().toArray()[i]));
       }
       printWriter.close();
    }
         
//    public static void main(String[] args) throws Exception {
//        DictionaryFinder df=new DictionaryFinder();
//        
//        ArrayList<String> in=readWordsFromCSV("lotr.csv");
//        
//        //DO STUFF TO df HERE in countFrequencies
//        df.formDictionary(in);  
//
//    }
    
}
