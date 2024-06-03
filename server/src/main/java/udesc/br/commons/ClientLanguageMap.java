package udesc.br.commons;

import udesc.br.exception.ServerSideException;
import udesc.br.model.LanguageData;

import javax.swing.*;

import static udesc.br.commons.Colors.*;
import static udesc.br.commons.Constants.*;

public class ClientLanguageMap {
    private static LanguageData[] languages = {
            new LanguageData(new ImageIcon(JAVA), "Java", RED),
            new LanguageData(new ImageIcon(CSHARP), "C#", PURPLE),
            new LanguageData(new ImageIcon(GO), "Go", CYAN),
            new LanguageData(new ImageIcon(JAVASCRIPT), "JavaScript", YELLOW),
            new LanguageData(new ImageIcon(KOTLIN), "Kotlin", BLUE),
            new LanguageData(new ImageIcon(RUST), "Rust", BLACK)
    };

    public static LanguageData getLanguage(int index) {
        try {
            return languages[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ServerSideException();
        }
    }
}

