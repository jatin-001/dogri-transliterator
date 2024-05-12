package com.transliteration;

import java.io.IOException;
import java.util.regex.Pattern;

public class Translator {

    private final static Pattern separator = Pattern.compile("\\b");
    private final Schema schema;

    /**
     * @param schema The scheme according to which the translation will take place
     * @throws IllegalStateException if schema was corrupted
     */
    public Translator(Schemas schema) {
        try {
            this.schema = schema.getSchema();
        } catch (IOException e) {
            throw new IllegalStateException("Resources were corrupted");
        }
    }

    public String translate(String str) {
        if (str == null) return null;
        var words = separator.split(str);
        var translated = new StringBuilder();
        for (String word : words) {
            translated.append(translateWord(word));
        }
        return translated.toString();
    }

    private String translateWord(String word) {
        var splitWord = splitWord(word);
        var translatedEnding = schema.translateEnding(splitWord.ending);
        return translatedEnding
            .map(s -> translateLetters(splitWord.stem) + s)
            .orElseGet(() -> translateLetters(word));
    }

    private String translateLetters(String word) {
        String prev = "";
        String curr = "";
        String next = "";
        int length = word.length();
        var translated = new StringBuilder();
        for (int i=0; i < length; ) {
            if (word.charAt(i) == ' ') {
                translated.append(" ");
                prev="";
                curr="";
                next="";
                ++i;
                continue;
            }
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
            } else {
                next = "";
            }
            String str = schema.translateLetter(prev, curr, next);
            translated.append(str);
            int skip = Math.max(str.length() / 2, 1);
            i = i + skip;
            if (skip > 1) {
                prev = "";
                curr = "";
                next = "";
            }
        }
        return translated.toString();
    }

    private SplitWord splitWord(String word) {
        int endingLength = 2;
        if (word.length() > endingLength) {
            int separateIndex = word.length() - endingLength;
            return new SplitWord(word.substring(0, separateIndex), word.substring(separateIndex));
        } else {
            return new SplitWord(word, "");
        }
    }

    private static class SplitWord {
        private final String stem;
        private final String ending;

        public SplitWord(String stem, String ending) {
            this.stem = stem;
            this.ending = ending;
        }
    }
}
