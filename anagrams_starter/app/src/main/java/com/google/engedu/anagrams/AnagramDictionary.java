/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWord;


    public String sortLetters(String word){
        char[] sorted = word.toCharArray();
        Arrays.sort(sorted);
        return Arrays.toString(sorted);
    }

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        wordList = new ArrayList<>();
        lettersToWord = new HashMap<>();
        wordSet = new HashSet<>();
        sizeToWord = new HashMap<>();
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            // allows us to check if a word is valid in O(n)
            wordSet.add(word);
            if (sizeToWord.containsKey(word.length())){
                ArrayList<String> values = sizeToWord.get(word.length());
                values.add(word);
                sizeToWord.put(word.length(),values);
            }
            else {
                ArrayList<String> values = new ArrayList<>();
                values.add(word);
                sizeToWord.put(word.length(),values);
            }

            String sorted = sortLetters(word);
            if (lettersToWord.containsKey(sorted)){
                ArrayList<String> values = lettersToWord.get(sorted);
                values.add(word);
                lettersToWord.put(sorted,values);
            }
            else {
                ArrayList<String> values = new ArrayList<>();
                values.add(word);
                lettersToWord.put(sorted,values);
            }

        }

    }

    public boolean isGoodWord(String word, String base) {

        return wordSet.contains(word) && !word.toLowerCase().contains(base.toLowerCase()) ;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        // find all anagrams of target word in our string
        for (String word : wordList){
            if (word.length() == targetWord.length()){
                String sortWord = sortLetters(word);
                String sortTarget = sortLetters(targetWord);
                if (sortWord.equals(sortTarget)){
                    result.add(word);
                }

            }
        }

        // result now contains all the anagrams of the word
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(char c:alphabet){
            String newWord = word + c;
            String sorted = sortLetters(newWord);
           // Log.d("STATE", sorted);
            if (lettersToWord.containsKey(sorted)){
              //  Log.d("STATE", Arrays.toString(result.toArray()));
                result.addAll(lettersToWord.get(sorted));
            }

        }

        return result;
    }

    public String pickGoodStarterWord() {
        Random r = new Random();
        int index = r.nextInt(wordList.size()-10);
        int num_anagrams = 0;
        String word = wordList.get(index);
        int word_size = DEFAULT_WORD_LENGTH;
        while (num_anagrams < MIN_NUM_ANAGRAMS && word_size < MAX_WORD_LENGTH){
            ArrayList<String> values = sizeToWord.get(word_size);
            index = r.nextInt(values.size());
            while (num_anagrams < MIN_NUM_ANAGRAMS && index < values.size()) {
                word = wordList.get(index);
                num_anagrams = getAnagramsWithOneMoreLetter(word).size();
                index ++; //r.nextInt(values.size());
            }
            Log.d("Sized word",word );
            if( word_size < MAX_WORD_LENGTH) {
                word_size++;
            }
        }


        return word;
    }
}
