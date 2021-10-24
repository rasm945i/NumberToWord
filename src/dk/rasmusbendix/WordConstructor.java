package dk.rasmusbendix;

import java.util.LinkedList;

public class WordConstructor {

    private final LinkedList<Character> word;

    public WordConstructor() {
        this.word = new LinkedList<>();
    }

    public void append(char ch) {
        word.add(ch);
    }

    public void replaceLatest(char ch) {
        stepBack();
        append(ch);
    }

    public void stepBack() {
        word.removeLast();
    }

    public String getWord() {
        StringBuilder builder = new StringBuilder();
        for(char ch : word) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public WordConstructor clone() {
        WordConstructor clone = new WordConstructor();
        for(char ch : this.word) {
            clone.append(ch);
        }
        return clone;
    }
}
