package org.example;

import java.awt.event.KeyEvent;

// wątek, który wysyła kolejne współrzędne do klientów, dopóki klawisze W/A/S/D nie zostaną zwolnione
class RzutnikKoordynatow extends Thread {
    boolean gora;       // Flagi kierunków ruchu gracza
    boolean prawo;
    boolean lewo;
    boolean dol;
    int id;         // Identyfikator gracza

    // Konstruktor klasy
    RzutnikKoordynatow(int id) {
        this.id = id;
        gora = dol = prawo = lewo = false;  // Inicjalizacja flag kierunków ruchu
    }

    // Metoda run uruchamiana przy starcie wątku
    public void run() {
        int nowyX = Serwer.gracz[id].x;     // Aktualna pozycja X gracza
        int nowyY = Serwer.gracz[id].y;     // Aktualna pozycja Y gracza

        while (true) {      // Nieskończona pętla aktualizacji pozycji gracza
            if (gora || dol || prawo || lewo) {     // Sprawdzenie, czy jakiś kierunek jest aktywny

                // Aktualizacja pozycji gracza w zależności od wciśniętych klawiszy
                if (gora) nowyY = Serwer.gracz[id].y - Stale.ZMIANA_ROZMIARU;
                else if (dol) nowyY = Serwer.gracz[id].y + Stale.ZMIANA_ROZMIARU;
                else if (prawo) nowyX = Serwer.gracz[id].x + Stale.ZMIANA_ROZMIARU;
                else if (lewo) nowyX = Serwer.gracz[id].x - Stale.ZMIANA_ROZMIARU;

                // Sprawdzenie, czy nowe współrzędne są ważne
                if (wspolrzedneSaWazne(nowyX, nowyY)) {

                    // Wysłanie nowych współrzędnych do wszystkich klientów
                    MenadzerKlienta.wyslijDoWszystkichKlientow(id + " noweKoordynaty " + nowyX + " " + nowyY);

                    // Aktualizacja współrzędnych gracza na serwerze
                    Serwer.gracz[id].x = nowyX;
                    Serwer.gracz[id].y = nowyY;
                } else {

                    // Resetowanie pozycji do poprzedniej, jeśli nowe współrzędne nie są ważne
                    nowyX = Serwer.gracz[id].x;
                    nowyY = Serwer.gracz[id].y;
                }
                try {
                    sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_WSPOLRZEDNYCH);  // Oczekiwanie przed kolejną aktualizacją
                } catch (InterruptedException e) {
                    // Obsługa przerwania wątku
                }
            }
            try {
                sleep(0);       // Krótkie uśpienie wątku
            } catch (InterruptedException e) {
                // Obsługa przerwania wątku
            }
        }
    }

    // Metoda obliczająca indeks kolumny na mapie na podstawie współrzędnej x
    int getKolumnaMapy(int x) {
        return x / Stale.ROZMIAR_SPRITE_MAPY;   // Dzielenie współrzędnej x przez rozmiar ludka na mapie
    }

    // Metoda obliczająca indeks wiersza na mapie na podstawie współrzędnej y
    int getWierszMapy(int y) {
        return y / Stale.ROZMIAR_SPRITE_MAPY;   // Dzielenie współrzędnej y przez rozmiar ludka na mapie
    }

    // Metoda sprawdzająca, czy nowe współrzędne gracza są ważne
    boolean wspolrzedneSaWazne(int nowyX, int nowyY) {

        // Jeśli gracz nie jest żywy, współrzędne nie są ważne
        if (!Serwer.gracz[id].zywy)
            return false;

        // sprawdza, czy gracz wszedł w ogień (współrzędne środka ciała)
        int xCialo = nowyX + Stale.SZEROKOSC_SPRITE_GRACZA / 2;
        int yCialo = nowyY + 2 * Stale.WYSOKOSC_SPRITE_GRACZA / 3;

        if (Serwer.mapa[getWierszMapy(yCialo)][getKolumnaMapy(xCialo)].obraz.contains("eksplozja")) {

            // Jeśli gracz wszedł w eksplozję, ustaw jego status na martwy
            Serwer.gracz[id].zywy = false;
            MenadzerKlienta.wyslijDoWszystkichKlientow(id + " nowyStatus martwy");
            return true;
        }

        // Obliczenie współrzędnych dla czterech rogów ludka gracza
        int x[] = new int[4], y[] = new int[4];
        int c[] = new int[4], l[] = new int[4];

        // W ODNIESIENIU DO NOWYCH WSPÓŁRZĘDNYCH

        // 0: punkt w lewym górnym rogu
        x[0] = Stale.VAR_X_SPRITE + nowyX + Stale.ZMIANA_ROZMIARU;
        y[0] = Stale.VAR_Y_SPRITE + nowyY + Stale.ZMIANA_ROZMIARU;
        // 1: punkt w prawym górnym rogu
        x[1] = Stale.VAR_X_SPRITE + nowyX + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;
        y[1] = Stale.VAR_Y_SPRITE + nowyY + Stale.ZMIANA_ROZMIARU;
        // 2: punkt w lewym dolnym rogu
        x[2] = Stale.VAR_X_SPRITE + nowyX + Stale.ZMIANA_ROZMIARU;
        y[2] = Stale.VAR_Y_SPRITE + nowyY + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;
        // 3: punkt w prawym dolnym rogu
        x[3] = Stale.VAR_X_SPRITE + nowyX + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;
        y[3] = Stale.VAR_Y_SPRITE + nowyY + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;


        // Sprawdzenie, czy każdy z rogów ludka gracza znajduje się na ważnych współrzędnych
        for (int i = 0; i < 4; i++) {
            c[i] = getKolumnaMapy(x[i]);
            l[i] = getWierszMapy(y[i]);
        }

        // Sprawdzenie, czy nowe współrzędne gracza znajdują się na ważnych polach
        if (
                // Sprawdzanie każdego z czterech rogów ludka gracza
                (Serwer.mapa[l[0]][c[0]].obraz.equals("podloga-1") || Serwer.mapa[l[0]][c[0]].obraz.contains("eksplozja")) &&
                (Serwer.mapa[l[1]][c[1]].obraz.equals("podloga-1") || Serwer.mapa[l[1]][c[1]].obraz.contains("eksplozja")) &&
                (Serwer.mapa[l[2]][c[2]].obraz.equals("podloga-1") || Serwer.mapa[l[2]][c[2]].obraz.contains("eksplozja")) &&
                (Serwer.mapa[l[3]][c[3]].obraz.equals("podloga-1") || Serwer.mapa[l[3]][c[3]].obraz.contains("eksplozja"))
        )
            return true; // Gracz znajduje się na polu, które jest albo podłogą, albo eksplozją

        // Sprawdzenie, czy gracz napotkał blok lub ścianę
        if (

                (Serwer.mapa[l[0]][c[0]].obraz.contains("blok") || Serwer.mapa[l[0]][c[0]].obraz.contains("sciana")) ||
                (Serwer.mapa[l[1]][c[1]].obraz.contains("blok") || Serwer.mapa[l[1]][c[1]].obraz.contains("sciana")) ||
                (Serwer.mapa[l[2]][c[2]].obraz.contains("blok") || Serwer.mapa[l[2]][c[2]].obraz.contains("sciana")) ||
                (Serwer.mapa[l[3]][c[3]].obraz.contains("blok") || Serwer.mapa[l[3]][c[3]].obraz.contains("sciana"))
        )
            return false; // Gracz napotkał blok lub ścianę, więc współrzędne nie są ważne

        // W ODNIESIENIU DO POPRZEDNICH WSPÓŁRZĘDNYCH

        // 0: punkt w lewym górnym rogu
        x[0] = Stale.VAR_X_SPRITE + Serwer.gracz[id].x + Stale.ZMIANA_ROZMIARU;
        y[0] = Stale.VAR_Y_SPRITE + Serwer.gracz[id].y + Stale.ZMIANA_ROZMIARU;
        // 1: punkt w prawym górnym rogu
        x[1] = Stale.VAR_X_SPRITE + Serwer.gracz[id].x + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;
        y[1] = Stale.VAR_Y_SPRITE + Serwer.gracz[id].y + Stale.ZMIANA_ROZMIARU;
        // 2: punkt w lewym dolnym rogu
        x[2] = Stale.VAR_X_SPRITE + Serwer.gracz[id].x + Stale.ZMIANA_ROZMIARU;
        y[2] = Stale.VAR_Y_SPRITE + Serwer.gracz[id].y + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;
        // 3: punkt w prawym dolnym rogu
        x[3] = Stale.VAR_X_SPRITE + Serwer.gracz[id].x + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;
        y[3] = Stale.VAR_Y_SPRITE + Serwer.gracz[id].y + Stale.ROZMIAR_SPRITE_MAPY - 2 * Stale.ZMIANA_ROZMIARU;


        // Sprawdzenie, czy każdy z rogów ludka gracza znajduje się na ważnych współrzędnych
        for (int i = 0; i < 4; i++) {
            c[i] = getKolumnaMapy(x[i]);
            l[i] = getWierszMapy(y[i]);
        }

        // Sprawdzanie, czy gracz znajduje się na zasadzonej bombie
        if (
                Serwer.mapa[l[0]][c[0]].obraz.contains("bomba-zasadzona") ||
                Serwer.mapa[l[1]][c[1]].obraz.contains("bomba-zasadzona") ||
                Serwer.mapa[l[2]][c[2]].obraz.contains("bomba-zasadzona") ||
                Serwer.mapa[l[3]][c[3]].obraz.contains("bomba-zasadzona")
        )
            return true; // był na bombie, którą właśnie zasadził, musi wyjść

        return false; //współrzędne  nie jest ważne
    }


    // Obsługa zdarzenia wciśnięcia klawisza
    void klawiszWcisniety(int kodKlawisza) {
        switch (kodKlawisza) {
            case KeyEvent.VK_W:             // Obsługa klawisza 'W' - ruch do góry
                gora = true;
                dol = prawo = lewo = false;
                // Wysyłanie nowego statusu ruchu do wszystkich klientów
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus gora");
                break;
            case KeyEvent.VK_S:            // Obsługa klawisza 'S' - ruch do dołu
                dol = true;
                gora = prawo = lewo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus dol");
                break;
            case KeyEvent.VK_D:           // Obsługa klawisza 'D' - ruch w prawo
                prawo = true;
                gora = dol = lewo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus prawo");
                break;
            case KeyEvent.VK_A:          // Obsługa klawisza 'A' - ruch w lewo
                lewo = true;
                gora = dol = prawo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus lewo");
                break;
        }
    }


    // Obsługa zdarzenia zwolnienia klawisza
    void klawiszZwolniony(int kodKlawisza) {
        if (kodKlawisza != KeyEvent.VK_W && kodKlawisza != KeyEvent.VK_S && kodKlawisza != KeyEvent.VK_D && kodKlawisza != KeyEvent.VK_A)
            return;

        // Wysłanie informacji o zatrzymaniu aktualizacji statusu ruchu
        MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " zatrzymajAktualizacjeStatusu");
        switch (kodKlawisza) {

            // Resetowanie odpowiedniej flagi ruchu
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


