package org.example;

import java.awt.*;

public class SecondClient {
    public static void main(String[] args) {
        // Uruchomienie interfejsu graficznego w oddzielnym wątku
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Logowanie EkranLogowania = new Logowanie();     // Tworzenie i wyświetlanie okna logowania
                EkranLogowania.setVisible(true);                // Ustawienie okna logowania jako widocznego
                Toolkit t = Toolkit.getDefaultToolkit();        // Pobranie narzędzi systemowych i rozmiarów ekranu
                Dimension d = t.getScreenSize();
                EkranLogowania.setLocation((d.width / 4), (d.height / 4));  // Ustawienie położenia okna logowania na ekranie
            }
        });
    }
}
