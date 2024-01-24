package org.example;

// Klasa Koordynaty reprezentuje położenie obiektu w grze
class Koordynaty {
    public int x, y;       // Zmienne x i y przechowujące współrzędne obiektu
    String obraz;       // Zmienna przechowująca nazwę obrazu reprezentującego obiekt

    // Konstruktor przyjmujący tylko współrzędne (x, y)
    Koordynaty(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Rozszerzony konstruktor przyjmujący współrzędne (x, y) oraz nazwę obrazu
    Koordynaty(int x, int y, String obraz) {
        this.x = x;
        this.y = y;
        this.obraz = obraz;
    }
}
