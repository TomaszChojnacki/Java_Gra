package org.example;

import java.awt.event.*;

// Klasa Nadawca rozszerza KeyAdapter i nasłuchuje zdarzeń klawiatury, gdy okno (JFrame) jest w focusie.
public class Nadawca extends KeyAdapter {
    int ostatniWcisnietyKodKlawisza; // Zmienna do przechowywania kodu ostatnio wciśniętego klawisza

    public void keyPressed(KeyEvent e) {    // Metoda wywoływana, gdy klawisz jest wciśnięty
        if (e.getKeyCode() == KeyEvent.VK_SPACE)

            // Wysyłanie informacji o wciśnięciu spacji do serwera
            Klient.wyjscie.println("wcisnietoSpacje " + Gra.ty.x + " " + Gra.ty.y);
        else if (jestNowymKodemKlawisza(e.getKeyCode()))

            // Wysyłanie informacji o wciśnięciu innego klawisza do serwera
            Klient.wyjscie.println("klawiszWcisniety " + e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {   // Metoda wywoływana, gdy klawisz jest zwolniony

        // Wysyłanie informacji o zwolnieniu klawisza do serwera
        Klient.wyjscie.println("klawiszZwolniony " + e.getKeyCode());
        ostatniWcisnietyKodKlawisza = -1; // Resetowanie ostatnio wciśniętego klawisza
    }

    // Metoda sprawdzająca, czy aktualny klawisz jest nowym wciśnięciem
    boolean jestNowymKodemKlawisza(int kodKlawisza) {

        // Sprawdzenie, czy aktualny klawisz jest różny od ostatnio wciśniętego
        boolean ok = (kodKlawisza != ostatniWcisnietyKodKlawisza) ? true : false;
        ostatniWcisnietyKodKlawisza = kodKlawisza;      // Aktualizacja ostatnio wciśniętego klawisza
        return ok;
    }
}

