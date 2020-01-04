package com.example.shopifywordsearchgame.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.shopifywordsearchgame.R;
import com.example.shopifywordsearchgame.data.WordLoader;
import com.example.shopifywordsearchgame.data.IWordService;
import com.example.shopifywordsearchgame.presenter.IWordSearchPresenter;
import com.example.shopifywordsearchgame.presenter.WordSearchPresenter;

public class WordSearchActivity extends AppCompatActivity implements IWordSearchView {
    private final int ROWS = 10;
    private final int COLS = 10;

    private GridView gridView;
    private LinearLayout scrollViewContainer;
    private IWordSearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove notification bar
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.word_search_activity);

        gridView = findViewById(R.id.game_grid);
        scrollViewContainer = findViewById(R.id.word_list);

        IWordService wordLoader = new WordLoader(getResources().openRawResource(R.raw.wordlist));
        presenter = new WordSearchPresenter(ROWS, COLS, this, wordLoader);
    }
    @Override
    public void setWords(String[] words) {
        for (String word : words) {
            TextView tv = new TextView(this);
            tv.setText(word);
            tv.setTag(word.toUpperCase());
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextSize(24f);
            tv.setPadding(16, 0, 16, 0);
            tv.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tv.setLayoutParams(new ScrollView.LayoutParams(
                    ScrollView.LayoutParams.WRAP_CONTENT,
                    ScrollView.LayoutParams.MATCH_PARENT)
            );
            scrollViewContainer.addView(tv);
        }
    }

    @Override
    public void markWordSolved(String word) {
        ((TextView) scrollViewContainer.findViewWithTag(word.toUpperCase())
        ).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void setGrid(char[][] grid) {
        GridAdapter gridAdapter = new GridAdapter(WordSearchActivity.this, grid);
        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(COLS);
    }

    @Override
    public void highlightCharAtPos(int row, int col) {
        getTextViewAt(row, col).setTextColor(Color.RED);
    }

    @Override
    public void unhighlightCharAtPos(int row, int col) {
        getTextViewAt(row, col).setTextColor(Color.BLACK);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setGridTouchListener() {
        gridView.setOnTouchListener(new GridView.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                // Touch event out of bounds
                if (!(0 <= x && x <= gridView.getWidth() && 0 <= y && y <= gridView.getHeight())) {
                    return false;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    presenter.onPress(x, y);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    presenter.onMove(x, y);
                } else {
                    presenter.onLiftUp();
                }
                return true;
            }
        });
    }

    @Override
    public int getLetterHeight() {
        return gridView.getChildAt(0).getHeight();
    }

    @Override
    public int getLetterWidth() {
        return gridView.getChildAt(0).getWidth();
    }

    private TextView getTextViewAt(int row, int col) {
        return (TextView) (
                (ConstraintLayout) gridView.getChildAt(row * COLS + col)
        ).getChildAt(0);
    }
}
