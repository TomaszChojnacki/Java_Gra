package org.example;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Gra extends JPanel {
    private static final long serialVersionUID = 1L;    // Unikalny identyfikator klasy, używany w serializacji
    static Gracz ty, przeciwnik1, przeciwnik2, przeciwnik3;     // Statyczne zmienne dla gracza i przeciwników

    // Konstruktor klasy Gra
    Gra(int szerokosc, int wysokosc) {
        setPreferredSize(new Dimension(szerokosc, wysokosc));   // Ustawienie preferowanego rozmiaru panelu
        try {
            // Inicjalizacja gracza i przeciwników
            System.out.print("Inicjalizacja graczy...");
            ty = new Gracz(Klient.id, this);
            przeciwnik1 = new Gracz((Klient.id + 1) % Stale.ILU_GRACZY, this);
            przeciwnik2 = new Gracz((Klient.id + 2) % Stale.ILU_GRACZY, this);
            przeciwnik3 = new Gracz((Klient.id + 3) % Stale.ILU_GRACZY, this);
        } catch (InterruptedException e) {
            // Obsługa wyjątków przerwania
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");


    }

    // Metoda rysująca komponenty, wywoływana przez paint() i repaint()
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    // Wywołanie metody z klasy nadrzędnej
        rysujMape(g);   // Rysowanie mapy
        przeciwnik1.rysuj(g);   // Rysowanie przeciwnika 1
        przeciwnik2.rysuj(g);
        przeciwnik3.rysuj(g);
        ty.rysuj(g);    // Rysowanie gracza

        //System.out.format("%s: %s [%04d, %04d]\n", Gra.ty.kolor, Gra.ty.status, Gra.ty.x, Gra.ty.y);
        Toolkit.getDefaultToolkit().sync();     // Synchronizacja bufora graficznego dla płynnej animacji
    }

    // Metoda do rysowania mapy gry
    void rysujMape(Graphics g) {
        // Iteracja przez każdy wiersz i kolumnę mapy gry
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                // Rysowanie pojedynczej grafiki ludka na mapie
                g.drawImage(
                        Ludek.ht.get(Klient.mapa[i][j].obraz),  // Pobranie obrazu ludka z tablicy hashującej
                        Klient.mapa[i][j].x, Klient.mapa[i][j].y,   // Ustawienie współrzędnych ludka
                        Stale.ROZMIAR_SPRITE_MAPY, Stale.ROZMIAR_SPRITE_MAPY, // Ustawienie rozmiaru ludka
                        null // Użycie domyślnego obrazu ludka
                );
    }

    // Metoda do aktualizacji obrazu na mapie
    static void ustawSpriteMape(String slowoKluczowe, int l, int c) {
        // Aktualizacja obrazu dla konkretnej komórki na mapie
        Klient.mapa[l][c].obraz = slowoKluczowe;    // Przypisanie nowego obrazu na mapie
    }
}



