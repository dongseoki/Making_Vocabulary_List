package com.example.wordlikeproject;

public class Word {
    private String wordSpelling;
    private String wordMeaning;
    private String wordSentence;
    private int rank;

    public Word(String wordSpelling, String wordMeaning, String wordSentence, int rank) {
        this.wordSpelling = wordSpelling;
        this.wordMeaning = wordMeaning;
        this.wordSentence = wordSentence;
        this.rank = rank;
    }

    public String getWordSpelling() {
        return wordSpelling;
    }

    public void setWordSpelling(String wordSpelling) {
        this.wordSpelling = wordSpelling;
    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public void setWordMeaning(String wordMeaning) {
        this.wordMeaning = wordMeaning;
    }

    public String getWordSentence() {
        return wordSentence;
    }

    public void setWordSentence(String wordSentence) {
        this.wordSentence = wordSentence;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
