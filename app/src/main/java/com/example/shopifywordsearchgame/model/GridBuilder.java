package com.example.shopifywordsearchgame.model;

import android.graphics.Point;
import android.util.ArraySet;

import com.example.shopifywordsearchgame.common.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GridBuilder {
    private Grid grid;
    private Set<Word> placedWords = new ArraySet<>();
    private static Random random = new Random();

    /**
     * Initializes a Grid.
     *
     * @param numRows number of rows
     * @param numCols number of columns
     * @return
     */
    public GridBuilder initGrid(int numRows, int numCols) {
        grid = new Grid(numRows, numCols);
        return this;
    }

    public GridBuilder setWords(String[] words) {
        Direction dir;  // Direction in which a Word should be placed
        Point pos;      // Position at which a Word should be placed
        List<Point> visited = new ArrayList<>();

        for (String word : words) {
            boolean placed = false;

            while (!placed && visited.size() < grid.getGridNumCols() * grid.getGridNumRows()) {
                dir = Direction.getRandomDirection();
                pos = new Point(
                        random.nextInt(grid.getGridNumCols()),
                        random.nextInt(grid.getGridNumRows()));

                if (visited.contains(pos)) {
                    continue;
                } else {
                    visited.add(pos);
                }
                for (int i = 0; i < Direction.size; i++) {
                    if (canPlaceWord(pos.y, pos.x, dir, word)) {
                        placeWord(pos.y, pos.x, dir, word);
                        placed = true;
                        break;
                    }
                    dir = dir.getNextDirection();
                }
            }
        }
        return this;
    }

    public GridBuilder setOtherLetters() {
        for (int i = 0; i < grid.getGridNumRows(); i++) {
            for (int j = 0; j < grid.getGridNumCols(); j++) {
                if (((int) grid.getCharAt(i, j)) == 0) {
                    grid.setCharAt(i, j, (char) (random.nextInt(26) + 'a'));
                }
            }
        }
        return this;
    }

    public Grid build() {
        return grid;
    }

    private void placeWord(int row, int col, Direction dir, String word) {
        for (int i = 0; i < word.length(); i++) {
            grid.setCharAt(
                    row + dir.getYOffset() * i,
                    col + dir.getXOffset() * i,
                    word.charAt(i)
            );
        }
        placedWords.add(new Word(word, new Point(col, row), dir));
        grid.addWord(word);
    }

    private boolean canPlaceWord(int row, int col, Direction dir, String word) {
        // Check to see if the Grid has ample room to fit this word
        if (Math.pow(word.length(), 2) > Math.max(
                // Diagonal check
                2 * Math.pow(Math.min(grid.getGridNumRows(), grid.getGridNumCols()), 2),
                // Vertical/horizontal check
                Math.max(grid.getGridNumRows(), grid.getGridNumCols()))) {
            return false;
        }

        int lastRow = row + (word.length() - 1) * dir.getYOffset();
        int lastCol = col + (word.length() - 1) * dir.getXOffset();


        // Check to see if all characters would fit in the grid
        if (!(0 <= row && row < grid.getGridNumRows() && 0 <= col && col < grid.getGridNumCols()) ||
                !(0 <= lastRow && lastRow < grid.getGridNumRows() && 0 <= lastCol && lastCol < grid.getGridNumCols())) {
            return false;
        }

        // Check to see if there is overlap between existing words and this word
        for (int i = 0; i < word.length(); i++) {
            char charAtPos = grid.getCharAt(
                    row + dir.getYOffset() * i,
                    col + dir.getXOffset() * i);
            if ((int) charAtPos != 0 && charAtPos != word.charAt(i)) {
                return false;
            }
        }

        // If all checks pass, we can place this word
        return true;
    }
}
