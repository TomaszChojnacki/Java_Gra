package org.example;

// wątek, który uruchamia stopniowe zmiany na mapie, które następują zaraz po zasadzeniu bomby
class RzutnikAktualizacjiMapy extends Thread {
    boolean bombaZasadzona;
    int id, w, k;

    RzutnikAktualizacjiMapy(int id) {
        this.id = id;
        this.bombaZasadzona = false;
    }

    void ustawBombeZasadzona(int x, int y) {
        x += Stale.SZEROKOSC_SPRITE_GRACZA / 2;
        y += 2 * Stale.WYSOKOSC_SPRITE_GRACZA / 3;

        this.k = x/Stale.ROZMIAR_SPRITE_MAPY;
        this.w = y/Stale.ROZMIAR_SPRITE_MAPY;

        this.bombaZasadzona = true;
    }

    // zmienia mapę na serwerze i u klienta
    static void zmienMape(String slowoKlucz, int w, int k) {
        Serwer.mapa[w][k].obraz = slowoKlucz;
        MenadzerKlienta.wyslijDoWszystkichKlientow("-1 aktualizacjaMapy " + slowoKlucz + " " + w + " " + k);
    }

    int getKolumnaMapy(int x) {
        return x / Stale.ROZMIAR_SPRITE_MAPY;
    }
    int getWierszMapy(int y) {
        return y / Stale.ROZMIAR_SPRITE_MAPY;
    }

    // sprawdza, czy wybuch ognia dotknął jakiegoś nieruchomego gracza (współrzędne środka ciała)
    void sprawdzCzyEksplozjaZabilaKogos(int wSprita, int kSprita) {
        int wGracza, kGracza, x, y;

        for (int id = 0; id < Stale.ILU_GRACZY; id++)
            if (Serwer.gracz[id].zywy) {
                x = Serwer.gracz[id].x + Stale.SZEROKOSC_SPRITE_GRACZA / 2;
                y = Serwer.gracz[id].y + 2 * Stale.WYSOKOSC_SPRITE_GRACZA / 3;

                kGracza = getKolumnaMapy(x);
                wGracza = getWierszMapy(y);

                if (wSprita == wGracza && kSprita == kGracza) {
                    Serwer.gracz[id].zywy = false;
                    MenadzerKlienta.wyslijDoWszystkichKlientow(id + " nowyStatus martwy");
                }
            }
    }

    public void run() {
        while (true) {
            if (bombaZasadzona) {
                bombaZasadzona = false;

                for (String indeks: Stale.indeksBombaZasadzona) {
                    zmienMape("bomba-zasadzona-" + indeks, w, k);
                    try {
                        sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_BOMBY);
                    } catch (InterruptedException e) {}
                }

                // efekty eksplozji
                new Rzucacz("centrum-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w, k).start();
                sprawdzCzyEksplozjaZabilaKogos(w, k);

                // poniżej
                if (Serwer.mapa[w+1][k].obraz.equals("podloga-1")) {
                    new Rzucacz("dol-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w+1, k).start();
                    sprawdzCzyEksplozjaZabilaKogos(w+1, k);
                }
                else if (Serwer.mapa[w+1][k].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w+1, k).start();

                // po prawej
                if (Serwer.mapa[w][k+1].obraz.equals("podloga-1")) {
                    new Rzucacz("prawo-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w, k+1).start();
                    sprawdzCzyEksplozjaZabilaKogos(w, k+1);
                }
                else if (Serwer.mapa[w][k+1].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w, k+1).start();

                // powyżej
                if (Serwer.mapa[w-1][k].obraz.equals("podloga-1")) {
                    new Rzucacz("gora-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w-1, k).start();
                    sprawdzCzyEksplozjaZabilaKogos(w-1, k);
                }
                else if (Serwer.mapa[w-1][k].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w-1, k).start();

                // po lewej
                if (Serwer.mapa[w][k-1].obraz.equals("podloga-1")) {
                    new Rzucacz("lewo-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w, k-1).start();
                    sprawdzCzyEksplozjaZabilaKogos(w, k-1);
                }
                else if (Serwer.mapa[w][k-1].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w, k-1).start();

                Serwer.gracz[id].liczbaBomb++; // zwolnienie bomby
            }
            try {sleep(0);} catch (InterruptedException e) {}
        }
    }
}

// wątek pomocniczy
class Rzucacz extends Thread {
    String slowoKlucz, indeks[];
    int w, k;
    int opoznienie;

    Rzucacz(String slowoKlucz, String indeks[], int opoznienie, int w, int k) {
        this.slowoKlucz = slowoKlucz;
        this.indeks = indeks;
        this.opoznienie = opoznienie;
        this.w = w;
        this.k = k;
    }

    public void run() {
        for (String i : indeks) {
            RzutnikAktualizacjiMapy.zmienMape(slowoKlucz + "-" + i, w, k);
            try {
                sleep(opoznienie);
            } catch (InterruptedException e) {}
        }
        // sytuacja po eksplozji
        RzutnikAktualizacjiMapy.zmienMape("podloga-1", w, k);
    }
}


