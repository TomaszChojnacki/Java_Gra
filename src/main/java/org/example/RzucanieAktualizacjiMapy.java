package org.example;

//wątek, który uruchamia stopniowe zmiany na mapie po zasadzeniu bomby
class RzucanieAktualizacjiMapy extends Thread {
   boolean bombaZasadzona;
   int id, l, c;

   RzucanieAktualizacjiMapy(int id) {
      this.id = id;
      this.bombaZasadzona = false;
   }

   void setBombaZasadzona(int x, int y) {
      x += Stale.SZEROKOSC_SPRITA_GRACZA / 2;
      y += 2 * Stale.WYSOKOSC_SPRITA_GRACZA / 3;

      this.c = x/Stale.ROZMIAR_SPRITA_MAPY;
      this.l = y/Stale.ROZMIAR_SPRITA_MAPY;

      this.bombaZasadzona = true;
   }

   //zmienia mapę na serwerze i u klienta
   static void zmienMape(String kluczoweSlowo, int l, int c) {
      Serwer.mapa[l][c].img = kluczoweSlowo;
      MenedzerKlientow.wyslijDoWszystkichKlientow("-1 aktualizacjaMapy " + kluczoweSlowo + " " + l + " " + c);
   }

   int getKolumnaMapy(int x) {
      return x / Stale.ROZMIAR_SPRITA_MAPY;
   }
   int getLiniaMapy(int y) {
      return y / Stale.ROZMIAR_SPRITA_MAPY;
   }

   // sprawdza, czy wybuch zabił jakiegoś stojącego gracza (współrzędne środka ciała)
   void sprawdzCzyWybuchZabilKogos(int liniaSprita, int kolumnaSprita) {
      int liniaGracza, kolumnaGracza, x, y;

      for (int id = 0; id < Stale.ILOSC_GRACZY; id++)
         if (Serwer.gracz[id].zywy) {
            x = Serwer.gracz[id].x + Stale.SZEROKOSC_SPRITA_GRACZA / 2;
            y = Serwer.gracz[id].y + 2 * Stale.WYSOKOSC_SPRITA_GRACZA / 3;

            kolumnaGracza = getKolumnaMapy(x);
            liniaGracza = getLiniaMapy(y);

            if (liniaSprita == liniaGracza && kolumnaSprita == kolumnaGracza) {
               Serwer.gracz[id].zywy = false;
               MenedzerKlientow.wyslijDoWszystkichKlientow(id + " nowyStatus martwy");
            }
         }
   }

   public void run() {
      while (true) {
         if (bombaZasadzona) {
            bombaZasadzona = false;

            for (String index: Stale.indeksZasadzonejBomby) {
               zmienMape("bomba-zasadzona-" + index, l, c);
               try {
                  sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_BOMBY);
               } catch (InterruptedException e) {}
            }

            //efekty wybuchu
            new Rzucanie("srodek-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, l, c).start();
            sprawdzCzyWybuchZabilKogos(l, c);

            //poniżej
            if (Serwer.mapa[l+1][c].img.equals("podloga-1")) {
               new Rzucanie("dol-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, l+1, c).start();
               sprawdzCzyWybuchZabilKogos(l+1, c);
            }
            else if (Serwer.mapa[l+1][c].img.contains("blok"))
               new Rzucanie("blok-w-ogniu", Stale.indeksBlokujacyOgien, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, l+1, c).start();

            //po prawej
            if (Serwer.mapa[l][c+1].img.equals("podloga-1")) {
               new Rzucanie("prawo-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, l, c+1).start();
               sprawdzCzyWybuchZabilKogos(l, c+1);
            }
            else if (Serwer.mapa[l][c+1].img.contains("blok"))
               new Rzucanie("blok-w-ogniu", Stale.indeksBlokujacyOgien, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, l, c+1).start();

            //powyżej
            if (Serwer.mapa[l-1][c].img.equals("podloga-1")) {
               new Rzucanie("gora-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, l-1, c).start();
               sprawdzCzyWybuchZabilKogos(l-1, c);
            }
            else if (Serwer.mapa[l-1][c].img.contains("blok"))
               new Rzucanie("blok-w-ogniu", Stale.indeksBlokujacyOgien, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, l-1, c).start();

            //po lewej
            if (Serwer.mapa[l][c-1].img.equals("podloga-1")) {
               new Rzucanie("lewo-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, l, c-1).start();
               sprawdzCzyWybuchZabilKogos(l, c-1);
            }
            else if (Serwer.mapa[l][c-1].img.contains("blok"))
               new Rzucanie("blok-w-ogniu", Stale.indeksBlokujacyOgien, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, l, c-1).start();

            Serwer.gracz[id].iloscBomb++; //uwolnienie bomby
         }
         try {sleep(0);} catch (InterruptedException e) {}
      }
   }
}

//wątek pomocniczy
class Rzucanie extends Thread {
   String kluczoweSlowo, indeks[];
   int l, c;
   int opoznienie;

   Rzucanie(String kluczoweSlowo, String indeks[], int opoznienie, int l, int c) {
      this.kluczoweSlowo = kluczoweSlowo;
      this.indeks = indeks;
      this.opoznienie = opoznienie;
      this.l = l;
      this.c = c;
   }

   public void run() {
      for (String i : indeks) {
         RzucanieAktualizacjiMapy.zmienMape(kluczoweSlowo + "-" + i, l, c);
         try {
            sleep(opoznienie);
         } catch (InterruptedException e) {}
      }
      //sytuacja po wybuchu
      RzucanieAktualizacjiMapy.zmienMape("podloga-1", l, c);
   }
}
