package com.transliteration;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public enum Schemas {
    DOGRI("dogri.json");

    private final String name;

    Schemas(String name) {
        this.name = name;
    }

    public Schema getSchema() throws IOException {
        var stream = getJsonStream(name);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(stream, Schema.class);
    }

    public String getName() {
        return name;
    }

    private InputStream getJsonStream(String fileName) {
        return getClass().getResourceAsStream("/schemas/" + fileName);
    }
}
