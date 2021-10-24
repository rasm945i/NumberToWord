package dk.rasmusbendix;

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
        if(currentIndex+1 >= associatedLetters.length)
            return false;
        System.out.println("Increase.");
        currentIndex++;
        return true;
    }

    public boolean isDone() {
        return currentIndex+1 >= associatedLetters.length;
    }

    public void resetIndex() {
        currentIndex = 0;
    }

    public char getChar() {
        return associatedLetters[currentIndex];
    }

    public boolean contains(char ch) {
        for(char c : associatedLetters) {
            if(c == ch)
                return true;
        }
        return false;
    }

}
