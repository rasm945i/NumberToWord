package dk.rasmusbendix;

import java.util.Arrays;
import java.util.stream.Stream;

public class Number {

    private int currentIndex;
    private final int number;
    private final char[] associatedLetters;

    public Number(int num, char ... chars) {
        this.number = num;
        this.associatedLetters = chars;
        this.currentIndex = 0;
    }

    public int getNumber() {
        return number;
    }

    public boolean next() {
        if(!hasNext())
            return false;
        System.out.println("Increase.");
        currentIndex++;
        return true;
    }

    public boolean hasNext() {
        return currentIndex+1 < associatedLetters.length;
    }

    public void resetIndex() {
        currentIndex = 0;
    }

    public void setIndex(int i) {
        currentIndex = i;
    }

    public char getChar() {
        if(currentIndex < 0 || currentIndex >= associatedLetters.length)
            throw new ArrayIndexOutOfBoundsException(getNumber() + " " + Arrays.toString(associatedLetters));
        return associatedLetters[currentIndex];
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getMaxIndex() {
        return associatedLetters.length;
    }

    public boolean contains(char ch) {
        for(char c : associatedLetters) {
            if(c == ch)
                return true;
        }
        return false;
    }

    public Number clone() {
        return new Number(getNumber(), associatedLetters);
    }
}
