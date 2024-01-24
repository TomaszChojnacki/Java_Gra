package org.example;

import javax.swing.*;

class Okno extends JFrame {
    private static final long serialVersionUID = 1L;    // Unikalny identyfikator klasy, używany w serializacji

    // Konstruktor klasy Okno
    Okno() {
        Ludek.zaladujObrazy();  // Załadowanie obrazów potrzebnych w grze
        Ludek.ustawMaxPetlaStatusu();   // Ustawienie maksymalnej liczby iteracji w pętli statusu

        // Dodanie panelu gry do okna
        add(new Gra(Stale.KOLUMNY * Stale.ROZMIAR_SPRITE_MAPY, Stale.WIERSZE * Stale.ROZMIAR_SPRITE_MAPY));
        setTitle("bomberman");  // Ustawienie tytułu okna
        pack();     // Dostosowanie rozmiaru okna do zawartości
        setVisible(true);   // Ustawienie okna jako widocznego
        setLocationRelativeTo(null);    // Centrowanie okna na ekranie
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);    // Ustawienie domyślnej operacji zamknięcia

        addKeyListener(new Nadawca());  // Dodanie nasłuchiwania na zdarzenia klawiatury
    }
}
