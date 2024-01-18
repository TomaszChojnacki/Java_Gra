package org.example;

import java.awt.Graphics;
import javax.swing.JPanel;

//zarówno dla 'ty', jak i dla 'przeciwnik'
public class Gracz {
   int x, y;
   String status, kolor;
   JPanel panel;
   boolean zywy;

   ZmieniaczStatusu sc;

   Gracz(int id, JPanel panel) throws InterruptedException {
      this.x = Klient.punktStartowy[id].x;
      this.y = Klient.punktStartowy[id].y;
      this.kolor = Ludek.koloryPostaci[id];
      this.panel = panel;
      this.zywy = Klient.zywy[id];

      (sc = new ZmieniaczStatusu(this, "czekaj")).start();
   }

   public void rysuj(Graphics g) {
      if (zywy)
         g.drawImage(Ludek.ht.get(kolor + "/" + status), x, y, Stale.SZEROKOSC_SPRITA_GRACZA, Stale.WYSOKOSC_SPRITA_GRACZA, null);
   }
}

class ZmieniaczStatusu extends Thread {
   Gracz p;
   String status;
   int indeks;
   boolean graczWRuchu;

   ZmieniaczStatusu(Gracz p, String poczatkowyStatus) {
      this.p = p;
      this.status = poczatkowyStatus;
      indeks = 0;
      graczWRuchu = true;
   }
   public void run() {
      while (true) {
         p.status = status + "-" + indeks;
         if (graczWRuchu) {
            indeks = (++indeks) % Ludek.maxPetlaStatusu.get(status);
            p.panel.repaint();
         }

         try {
            Thread.sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_STATUSU_GRACZA);
         } catch (InterruptedException e) {}

         if (p.status.equals("martwy-4")) {
            p.zywy = false;
            if (Gra.ty == p)
               System.exit(1);
         }
      }
   }
   void setLoopStatus(String status) {
      this.status = status;
      indeks = 1;
      graczWRuchu = true;
   }
   void stopLoopStatus() {
      graczWRuchu = false;
      indeks = 0;
   }
}
