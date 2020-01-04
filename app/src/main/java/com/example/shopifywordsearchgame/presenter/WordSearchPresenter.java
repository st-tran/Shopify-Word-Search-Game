package com.example.shopifywordsearchgame.presenter;

import android.graphics.Point;
import android.util.ArraySet;

import com.example.shopifywordsearchgame.common.Direction;
import com.example.shopifywordsearchgame.model.WordSearchLogic;
import com.example.shopifywordsearchgame.view.IWordSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordSearchPresenter implements IWordSearchPresenter {
    private IWordSearchView wordSearchView;
    private WordSearchLogic gameBackend;
    private Set<Point> highlighted = new ArraySet<>();
    private List<Point> visited = new ArrayList<>();
    private Direction currDirection;
    private int rows, cols;

    public WordSearchPresenter(
            int rows,
            int cols,
            IWordSearchView wordSearchView,
            String[] words) {
        this.wordSearchView = wordSearchView;

        this.rows = rows;
        this.cols = cols;
        gameBackend = new WordSearchLogic(rows, cols, words);

        wordSearchView.setGrid(gameBackend.getGrid());
        wordSearchView.setWords(gameBackend.getWords().toArray(new String[0]));
        wordSearchView.setGridTouchListener();
    }

    @Override
    public void onPress(int x, int y) {
        Point clickPos = getRowCol(x, y);

        int pressedRow = clickPos.y;
        int pressedCol = clickPos.x;

        visited.add(clickPos);
        wordSearchView.highlightCharAtPos(pressedRow, pressedCol);
    }

    @Override
    public void onMove(int x, int y) {
        Point initPos = visited.get(0);
        Point clickPos = getRowCol(x, y);

        if(!initPos.equals(clickPos)) {
            Direction newDir = Direction.between(initPos, clickPos);

            if(currDirection != newDir) {
                currDirection = newDir;
            }

            for (Point pos : visited) {
                wordSearchView.unhighlightCharAtPos(pos.y, pos.x);
            }
            visited.clear();
            visited.add(initPos);
            wordSearchView.highlightCharAtPos(initPos.y, initPos.x);

            int rowDiff = Math.abs(initPos.y - clickPos.y);
            int colDiff = Math.abs(initPos.x - clickPos.x);
            for (int i = 0; i < Math.max(rowDiff, colDiff) + 1; i++) {
                Point targetPoint = new Point(
                        initPos.x + currDirection.getXOffset() * i,
                        initPos.y + currDirection.getYOffset() * i);
                if (isRowColInBounds(targetPoint.y, targetPoint.x)) {
                    boolean toAdd = true;
                    for (Point visitedPoint : visited) {
                        if (visitedPoint.equals(targetPoint)) {
                            toAdd = false;
                            break;
                        }
                    }

                    if (toAdd) {
                        wordSearchView.highlightCharAtPos(targetPoint.y, targetPoint.x);
                        visited.add(targetPoint);
                    }
                }
            }
        }
    }

    @Override
    public void onLiftUp() {
        Point initPos = visited.get(0);
        StringBuilder result = new StringBuilder();

        for (Point visitedPoint : visited) {
            result.append(gameBackend.getCharAt(visitedPoint.y, visitedPoint.x));
            wordSearchView.unhighlightCharAtPos(visitedPoint.y, visitedPoint.x);
        }

        if (gameBackend.attemptSolve(initPos.y, initPos.x, currDirection, result.toString())) {
            highlighted.addAll(visited);
            wordSearchView.markWordSolved(result.toString());
        }

        for (Point highlightPoints : highlighted) {
            wordSearchView.highlightCharAtPos(highlightPoints.y, highlightPoints.x);
        }
        visited.clear();
        currDirection = null;
    }

    private Point getRowCol(int x, int y) {
        int pressedRow = y / wordSearchView.getLetterHeight();
        int pressedCol = x / wordSearchView.getLetterWidth();

        return new Point(pressedCol, pressedRow);
    }

    private boolean isRowColInBounds(int row, int col) {
        return 0 <= row && row < rows && 0 <= col && col < cols;
    }
}
