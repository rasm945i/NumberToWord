package dk.rasmusbendix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
        StringBuilder builder = new StringBuilder();
        for(Number number : nums) {
            builder.append(number.getNumber());
        }

        System.out.println("Numbers: " + builder.toString());
        ArrayList<String> words = bruteForce(builder.toString(), rightChars);
        for(String match : words) {
            System.out.println("Match: " + match);
        }

        return null;

    }

    public ArrayList<String> bruteForce(String inputNumber, ArrayList<String> wordList) {

        ArrayList<String> result = new ArrayList<>();
        HashMap<String, String> numberToWordMap = new HashMap<>();

        for(String word : wordList) {
            StringBuilder  builder = new StringBuilder();
            ArrayList<Number> nums = wordToNumList(word);
            for(Number num : nums) {
                builder.append(num.getNumber());
            }
            numberToWordMap.put(word, builder.toString());
        }

        for(String word : numberToWordMap.keySet()) {
//            System.out.println("Comparing: " + numberToWordMap.get(word) + " " + inputNumber);
            if(inputNumber.equalsIgnoreCase(numberToWordMap.get(word)))
                result.add(word);
        }

        return result;

    }

    public WordConstructor constructWord(ArrayList<Number> nums) {

        WordConstructor constructor = new WordConstructor();
        for(Number num : nums) {
            constructor.append(num.getChar());
        }

        return constructor;

    }

    public boolean isWord(ArrayList<Number> nums) {
        return isWord(constructWord(nums).getWord());
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
            list.add(numberMap.get(Integer.parseInt(String.valueOf(ch))).clone());
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
