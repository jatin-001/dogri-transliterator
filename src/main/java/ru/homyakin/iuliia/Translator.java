package ru.homyakin.iuliia;

import java.io.IOException;
import javafx.util.Pair;

public class Translator {

    private final Schema schema;

    public Translator(Schemas schema) throws IOException {
        this.schema = schema.getSchema();
    }

    public String translate(String str) {
        if (str == null) return null;
        var words = str.split(" ");
        var translated = new StringBuilder();
        for (var word : words) {
            translated.append(translateWord(word));
        }
        return translated.toString();
    }

    private String translateWord(String word) {
        var pair = splitWord(word);
        var translatedEnding = schema.translateEnding(pair.getValue());

        return translateLetters(pair.getKey()) +
            translatedEnding.orElseGet(() -> translateLetters(pair.getValue()));
    }

    private String translateLetters(String word) {
        String prev = "";
        String curr = "";
        String next = "";
        int length = word.length();
        var translated = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            if (!curr.equals("")) {
                prev = curr;
            }
            if (next.equals("")) {
                curr = String.valueOf(word.charAt(i));
            } else {
                curr = next;
            }
            if (i < length - 1) {
                next = String.valueOf(word.charAt(i + 1));
            }
            translated.append(schema.translateLetter(prev, curr, next));
        }
        return translated.toString();
    }

    private Pair<String, String> splitWord(String word) {
        int endingLength = 2;
        if (word.length() > endingLength) {
            int separateIndex = word.length() - endingLength;
            return new Pair<>(word.substring(0, separateIndex), word.substring(separateIndex));
        } else {
            return new Pair<>(word, "");
        }
    }
}