package org.example;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Gra extends JPanel {
    private static final long serialVersionUID = 1L;
    static Gracz ty, przeciwnik1, przeciwnik2, przeciwnik3;

    Gra(int szerokosc, int wysokosc) {
        setPreferredSize(new Dimension(szerokosc, wysokosc));
        try {
            System.out.print("Inicjalizacja graczy...");
            ty = new Gracz(Klient.id, this);
            przeciwnik1 = new Gracz((Klient.id+1)%Stale.ILU_GRACZY, this);
            przeciwnik2 = new Gracz((Klient.id+2)%Stale.ILU_GRACZY, this);
            przeciwnik3 = new Gracz((Klient.id+3)%Stale.ILU_GRACZY, this);
        } catch (InterruptedException e) {
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");

        System.out.println("Mój gracz: " + Ludek.koloryPostaci[Klient.id]);
    }

    // rysuje komponenty, wywoływane przez paint() i repaint()
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        rysujMape(g);
        przeciwnik1.rysuj(g);
        przeciwnik2.rysuj(g);
        przeciwnik3.rysuj(g);
        ty.rysuj(g);

         //System.out.format("%s: %s [%04d, %04d]\n", Gra.ty.kolor, Gra.ty.status, Gra.ty.x, Gra.ty.y);
        Toolkit.getDefaultToolkit().sync();
    }

    void rysujMape(Graphics g) {
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                g.drawImage(
                        Ludek.ht.get(Klient.mapa[i][j].obraz),
                        Klient.mapa[i][j].x, Klient.mapa[i][j].y,
                        Stale.ROZMIAR_SPRITE_MAPY, Stale.ROZMIAR_SPRITE_MAPY, null
                );
    }

    static void ustawSpriteMape(String slowoKluczowe, int l, int c) {
        Klient.mapa[l][c].obraz = slowoKluczowe;
    }
}


