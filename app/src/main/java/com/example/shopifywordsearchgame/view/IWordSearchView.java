package com.example.shopifywordsearchgame.view;

public interface IWordSearchView {
    void setWords(String[] words);
    void markWordSolved(String word);
    void setGrid(char[][] grid);
    void highlightCharAtPos(int row, int col);
    void unhighlightCharAtPos(int row, int col);
    void setGridTouchListener();
    void setSolved(String solvedText);
    void showGameFinishedPopup();
    int getLetterHeight();
    int getLetterWidth();
}
