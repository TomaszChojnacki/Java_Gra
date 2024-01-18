package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class DaneGracza {
   boolean zalogowany, zywy;
   int x, y; //aktualna koordynacja
   int iloscBomb;

   DaneGracza(int x, int y) {
      this.x = x;
      this.y = y;
      this.zalogowany = false;
      this.zywy = false;
      this.iloscBomb = 1; // dla 2 bomb, trzeba obsłużyć każdą bombę w oddzielnym wątku
   }
}

class Serwer {
   static DaneGracza gracz[] = new DaneGracza[Stale.ILOSC_GRACZY];
   static Wspolrzedne mapa[][] = new Wspolrzedne[Stale.LINIE][Stale.KOLUMNY];

   Serwer(int numerPortu) {
      ServerSocket ss;

      ustawMape();
      ustawDaneGracza();

      try {
         System.out.print("Otwieranie portu " + numerPortu + "...");
         ss = new ServerSocket(numerPortu); // socket nasłuchuje portu
         System.out.print(" ok\n");

         for (int id = 0; !zalogowaniSaPelni(); id = (++id)%Stale.ILOSC_GRACZY)
            if (!gracz[id].zalogowany) {
               Socket gniazdoKlienta = ss.accept();
               new MenedzerKlientow(gniazdoKlienta, id).start();
            }
         //serwer nie kończy działania, dopóki wątki klientów są aktywne
      } catch (IOException e) {
         System.out.println(" blad: " + e + "\n");
         System.exit(1);
      }
   }

   boolean zalogowaniSaPelni() {
      for (int i = 0; i < Stale.ILOSC_GRACZY; i++)
         if (!gracz[i].zalogowany)
            return false;
      return true;
   }

   void ustawMape() {
      for (int i = 0; i < Stale.LINIE; i++)
         for (int j = 0; j < Stale.KOLUMNY; j++)
            mapa[i][j] = new Wspolrzedne(Stale.ROZMIAR_SPRITA_MAPY * j, Stale.ROZMIAR_SPRITA_MAPY * i, "blok");

      // stałe ściany brzegowe
      for (int j = 1; j < Stale.KOLUMNY - 1; j++) {
         mapa[0][j].img = "sciana-srodek";
         mapa[Stale.LINIE - 1][j].img = "sciana-srodek";
      }
      for (int i = 1; i < Stale.LINIE - 1; i++) {
         mapa[i][0].img = "sciana-srodek";
         mapa[i][Stale.KOLUMNY - 1].img = "sciana-srodek";
      }
      mapa[0][0].img = "sciana-gora-lewo";
      mapa[0][Stale.KOLUMNY - 1].img = "sciana-gora-prawo";
      mapa[Stale.LINIE - 1][0].img = "sciana-dol-lewo";
      mapa[Stale.LINIE - 1][Stale.KOLUMNY - 1].img = "sciana-dol-prawo";

      // stałe ściany centralne
      for (int i = 2; i < Stale.LINIE - 2; i++)
         for (int j = 2; j < Stale.KOLUMNY - 2; j++)
            if (i % 2 == 0 && j % 2 == 0)
               mapa[i][j].img = "sciana-srodek";

      // otoczenie punktu startowego
      mapa[1][1].img = "podloga-1";
      mapa[1][2].img = "podloga-1";
      mapa[2][1].img = "podloga-1";
      mapa[Stale.LINIE - 2][Stale.KOLUMNY - 2].img = "podloga-1";
      mapa[Stale.LINIE - 3][Stale.KOLUMNY - 2].img = "podloga-1";
      mapa[Stale.LINIE - 2][Stale.KOLUMNY - 3].img = "podloga-1";
      mapa[Stale.LINIE - 2][1].img = "podloga-1";
      mapa[Stale.LINIE - 3][1].img = "podloga-1";
      mapa[Stale.LINIE - 2][2].img = "podloga-1";
      mapa[1][Stale.KOLUMNY - 2].img = "podloga-1";
      mapa[2][Stale.KOLUMNY - 2].img = "podloga-1";
      mapa[1][Stale.KOLUMNY - 3].img = "podloga-1";
   }

   void ustawDaneGracza() {
      gracz[0] = new DaneGracza(
              mapa[1][1].x - Stale.ROZNICA_X_SPRITOW,
              mapa[1][1].y - Stale.ROZNICA_Y_SPRITOW
      );

      gracz[1] = new DaneGracza(
              mapa[Stale.LINIE - 2][Stale.KOLUMNY - 2].x - Stale.ROZNICA_X_SPRITOW,
              mapa[Stale.LINIE - 2][Stale.KOLUMNY - 2].y - Stale.ROZNICA_Y_SPRITOW
      );
      gracz[2] = new DaneGracza(
              mapa[Stale.LINIE - 2][1].x - Stale.ROZNICA_X_SPRITOW,
              mapa[Stale.LINIE - 2][1].y - Stale.ROZNICA_Y_SPRITOW
      );
      gracz[3] = new DaneGracza(
              mapa[1][Stale.KOLUMNY - 2].x - Stale.ROZNICA_X_SPRITOW,
              mapa[1][Stale.KOLUMNY - 2].y - Stale.ROZNICA_Y_SPRITOW
      );
   }

   public static void main(String[] args) {
      new Serwer(8383);
   }
}
