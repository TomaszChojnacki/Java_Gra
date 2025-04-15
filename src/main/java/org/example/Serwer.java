package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Serwer {
    // Deklaracja tablicy graczy i mapy gry
    static DaneGracza gracz[] = new DaneGracza[Stale.ILU_GRACZY];
    static Koordynaty mapa[][] = new Koordynaty[Stale.WIERSZE][Stale.KOLUMNY];

    // Konstruktor serwera, który przyjmuje numer portu
    Serwer(int numerPortu) {
        ServerSocket ss;

        // Inicjalizacja mapy i danych gracza
        ustawMape();
        ustawDaneGracza();

        try {
            // Próba otwarcia portu i nasłuchiwania na nim
            System.out.print("Otwieranie portu " + numerPortu + "...");
            ss = new ServerSocket(numerPortu); // socket nasłuchuje portu
            System.out.print(" ok\n");

            // Pętla akceptująca połączenia od klientów
            for (int id = 0; !zalogowaniPelni(); id = (++id) % Stale.ILU_GRACZY)
                if (!gracz[id].zalogowany) {
                    Socket gniazdoKlienta = ss.accept();
                    new MenadzerKlienta(gniazdoKlienta, id).start();
                }
            // serwer nie kończy działania dopóki wątki klientów są aktywne
        } catch (IOException e) {
            // Obsługa wyjątków związanych z błędami wejścia/wyjścia
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
    }

    // Metoda sprawdzająca, czy wszyscy gracze są zalogowani
    boolean zalogowaniPelni() {
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            if (!gracz[i].zalogowany)
                return false;
        return true;
    }

    // Metoda ustawiająca mapę gry
    void ustawMape() {
        // Ustawianie początkowych koordynatów i bloków
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                mapa[i][j] = new Koordynaty(Stale.ROZMIAR_SPRITE_MAPY * j, Stale.ROZMIAR_SPRITE_MAPY * i, "blok");

        // ustawienie scian wedlug kolumn i wierszy
        for (int j = 1; j < Stale.KOLUMNY - 1; j++) {
            mapa[0][j].obraz = "sciana-srodek";
            mapa[Stale.WIERSZE - 1][j].obraz = "sciana-srodek";
        }
        for (int i = 1; i < Stale.WIERSZE - 1; i++) {
            mapa[i][0].obraz = "sciana-srodek";
            mapa[i][Stale.KOLUMNY - 1].obraz = "sciana-srodek";
        }
        mapa[0][0].obraz = "sciana-gora-lewo";
        mapa[0][Stale.KOLUMNY - 1].obraz = "sciana-gora-prawo";
        mapa[Stale.WIERSZE - 1][0].obraz = "sciana-dol-lewo";
        mapa[Stale.WIERSZE - 1][Stale.KOLUMNY - 1].obraz = "sciana-dol-prawo";

        // ustawienie scaian na srodku ekranu
        for (int i = 2; i < Stale.WIERSZE - 2; i++)
            for (int j = 2; j < Stale.KOLUMNY - 2; j++)
                if (i % 2 == 0 && j % 2 == 0)
                    mapa[i][j].obraz = "sciana-srodek";

        // ustawienie podlog po ktorych gracz moze chodzic
        mapa[1][1].obraz = "podloga-1";
        mapa[1][2].obraz = "podloga-1";
        mapa[2][1].obraz = "podloga-1";
        mapa[Stale.WIERSZE - 2][Stale.KOLUMNY - 2].obraz = "podloga-1";
        mapa[Stale.WIERSZE - 3][Stale.KOLUMNY - 2].obraz = "podloga-1";
        mapa[Stale.WIERSZE - 2][Stale.KOLUMNY - 3].obraz = "podloga-1";
        mapa[Stale.WIERSZE - 2][1].obraz = "podloga-1";
        mapa[Stale.WIERSZE - 3][1].obraz = "podloga-1";
        mapa[Stale.WIERSZE - 2][2].obraz = "podloga-1";
        mapa[1][Stale.KOLUMNY - 2].obraz = "podloga-1";
        mapa[2][Stale.KOLUMNY - 2].obraz = "podloga-1";
        mapa[1][Stale.KOLUMNY - 3].obraz = "podloga-1";
    }

    // Metoda ustawiająca pozycje graczy na mapie
    void ustawDaneGracza() {
        gracz[0] = new DaneGracza(
                mapa[1][1].x - Stale.VAR_X_SPRITE,
                mapa[1][1].y - Stale.VAR_Y_SPRITE
        );

        gracz[1] = new DaneGracza(
                mapa[Stale.WIERSZE - 2][Stale.KOLUMNY - 2].x - Stale.VAR_X_SPRITE,
                mapa[Stale.WIERSZE - 2][Stale.KOLUMNY - 2].y - Stale.VAR_Y_SPRITE
        );
        gracz[2] = new DaneGracza(
                mapa[Stale.WIERSZE - 2][1].x - Stale.VAR_X_SPRITE,
                mapa[Stale.WIERSZE - 2][1].y - Stale.VAR_Y_SPRITE
        );
        gracz[3] = new DaneGracza(
                mapa[1][Stale.KOLUMNY - 2].x - Stale.VAR_X_SPRITE,
                mapa[1][Stale.KOLUMNY - 2].y - Stale.VAR_Y_SPRITE
        );
    }

    // Główna metoda programu która uruchamia serwer na porcie 1234
    public static void main(String[] args) {
        new Serwer(1234);
    }
}

