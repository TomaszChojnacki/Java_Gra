package org.example;

import java.awt.event.KeyEvent;

//wątek, który wysyła kolejne współrzędne do klientów, dopóki W/A/S/D nie zostanie zwolniony
class WyrzutnikWspolrzednych extends Thread {
   boolean gora, prawo, lewo, dol;
   int id;

   WyrzutnikWspolrzednych(int id) {
      this.id = id;
      gora = dol = prawo = lewo = false;
   }

   public void run() {
      int noweX = Serwer.gracz[id].x;
      int noweY = Serwer.gracz[id].y;

      while (true) {
         if (gora || dol || prawo || lewo) {
            if (gora) noweY = Serwer.gracz[id].y - Stale.POWIEKSZENIE;
            else if (dol) noweY = Serwer.gracz[id].y + Stale.POWIEKSZENIE;
            else if (prawo) noweX = Serwer.gracz[id].x + Stale.POWIEKSZENIE;
            else if (lewo) noweX = Serwer.gracz[id].x - Stale.POWIEKSZENIE;

            if (wspolrzednaJestWazna(noweX, noweY)) {
               MenedzerKlientow.wyslijDoWszystkichKlientow(id + " nowaWspolrzedna " + noweX + " " + noweY);

               Serwer.gracz[id].x = noweX;
               Serwer.gracz[id].y = noweY;
            } else {
               noweX = Serwer.gracz[id].x;
               noweY = Serwer.gracz[id].y;
            }
            try {
               sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_WSPOLRZEDNYCH);
            } catch (InterruptedException e) {
            }
         }
         try {
            sleep(0);
         } catch (InterruptedException e) {
         }
      }
   }

   int getColumnOfMap(int x) {
      return x / Stale.ROZMIAR_SPRITA_MAPY;
   }

   int getLineOfMap(int y) {
      return y / Stale.ROZMIAR_SPRITA_MAPY;
   }

   // sprawdza, na których sprite'ach mapy znajduje się gracz i czy są one ważne
   boolean wspolrzednaJestWazna(int noweX, int noweY) {
      if (!Serwer.gracz[id].zywy)
         return false;

      //sprawdza, czy gracz wszedł w ogień (współrzędna środka ciała)
      int xCiala = noweX + Stale.SZEROKOSC_SPRITA_GRACZA / 2;
      int yCiala = noweY + 2 * Stale.WYSOKOSC_SPRITA_GRACZA / 3;

      if (Serwer.mapa[getLineOfMap(yCiala)][getColumnOfMap(xCiala)].img.contains("explosion")) {
         Serwer.gracz[id].zywy = false;
         MenedzerKlientow.wyslijDoWszystkichKlientow(id + " nowyStatus martwy");
         return true;
      }

      int x[] = new int[4], y[] = new int[4];
      int c[] = new int[4], l[] = new int[4];

      // W ODNIESIENIU DO NOWEJ WSPÓŁRZĘDNEJ

      // 0: punkt w lewym górnym rogu
      x[0] = Stale.ROZNICA_X_SPRITOW + noweX + Stale.POWIEKSZENIE;
      y[0] = Stale.ROZNICA_Y_SPRITOW + noweY + Stale.POWIEKSZENIE;
      // 1: punkt w prawym górnym rogu
      x[1] = Stale.ROZNICA_X_SPRITOW + noweX + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;
      y[1] = Stale.ROZNICA_Y_SPRITOW + noweY + Stale.POWIEKSZENIE;
      // 2: punkt w lewym dolnym rogu
      x[2] = Stale.ROZNICA_X_SPRITOW + noweX + Stale.POWIEKSZENIE;
      y[2] = Stale.ROZNICA_Y_SPRITOW + noweY + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;
      // 3: punkt w prawym dolnym rogu
      x[3] = Stale.ROZNICA_X_SPRITOW + noweX + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;
      y[3] = Stale.ROZNICA_Y_SPRITOW + noweY + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;

      for (int i = 0; i < 4; i++) {
         c[i] = getColumnOfMap(x[i]);
         l[i] = getLineOfMap(y[i]);
      }

      if (
              (Serwer.mapa[l[0]][c[0]].img.equals("podloga-1") || Serwer.mapa[l[0]][c[0]].img.contains("centrum-wybuchu-1")) &&
                      (Serwer.mapa[l[1]][c[1]].img.equals("podloga-1") || Serwer.mapa[l[1]][c[1]].img.contains("centrum-wybuchu-1")) &&
                      (Serwer.mapa[l[2]][c[2]].img.equals("podloga-1") || Serwer.mapa[l[2]][c[2]].img.contains("centrum-wybuchu-1")) &&
                      (Serwer.mapa[l[3]][c[3]].img.equals("podloga-1") || Serwer.mapa[l[3]][c[3]].img.contains("centrum-wybuchu-1"))
      )
         return true; // będzie na ważnej współrzędnej

      if (
              (Serwer.mapa[l[0]][c[0]].img.contains("blok") || Serwer.mapa[l[0]][c[0]].img.contains("sciana")) ||
                      (Serwer.mapa[l[1]][c[1]].img.contains("blok") || Serwer.mapa[l[1]][c[1]].img.contains("sciana")) ||
                      (Serwer.mapa[l[2]][c[2]].img.contains("blok") || Serwer.mapa[l[2]][c[2]].img.contains("sciana")) ||
                      (Serwer.mapa[l[3]][c[3]].img.contains("blok") || Serwer.mapa[l[3]][c[3]].img.contains("sciana"))
      )
         return false; // będzie na ścianie

      // W ODNIESIENIU DO POPRZEDNIEJ WSPÓŁRZĘDNEJ


// 0: punkt w lewym górnym rogu
      x[0] = Stale.ROZNICA_X_SPRITOW + Serwer.gracz[id].x + Stale.POWIEKSZENIE;
      y[0] = Stale.ROZNICA_Y_SPRITOW + Serwer.gracz[id].y + Stale.POWIEKSZENIE;
// 1: punkt w prawym górnym rogu
      x[1] = Stale.ROZNICA_X_SPRITOW + Serwer.gracz[id].x + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;
      y[1] = Stale.ROZNICA_Y_SPRITOW + Serwer.gracz[id].y + Stale.POWIEKSZENIE;
// 2: punkt w lewym dolnym rogu
      x[2] = Stale.ROZNICA_X_SPRITOW + Serwer.gracz[id].x + Stale.POWIEKSZENIE;
      y[2] = Stale.ROZNICA_Y_SPRITOW + Serwer.gracz[id].y + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;
// 3: punkt w prawym dolnym rogu
      x[3] = Stale.ROZNICA_X_SPRITOW + Serwer.gracz[id].x + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;
      y[3] = Stale.ROZNICA_Y_SPRITOW + Serwer.gracz[id].y + Stale.ROZMIAR_SPRITA_MAPY - 2 * Stale.POWIEKSZENIE;

      for (int i = 0; i < 4; i++) {
         c[i] = getColumnOfMap(x[i]);
         l[i] = getLineOfMap(y[i]);
      }

      if (
              Serwer.mapa[l[0]][c[0]].img.contains("bomba1") ||
                      Serwer.mapa[l[1]][c[1]].img.contains("bomba1") ||
                      Serwer.mapa[l[2]][c[2]].img.contains("bomba1") ||
                      Serwer.mapa[l[3]][c[3]].img.contains("bomba1")
      )
         return true; //znajduje się na bombie, którą właśnie posadził, musi wyjść

      return false;
   }

   void wcisnietoKlawisz(int keyCode) {
      switch (keyCode) {
         case KeyEvent.VK_W:
            gora = true;
            dol = prawo = lewo = false;
            MenedzerKlientow.wyslijDoWszystkichKlientow(this.id + " newStatus up");
            break;
         case KeyEvent.VK_S:
            dol = true;
            gora = prawo = lewo = false;
            MenedzerKlientow.wyslijDoWszystkichKlientow(this.id + " newStatus down");
            break;
         case KeyEvent.VK_D:
            prawo = true;
            gora = dol = lewo = false;
            MenedzerKlientow.wyslijDoWszystkichKlientow(this.id + " newStatus right");
            break;
         case KeyEvent.VK_A:
            lewo = true;
            gora = dol = prawo = false;
            MenedzerKlientow.wyslijDoWszystkichKlientow(this.id + " newStatus left");
            break;
      }
   }

   void zwolnionoKlawisz(int keyCode) {
      if (keyCode != KeyEvent.VK_W && keyCode != KeyEvent.VK_S && keyCode != KeyEvent.VK_D && keyCode != KeyEvent.VK_A)
         return;

      MenedzerKlientow.wyslijDoWszystkichKlientow(this.id + " stopStatusUpdate");
      switch (keyCode) {
         case KeyEvent.VK_W:
            gora = false;
            break;
         case KeyEvent.VK_S:
            dol = false;
            break;
         case KeyEvent.VK_D:
            prawo = false;
            break;
         case KeyEvent.VK_A:
            lewo = false;
            break;
      }
   }
}