package org.example;

// Klasa RzutnikAktualizacjiMapy reprezentuje wątek, który zarządza zmianami na mapie po zasadzeniu bomby
class RzutnikAktualizacjiMapy extends Thread {
    boolean bombaZasadzona;     // Flaga wskazująca, czy bomba została zasadzona
    int id, w, k;       // Identyfikator gracza oraz współrzędne na mapie (wiersz, kolumna)

    // Konstruktor klasy
    RzutnikAktualizacjiMapy(int id) {
        this.id = id;                   // Przypisanie identyfikatora gracza
        this.bombaZasadzona = false;    // Początkowo bomba nie jest zasadzona
    }

    // Metoda ustawiająca pozycję zasadzonej bomby
    void ustawBombeZasadzona(int x, int y) {
        // Przeliczanie współrzędnych gracza na współrzędne mapy
        x += Stale.SZEROKOSC_SPRITE_GRACZA / 2;
        y += 2 * Stale.WYSOKOSC_SPRITE_GRACZA / 3;

        this.k = x / Stale.ROZMIAR_SPRITE_MAPY;     // Obliczenie kolumny na mapie
        this.w = y / Stale.ROZMIAR_SPRITE_MAPY;     // Obliczenie wiersza na mapie

        this.bombaZasadzona = true;     // Ustawienie flagi zasadzenia bomby
    }

    // Statyczna metoda zmieniająca mapę na serwerze i u klienta
    static void zmienMape(String slowoKlucz, int w, int k) {
        // Aktualizacja obrazu w konkretnej komórce tabeli mapy na serwerze
        Serwer.mapa[w][k].obraz = slowoKlucz;

        // Wysyłanie informacji o zmianie mapy do wszystkich klientów
        MenadzerKlienta.wyslijDoWszystkichKlientow("-1 aktualizacjaMapy " + slowoKlucz + " " + w + " " + k);
    }

    // Metoda obliczająca kolumnę na mapie na podstawie współrzędnej x
    int getKolumnaMapy(int x) {
        // Dzieli współrzędną x przez rozmiar ludka na mape, aby uzyskać numer kolumny na mapie
        return x / Stale.ROZMIAR_SPRITE_MAPY;
    }

    // Metoda obliczająca wiersz na mapie na podstawie współrzędnej y
    int getWierszMapy(int y) {
        // Dzieli współrzędną y przez rozmiar ludka na mape, aby uzyskać numer kolumny na mapie
        return y / Stale.ROZMIAR_SPRITE_MAPY;
    }

    // sprawdza, czy wybuch ognia dotknął jakiegoś nieruchomego gracza (współrzędne środka ciała)
    void sprawdzCzyEksplozjaZabilaKogos(int wSprita, int kSprita) {
        int wGracza, kGracza, x, y;

        // Przejście przez wszystkich graczy w grze
        for (int id = 0; id < Stale.ILU_GRACZY; id++)
            if (Serwer.gracz[id].zywy) {    // Sprawdzanie tylko "żywych" graczy
                // Obliczenie środka ciała gracza
                x = Serwer.gracz[id].x + Stale.SZEROKOSC_SPRITE_GRACZA / 2;
                y = Serwer.gracz[id].y + 2 * Stale.WYSOKOSC_SPRITE_GRACZA / 3;

                // Obliczenie pozycji gracza na mapie
                kGracza = getKolumnaMapy(x);
                wGracza = getWierszMapy(y);

                // Sprawdzenie, czy pozycja eksplozji pokrywa się z pozycją gracza
                if (wSprita == wGracza && kSprita == kGracza) {

                    // Zmiana statusu gracza na "martwy"
                    Serwer.gracz[id].zywy = false;

                    // Informowanie wszystkich klientów o zmianie statusu gracza
                    MenadzerKlienta.wyslijDoWszystkichKlientow(id + " nowyStatus martwy");
                }
            }
    }

    public void run() {
        while (true) {                      // Nieskończona pętla wątku
            if (bombaZasadzona) {           // Sprawdzenie, czy bomba została zasadzona
                bombaZasadzona = false;     // Resetowanie flagi bomby zasadzonej

                // Animacja zasadzenia bomby
                for (String indeks : Stale.indeksBombaZasadzona) {
                    zmienMape("bomba-zasadzona-" + indeks, w, k);   // Zmiana mapy na serwerze i u klienta
                    try {
                        sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_BOMBY);  // Oczekiwanie między klatkami animacji
                    } catch (InterruptedException e) {
                        // Obsługa wyjątku przerwania wątku
                    }
                }

                // Rozpoczęcie efektów eksplozji
                new Rzucacz("centrum-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w, k).start();
                sprawdzCzyEksplozjaZabilaKogos(w, k);   // Sprawdzenie, czy eksplozja zabiła kogoś

                // poniżej
                if (Serwer.mapa[w + 1][k].obraz.equals("podloga-1")) {
                    new Rzucacz("dol-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w + 1, k).start();
                    sprawdzCzyEksplozjaZabilaKogos(w + 1, k);
                } else if (Serwer.mapa[w + 1][k].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w + 1, k).start();

                // po prawej
                if (Serwer.mapa[w][k + 1].obraz.equals("podloga-1")) {
                    new Rzucacz("prawo-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w, k + 1).start();
                    sprawdzCzyEksplozjaZabilaKogos(w, k + 1);
                } else if (Serwer.mapa[w][k + 1].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w, k + 1).start();

                // powyżej
                if (Serwer.mapa[w - 1][k].obraz.equals("podloga-1")) {
                    new Rzucacz("gora-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w - 1, k).start();
                    sprawdzCzyEksplozjaZabilaKogos(w - 1, k);
                } else if (Serwer.mapa[w - 1][k].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w - 1, k).start();

                // po lewej
                if (Serwer.mapa[w][k - 1].obraz.equals("podloga-1")) {
                    new Rzucacz("lewo-wybuchu", Stale.indeksEksplozji, Stale.CZESTOTLIWOSC_AKTUALIZACJI_OGNIA, w, k - 1).start();
                    sprawdzCzyEksplozjaZabilaKogos(w, k - 1);
                } else if (Serwer.mapa[w][k - 1].obraz.contains("blok"))
                    new Rzucacz("blok-w-ogniu", Stale.indeksBlokWPlomieniach, Stale.CZESTOTLIWOSC_AKTUALIZACJI_BLOKU, w, k - 1).start();

                Serwer.gracz[id].liczbaBomb++; // Zwiększenie liczby dostępnych bomb dla gracza po eksplozji z 0 na 1
            }
            try {
                sleep(0);   // Krótkie uśpienie wątku
            } catch (InterruptedException e) {
                // Obsługa wyjątku przerwania wątku
            }
        }
    }
}
