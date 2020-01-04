package com.example.shopifywordsearchgame.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordLoader implements IWordService {
    private List<String> words = new ArrayList<>();

    public WordLoader(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                words.addAll(Arrays.asList(
                        line.trim().split(", "))
                );
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getWords() {
        return words.toArray(new String[0]);
    }
}
