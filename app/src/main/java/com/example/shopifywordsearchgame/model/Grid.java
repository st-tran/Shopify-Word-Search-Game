package com.example.shopifywordsearchgame.model;

import java.util.Arrays;

/**
 * A Grid of characters
 */
public class Grid {
    private char[][] array;

    /**
     * @param numRows number of rows in this Grid
     * @param numCols number of cols in this Grid
     */
    public Grid(int numRows, int numCols) {
        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException("Number of rows and columns should be positive!");
        }
        array = new char[numRows][numCols];
    }

    public Grid(int sideLength) {
        this(sideLength, sideLength);
    }

    public int getGridNumRows() {
        return array.length;
    }

    public int getGridNumCols() {
        return array[0].length;
    }

    /**
     * @param row of the character
     * @param col of the character
     * @return character at position row-col in this Grid
     */
    public char getCharAt(int row, int col) {
        return array[row][col];
    }

    /**
     * Sets the character at row-col of this Grid to a character c
     * @param row of the character to be changed
     * @param col of the character to be changed
     * @param c the character
     */
    public void setCharAt(int row, int col, char c) {
        array[row][col] = c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getGridNumRows(); i++) {
            for (int j = 0; j < getGridNumCols(); j++) {
                sb.append(getCharAt(i, j));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public char[][] getArray() {
        if (array == null) {
            return null;
        }

        char[][] result = new char[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return result;
    }
}
