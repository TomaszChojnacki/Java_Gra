package org.example;

import org.example.images.KoloryPostaci;

import java.awt.Graphics;
import javax.swing.JPanel;

// Klasa Gracz reprezentuje gracza w grze, zarówno dla aktualnego użytkownika (ty), jak i dla przeciwników
public class Gracz {
    int x, y;   // Współrzędne gracza na mapie
    String status; // Status (np. czekanie, poruszanie się)
    String kolor;   // Odpowiada za kolor postaci gracza
    JPanel panel;   // Panel, na którym rysowany jest gracz
    boolean zywy;   // Flaga określająca, czy gracz jest żywy

    ZmieniaczStatusu zs;    // Wątek zarządzający statusem gracza

    // Konstruktor klasy Gracz
    Gracz(int id, JPanel panel) throws InterruptedException {
        this.x = Klient.spawn[id].x;    // Ustawienie pozycji startowej gracza na podstawie id
        this.y = Klient.spawn[id].y;
        this.kolor = String.valueOf(getColorById(id));  // Przypisanie koloru gracza na podstawie id
        this.panel = panel;     // Przypisanie panelu, na którym gracz będzie rysowany
        this.zywy = Klient.zywy[id];    // Ustawienie statusu życia gracza

        // Uruchomienie wątku zarządzającego statusem gracza
        (zs = new ZmieniaczStatusu(this, "czekaj")).start();
    }

    // Metoda do rysowania gracza na panelu
    public void rysuj(Graphics g) {
        if (zywy)   // Rysowanie gracza tylko jeśli jest żywy
            g.drawImage(Ludek.ht.get(kolor + "/" + status), x, y, Stale.SZEROKOSC_SPRITE_GRACZA, Stale.WYSOKOSC_SPRITE_GRACZA, null);
    }

    // Metoda zwracająca kolor postaci na podstawie id gracza
    public static KoloryPostaci getColorById(int id) {
        KoloryPostaci[] koloryPostaci = KoloryPostaci.values();     // Tablica dostępnych kolorów postaci
        if (id >= 0 && id < koloryPostaci.length) {
            return koloryPostaci[id];       // Zwrócenie koloru odpowiadającego id
        } else {
            System.out.println("błąd-kolory postaci");
            return null;
        }
    }
}

