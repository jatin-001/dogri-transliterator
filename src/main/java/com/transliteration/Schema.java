package com.transliteration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schema {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    private Map<String, String> mapping;
    private Map<String, String> prevMapping;
    private Map<String, String> nextMapping;
    private Map<String, String> endingMapping;
    private Map<String, String> vowels;
    private Map<String, String> mainVowels;
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Optional<String> translateEnding(String ending) {
        if (ending.equals("")) {
            return Optional.of(ending);
        }
        return Optional.ofNullable(endingMapping.getOrDefault(ending, null));
    }

    public String translateLetter(String prev, String curr, String next) {
        String letter = prevMapping.get(prev + curr);
        if (letter == null) {
            if (prev.equals(" ") || prev.equals("")) {
                letter = mainVowels.get(curr + next) == null ? mainVowels.get(curr) : mainVowels.get(curr + next) ;
            }
        }
        if (letter == null) {
            if (vowels.get(next) != null && !List.of("ee", "oo", "ai", "au", "ah").contains(curr + next)) {
                if (List.of("i").contains(next)) {
                    letter = vowels.get(next) + mapping.getOrDefault(curr, "");
                } else
                    letter = mapping.getOrDefault(curr, "") + vowels.get(next);
            } else {
                letter = nextMapping.get(curr + next);
            }
        }
        if (letter == null) {
            letter = mapping.getOrDefault(curr, "");
        }
        return letter;
    }

    @JsonProperty("mapping")
    private void unpackMapping(Map<String, String> mapping) {
        if (mapping == null) {
            this.mapping = new HashMap<>();
        } else {
            var entrySet = new HashSet<>(mapping.entrySet());
            for (var entry : entrySet) {
                mapping.put(capitalize(entry.getKey()), capitalize(entry.getValue()));
            }
            this.mapping = mapping;
        }
    }

    @JsonProperty("vowels")
    private void unpackVowels(Map<String, String> vowels) {
        if (vowels == null) {
            this.vowels = new HashMap<>();
        } else {
            var entrySet = new HashSet<>(vowels.entrySet());
            for (var entry : entrySet) {
                vowels.put(entry.getKey(), entry.getValue());
            }
            this.vowels = vowels;
        }
    }

    @JsonProperty("prev_mapping")
    private void unpackPrevMapping(Map<String, String> prevMapping) {
        if (prevMapping == null) {
            this.prevMapping = new HashMap<>();
        } else {
            var entrySet = new HashSet<>(prevMapping.entrySet());
            for (var entry : entrySet) {
                prevMapping.put(capitalize(entry.getKey()), entry.getValue());
                prevMapping.put(entry.getKey().toUpperCase(), capitalize(entry.getValue()));
            }
            this.prevMapping = prevMapping;
        }
    }

    @JsonProperty("next_mapping")
    private void unpackNextMapping(Map<String, String> nextMapping) {
        if (nextMapping == null) {
            this.nextMapping = new HashMap<>();
        } else {
            var entrySet = new HashSet<>(nextMapping.entrySet());
            for (var entry : entrySet) {
                nextMapping.put(capitalize(entry.getKey()), capitalize(entry.getValue()));
                nextMapping.put(entry.getKey().toUpperCase(), capitalize(entry.getValue()));
            }
            this.nextMapping = nextMapping;
        }
    }

    @JsonProperty("ending_mapping")
    private void unpackEndingMapping(Map<String, String> endingMapping) {
        if (endingMapping == null) {
            this.endingMapping = new HashMap<>();
        } else {
            var entrySet = new HashSet<>(endingMapping.entrySet());
            for (var entry : entrySet) {
                endingMapping.put(entry.getKey().toUpperCase(), entry.getValue().toUpperCase());
            }
            this.endingMapping = endingMapping;
        }
    }

    @JsonProperty("main_vowels")
    private void unpackMainVowels(Map<String, String> mainVowels) {
        if (mainVowels == null) {
            this.mainVowels = new HashMap<>();
        } else {
            var entrySet = new HashSet<>(mainVowels.entrySet());
            for (var entry : entrySet) {
                mainVowels.put(entry.getKey(), entry.getValue());
            }
            this.mainVowels = mainVowels;
        }
    }

    private String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return Character.toTitleCase(str.charAt(0)) + str.substring(1);
    }
}
