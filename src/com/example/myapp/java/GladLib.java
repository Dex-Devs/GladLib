package com.example.myapp.java;

import edu.duke.*;
import java.util.*;

// TO-DO : MAKE THIS A HASHMAP VERSION
public class GladLib {
        
        // replace with HashMap<String for label, ArrayList<String> for list of labels>
	private ArrayList<String> adjectiveList;
	private ArrayList<String> nounList;
	private ArrayList<String> colorList;
	private ArrayList<String> countryList;
	private ArrayList<String> nameList;
	private ArrayList<String> animalList;
	private ArrayList<String> timeList;
	private ArrayList<String> fruitList;
	private ArrayList<String> verbList;
        private ArrayList<String> wordsOccured;
	//
        
        // add HashMap<String, String> for label and resource directory
	private Random myRandom;
	
	private static String dataSourceURL = "http://dukelearntoprogram.com/course3/data";
	private static String dataSourceDirectory = "com/example/myapp/data"; // remove
	
        // MODIFY
	public GladLib(){
		initializeFromSource(dataSourceDirectory);
		myRandom = new Random();
                wordsOccured = new ArrayList<>();
                
	}
	
	public GladLib(String source){
		initializeFromSource(source);
		myRandom = new Random();
	}
	
	private void initializeFromSource(String source) {
            
        // MODIFY
		adjectiveList= readIt(source+"/adjective.txt");	
		nounList = readIt(source+"/noun.txt");
		colorList = readIt(source+"/color.txt");
		countryList = readIt(source+"/country.txt");
		nameList = readIt(source+"/name.txt");		
		animalList = readIt(source+"/animal.txt");
		timeList = readIt(source+"/timeframe.txt");
                fruitList = readIt(source+"/fruit.txt");
                verbList = readIt(source+"/verb.txt");
	}
	
	private String randomFrom(ArrayList<String> source){
		int index = myRandom.nextInt(source.size());
		return source.get(index);
	}
	
	private String getSubstitute(String label) {
		if (label.equals("country")) {
			return randomFrom(countryList);
		}
		if (label.equals("color")){
			return randomFrom(colorList);
		}
		if (label.equals("noun")){
			return randomFrom(nounList);
		}
		if (label.equals("name")){
			return randomFrom(nameList);
		}
		if (label.equals("adjective")){
			return randomFrom(adjectiveList);
		}
		if (label.equals("animal")){
			return randomFrom(animalList);
		}
		if (label.equals("timeframe")){
			return randomFrom(timeList);
		}
		if (label.equals("verb")){
			return randomFrom(verbList);
		}
		if (label.equals("fruit")){
			return randomFrom(fruitList);
		}
		if (label.equals("number")){
			return ""+myRandom.nextInt(50)+5;
		}
		
		return "**UNKNOWN**";
	}
	
	public String processWord(String w){
                
		int first = w.indexOf("<");
		int last = w.indexOf(">",first);
		if (first == -1 || last == -1){ // no indicator found
			return w;
		}
		String prefix = w.substring(0,first); // text from beginning to the indicator
		String suffix = w.substring(last+1); // from the last indicator to the end of text
                String category = w.substring(first+1,last); // get category betweeng indicators
		String sub = getSubstitute(category); // get substitute
                
                // substitute must not be in occurence 
                while(wordsOccured.contains(sub)){
                    sub = getSubstitute(category); // select other substitute
                }
                
                wordsOccured.add(sub); // add words not yet on word occured list
               
//                while(true) {
//                               
//                if ( !wordsOccured.contains(sub)) { // FALSE - > TRUE -> add to list
//                    wordsOccured.add(sub);
//                    break;
//                } 
//                    sub = getSubstitute(category); // existing, select other
//                }
		return prefix+sub+suffix;
	}
        
      
	
	private void printOut(String s, int lineWidth){
		int charsWritten = 0;
		for(String w : s.split("\\s+")){
			if (charsWritten + w.length() > lineWidth){
				System.out.println();
				charsWritten = 0;
			}
			System.out.print(w+" ");
			charsWritten += w.length() + 1;
		}
	}
	
	private String fromTemplate(String source){
		String story = "";
		if (source.startsWith("http")) {
			URLResource resource = new URLResource(source);
			for(String word : resource.words()){
				story = story + processWord(word) + " ";
			}
		}
		else {
			FileResource resource = new FileResource(source);
			for(String word : resource.words()){
				story = story + processWord(word) + " ";
			}
		}
		return story;
	}
	
	private ArrayList<String> readIt(String source){
		ArrayList<String> list = new ArrayList<String>();
		if (source.startsWith("http")) {
			URLResource resource = new URLResource(source);
			for(String line : resource.lines()){
				list.add(line);
			}
		}
		else {
			FileResource resource = new FileResource(source);
			for(String line : resource.lines()){
				list.add(line);
			}
		}
		return list;
	}
	
	public void makeStory(){
            wordsOccured.clear(); // clear list of words occured
            
	    System.out.println("\n");
		String story = fromTemplate("com/example/myapp/data/madtemplate2.txt");
		printOut(story, 30);
                System.out.println("\n\nTotal number of words that were replaced: " + wordsOccured.size());
	}
        
        public ArrayList<String> getOccurences() { 
            return this.wordsOccured;
        }
        
        public static void main(String [] args) {
            GladLib glad = new GladLib();
            glad.makeStory();
          
            
            System.out.println("ALL WORDS");
            for(String words : glad.getOccurences()){
                System.out.println((glad.getOccurences().indexOf(words)+1) + " " + words);
            }
             
            
        }

}
