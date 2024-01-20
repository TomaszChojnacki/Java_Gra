package org.example;

import java.awt.event.*;

// nasłuchuje, gdy okno (JFrame) jest w focusie
public class Nadawca extends KeyAdapter {
    int ostatniWcisnietyKodKlawisza;

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            Klient.wyjscie.println("wcisnietoSpacje " + Gra.ty.x + " " + Gra.ty.y);
        else if (jestNowymKodemKlawisza(e.getKeyCode()))
            Klient.wyjscie.println("klawiszWcisniety " + e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        Klient.wyjscie.println("klawiszZwolniony " + e.getKeyCode());
        ostatniWcisnietyKodKlawisza = -1; // każdy kolejny klawisz będzie nowy
    }

    boolean jestNowymKodemKlawisza(int kodKlawisza) {
        boolean ok = (kodKlawisza != ostatniWcisnietyKodKlawisza) ? true : false;
        ostatniWcisnietyKodKlawisza = kodKlawisza;
        return ok;
    }
}


