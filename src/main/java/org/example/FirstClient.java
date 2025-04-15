package org.example;

import java.awt.*;

public class FirstClient {
    public static void main(String[] args) {
        // Uruchomienie okna logowania w kolejce zdarzeń interfejsu graficznego
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Tworzenie i wyświetlanie ekranu logowania
                Logowanie EkranLogowania = new Logowanie();
                EkranLogowania.setVisible(true);        // Ustawienie ekranu logowania jako widocznego

                // Pobranie narzędzi systemowych i wymiarów ekranu
                Toolkit t = Toolkit.getDefaultToolkit();
                Dimension d = t.getScreenSize();

                // Ustawienie położenia okna logowania na ekranie
                EkranLogowania.setLocation((d.width / 4), (d.height / 4));
            }
        });
    }
}

