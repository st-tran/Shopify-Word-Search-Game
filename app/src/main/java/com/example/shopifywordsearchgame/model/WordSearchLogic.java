package com.example.shopifywordsearchgame.model;

import android.graphics.Point;
import android.util.ArraySet;

import com.example.shopifywordsearchgame.common.Direction;

import java.io.Serializable;
import java.util.Set;

public class WordSearchLogic implements Serializable {
    private Grid grid;
    private Set<String> words;
    private Set<Word> foundWords = new ArraySet<>();

    public WordSearchLogic(int sideLength, String[] words) {
        this(sideLength, sideLength, words);
    }

    public WordSearchLogic(int numRows, int numCols, String[] words) {
        grid = new GridBuilder().
                initGrid(numRows, numCols).
                setWords(words).
                setOtherLetters().
                build();
        this.words = new ArraySet<>(grid.getPlacedWords());
    }

    public boolean attemptSolve(int row, int col, Direction dir, String word) {
        if (!words.contains(word)) {
            return false;
        }

        for (Word w : foundWords) {
            if (w.toString().equals(word)) {
                return false;
            }
        }

        int lastRow = row + (word.length() - 1) * dir.getYOffset();
        int lastCol = col + (word.length() - 1) * dir.getXOffset();

        if (!(0 <= lastCol &&
                lastCol <= grid.getGridNumCols() &&
                0 <= lastRow &&
                lastRow <= grid.getGridNumRows())) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            if (!(word.charAt(i) == grid.getCharAt(
                    row + dir.getYOffset() * i,
                    col + dir.getXOffset() * i))) {
                return false;
            }
        }
        foundWords.add(new Word(word, new Point(col, row), dir));
        return true;
    }

    public Set<String> getWords() {
        return new ArraySet<>(words);
    }

    public char getCharAt(int row, int col) {
        return grid.getCharAt(row, col);
    }

    public char[][] getGrid() {
        return grid.getArray();
    }
}
