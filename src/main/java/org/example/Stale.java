// dostępne dla serwera i klienta
package org.example;
interface Stale {
   // jeśli nie jest to 4, wiele rzeczy musi zostać zaniechanych
   final static int ILOSC_GRACZY = 4;

   final static int LINIE = 9, KOLUMNY = 9; // zawsze nieparzyste
   final static int POWIEKSZENIE = 4; // rozmiar piksela

   final static int ROZMIAR_SPRITA_MAPY = 16 * POWIEKSZENIE;
   final static int SZEROKOSC_SPRITA_GRACZA = 22 * POWIEKSZENIE;
   final static int WYSOKOSC_SPRITA_GRACZA = 33 * POWIEKSZENIE;

   // różnica pikseli między spritem mapy a graczem
   final static int ROZNICA_X_SPRITOW = 3 * POWIEKSZENIE;
   final static int ROZNICA_Y_SPRITOW = 16 * POWIEKSZENIE;

   final static int CZESTOTLIWOSC_AKTUALIZACJI_BOMBY = 90;
   final static int CZESTOTLIWOSC_AKTUALIZACJI_BLOKU = 100;
   final static int CZESTOTLIWOSC_AKTUALIZACJI_OGNIA = 35;
   final static int CZESTOTLIWOSC_AKTUALIZACJI_STATUSU_GRACZA = 90;
   final static int CZESTOTLIWOSC_AKTUALIZACJI_WSPOLRZEDNYCH = 27;

   final static String indeksZasadzonejBomby[] = {
           "1", "2", "3", "2", "1", "2", "3", "2", "1", "2", "3", "2", "1", "2",
           "czerwony-3", "czerwony-2", "czerwony-1", "czerwony-2", "czerwony-3", "czerwony-2", "czerwony-3", "czerwony-2", "czerwony-3", "czerwony-2", "czerwony-3"
   };
   final static String indeksEksplozji[] = {
           "1", "2", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5", "4", "3", "2", "1"
   };
   final static String indeksBlokujacyOgien[] = {
           "1", "2", "1", "2", "1", "2", "3", "4", "5", "6"
   };
}

class Wspolrzedne {
   public int x, y;
   String img;

   Wspolrzedne(int x, int y) {
      this.x = x;
      this.y = y;
   }

   Wspolrzedne(int x, int y, String img) {
      this.x = x;
      this.y = y;
      this.img = img;
   }
}
