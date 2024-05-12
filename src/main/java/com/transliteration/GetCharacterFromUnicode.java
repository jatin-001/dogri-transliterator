package com.transliteration;

import java.util.Scanner;

public class GetCharacterFromUnicode {

    public static void main(String[] args) {
        Translator translator = new Translator(Schemas.DOGRI);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(translator.translate(line));
        }
    }
}