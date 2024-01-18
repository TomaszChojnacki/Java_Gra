package org.example;

import java.awt.Graphics;
import javax.swing.JPanel;

// zarówno dla ty, jak i dla przeciwnika
public class Gracz {
    int x, y;
    String status, kolor;
    JPanel panel;
    boolean zywy;

    ZmieniaczStatusu zs;

    Gracz(int id, JPanel panel) throws InterruptedException {
        this.x = Klient.spawn[id].x;
        this.y = Klient.spawn[id].y;
        this.kolor = Ludek.koloryPostaci[id];
        this.panel = panel;
        this.zywy = Klient.zywy[id];

        (zs = new ZmieniaczStatusu(this, "czekaj")).start();
    }

    public void rysuj(Graphics g) {
        if (zywy)
            g.drawImage(Ludek.ht.get(kolor + "/" + status), x, y, Stale.SZEROKOSC_SPRITE_GRACZA, Stale.WYSOKOSC_SPRITE_GRACZA, null);
    }
}

class ZmieniaczStatusu extends Thread {
    Gracz gracz;
    String status;
    int indeks;
    boolean graczWRuchu;

    ZmieniaczStatusu(Gracz gracz, String poczatkowyStatus) {
        this.gracz = gracz;
        this.status = poczatkowyStatus;
        indeks = 0;
        graczWRuchu = true;
    }
    public void run() {
        while (true) {
            gracz.status = status + "-" + indeks;
            if (graczWRuchu) {
                indeks = (++indeks) % Ludek.maxPetlaStatusu.get(status);
                gracz.panel.repaint();
            }

            try {
                Thread.sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_STATUSU_GRACZA);
            } catch (InterruptedException e) {}

            if (gracz.status.equals("martwy-4")) {
                gracz.zywy = false;
                if (Gra.ty == gracz)
                    System.exit(1);
            }
        }
    }
    void ustawPetleStatusu(String status) {
        this.status = status;
        indeks = 1;
        graczWRuchu = true;
    }
    void zatrzymajPetleStatusu() {
        graczWRuchu = false;
        indeks = 0;
    }
}
