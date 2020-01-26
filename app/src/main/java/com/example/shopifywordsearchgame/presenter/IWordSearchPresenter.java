package com.example.shopifywordsearchgame.presenter;

import com.example.shopifywordsearchgame.view.IWordSearchView;

import java.io.Serializable;

public interface IWordSearchPresenter extends Serializable {
    void onPress(int x, int y);
    void onMove(int x, int y);
    void onLiftUp();
    void onViewChanged();
    void updateView(IWordSearchView view);
    void reHighlight();
    void setSolved();
    void reCrossout();
}
