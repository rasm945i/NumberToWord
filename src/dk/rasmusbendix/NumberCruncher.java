package dk.rasmusbendix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.stream.Stream;

public class NumberCruncher {

    private LinkedHashSet<String> wordlist;
    private HashMap<Integer, Number> numberMap;
    private LinkedHashSet<String> alreadyPicked;

    public NumberCruncher() {
        this.wordlist = loadWordlist();
        this.numberMap = new HashMap<>();
        this.alreadyPicked = new LinkedHashSet<>();

        numberMap.put(0, new Number(0, ' '));
        numberMap.put(1, new Number(1, '_', ',', '@'));
        numberMap.put(2, new Number(2, 'a', 'b', 'c'));
        numberMap.put(3, new Number(3, 'd', 'e', 'f'));
        numberMap.put(4, new Number(4, 'g', 'h', 'i'));
        numberMap.put(5, new Number(5, 'j', 'k', 'l'));
        numberMap.put(6, new Number(6, 'm', 'n', 'o'));
        numberMap.put(7, new Number(7, 'p', 'q', 'r', 's'));
        numberMap.put(8, new Number(8, 't', 'u', 'v'));
        numberMap.put(9, new Number(9, 'w', 'x', 'y', 'z'));

    }

    private LinkedHashSet<String> loadWordlist() {
        long start = System.currentTimeMillis();
        System.out.println("Loading wordlist...");

        LinkedHashSet<String> list = new LinkedHashSet<>();
        Path path = Paths.get("S:\\words_alpha.txt");

        try(Stream<String> lines = Files.lines(path)) {

            lines.forEach(list::add);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Wordlist took " + (System.currentTimeMillis() - start) + "ms to load");
        System.out.println("List contains " + list.size() + " words.");
        return list;

    }

    public WordConstructor findMatch(String numeric) {

        // Get all words with the right amount of characters
        ArrayList<String> rightChars = new ArrayList<>();
        int matchLength = numeric.length();
        for(String str : wordlist) {
            if(str.length() == matchLength)
                rightChars.add(str);
        }

        System.out.println("Found " + rightChars.size() + " words of length '" + matchLength + "'");
        System.out.println("Searching ...");

        ArrayList<Number> nums = numbersToNumList(numeric);
        WordConstructor word = new WordConstructor();
        int currentWordIndex = 0;

        ArrayList<WordConstructor> words = test(nums);
        for(WordConstructor match : words) {
            System.out.println("Match: " + match.getWord());
        }

        return null;

    }

    public ArrayList<WordConstructor> bruteForce(ArrayList<Number> nums, ArrayList<WordConstructor> result, int index) {

        System.out.println("INDEX: " + index);
        WordConstructor constructor = constructWord(nums);
        Number current = nums.get(index);

        for(int i = index; i < nums.size(); i++) {

        }


        if(!current.next()) {
            index -= 1;
            if(index < 0)
                return result;

            System.out.println("Resetting remaining numbers.");
            for(int i = index; i < nums.size(); i++) {
                nums.get(i).resetIndex();
            }
        }

        System.out.println("Recursive call");
        return bruteForce(nums, result, index);

    }

    public ArrayList<WordConstructor> test(ArrayList<Number> nums) {

        ArrayList<WordConstructor> words = new ArrayList<>();
        for(int i = nums.size()-1; i > 0; i--) {
            for(int j = i; j < nums.size(); j++) {
                System.out.println("Resetting index " + j);
                nums.get(j).resetIndex();
            }
            words.addAll(doFlipping(nums, new ArrayList<>(), i));
        }

        return words;

    }

    public ArrayList<WordConstructor> doFlipping(ArrayList<Number> nums, ArrayList<WordConstructor> result, int index) {

        for(int i = index; i < nums.size(); i++) {

            Number number = nums.get(i);
            WordConstructor wc = constructWord(nums);
            if(isWord(wc.getWord()))
                result.add(wc);

            if(number.next())
                doFlipping(nums, result, i);

        }

        return result;

    }

    public ArrayList<WordConstructor> recursive2(ArrayList<Number> nums, ArrayList<WordConstructor> result, int index) {

        System.out.println("Index: " + index);
        for(int i = index; i < nums.size(); i++) {
            boolean success = nums.get(i).next();
            WordConstructor wordConstructor = constructWord(nums);
            if(isWord(wordConstructor.getWord()))
                result.add(wordConstructor);
            if(success)
                recursive2(nums, result, i);
        }

        if(nums.get(index).isDone()) {
            index -= 1;
        }

        if(index < 0)
            return result;

        System.out.println();
        return recursive2(nums, result, index);

    }

    public boolean evaluate(ArrayList<Number> nums, WordConstructor constructor) {
        constructor = constructWord(nums);
        System.out.println("Evaluating: " + constructor.getWord());
        return isWord(constructor.getWord());
    }

    public WordConstructor constructWord(ArrayList<Number> nums) {

        WordConstructor constructor = new WordConstructor();
        for(Number num : nums) {
            constructor.append(num.getChar());
        }

        return constructor;

    }


    public WordConstructor rec(ArrayList<Number> nums, WordConstructor current, int nextIndex) {

        Number number = nums.get(nextIndex);
        current.append(number.getChar());
        boolean isWord = false;
        if(!isWord(current.getWord())) {

            while(number.next()) {
                current.replaceLatest(number.getChar());
                if(isWord(current.getWord())) {
                    System.out.println("It is a word, " + current.getWord());
                    isWord = true;
                    break;
                }
            }

        }

        String word = current.getWord();
        if(isWord && word.length() == nums.size()) {
            System.out.println("Correct size, " + word.length());
            return current;
        }

        System.out.println("Iteration at index " + nextIndex);
        if(nextIndex >= nums.size())
            return null;

        return rec(nums, current, nextIndex+1);

    }

    public boolean isWord(String word) {
        System.out.println("Evaluating word: " + word);
        for(String str : wordlist) {
            if(str.equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

    public ArrayList<Number> numbersToNumList(String nums) {

        ArrayList<Number> list = new ArrayList<>();

        for(char ch : nums.toCharArray()) {
            if(!Character.isDigit(ch))
                throw new NumberFormatException("The input must be a number, but it contained '" + ch + "'");
            System.out.print(ch + ".. ");
            list.add(numberMap.get(Integer.parseInt(String.valueOf(ch))));
        }

        System.out.println();
        return list;

    }

    // Not tested
    public ArrayList<Number> wordToNumList(String word) {

        ArrayList<Number> list = new ArrayList<>();

        for(char ch : word.toCharArray()) {

            for(Number num : numberMap.values()) {
                if(num.contains(ch)) {
                    list.add(num);
                    break;
                }
            }

        }

        return list;

    }

}
