package org.example;

// dostępna zarówno dla serwera, jak i klienta
interface Stale {
    // jeśli nie jest to 4, wiele rzeczy musi przestać być instancjowane
    final static int ILU_GRACZY = 4;

    final static int WIERSZE = 9, KOLUMNY = 9; // zawsze nieparzyste
    final static int ZMIANA_ROZMIARU = 4; // rozmiar piksela

    final static int ROZMIAR_SPRITE_MAPY = 16 * ZMIANA_ROZMIARU;
    final static int SZEROKOSC_SPRITE_GRACZA = 22 * ZMIANA_ROZMIARU;
    final static int WYSOKOSC_SPRITE_GRACZA = 33 * ZMIANA_ROZMIARU;

    // różnica w pikselach między spritem mapy a graczem
    final static int VAR_X_SPRITE = 3 * ZMIANA_ROZMIARU;
    final static int VAR_Y_SPRITE = 16 * ZMIANA_ROZMIARU;

    final static int CZESTOTLIWOSC_AKTUALIZACJI_BOMBY = 90;
    final static int CZESTOTLIWOSC_AKTUALIZACJI_BLOKU = 100;
    final static int CZESTOTLIWOSC_AKTUALIZACJI_OGNIA = 35;
    final static int CZESTOTLIWOSC_AKTUALIZACJI_STATUSU_GRACZA = 90;
    final static int CZESTOTLIWOSC_AKTUALIZACJI_WSPOLRZEDNYCH = 27;

    final static String indeksBombaZasadzona[] = {
            "1", "2", "3", "2", "1", "2", "3", "2", "1", "2", "3", "2", "1", "2",
            "czerwona-3", "czerwona-2", "czerwona-1", "czerwona-2", "czerwona-3", "czerwona-2", "czerwona-3", "czerwona-2", "czerwona-3", "czerwona-2", "czerwona-3"
    };
    final static String indeksEksplozji[] = {
            "1", "2", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5", "4", "3", "2", "1"
    };
    final static String indeksBlokWPlomieniach[] = {
            "1", "2", "1", "2", "1", "2", "3", "4", "5", "6"
    };
}

class Koordynaty {
    public int x, y;
    String obraz;

    Koordynaty(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Koordynaty(int x, int y, String obraz) {
        this.x = x;
        this.y = y;
        this.obraz = obraz;
    }
}
