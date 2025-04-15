package org.example;

class DaneGracza {
    boolean zalogowany, zywy;   // Flagi określające, czy gracz jest zalogowany i czy jest żywy
    int x, y; // Aktualne współrzędne gracza na mapie gry
    int liczbaBomb; // Liczba bomb, które gracz posiada

    // Konstruktor klasy DaneGracza
    DaneGracza(int x, int y) {
        this.x = x;     // Ustawienie początkowej współrzędnej x gracza
        this.y = y;
        this.zalogowany = false;    // Domyślnie gracz nie jest zalogowany
        this.zywy = false;      // Domyślnie gracz nie jest uznawany za żywego
        this.liczbaBomb = 1; // Przypisanie domyślnej liczby bomb dla gracza
    }
}