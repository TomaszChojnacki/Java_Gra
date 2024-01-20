package org.example;

import java.awt.event.KeyEvent;

// wątek, który wysyła kolejne współrzędne do klientów, dopóki klawisze W/A/S/D nie zostaną zwolnione
class RzutnikKoordynatow extends Thread {
    boolean gora, prawo, lewo, dol;
    int id;

    RzutnikKoordynatow(int id) {
        this.id = id;
        gora = dol = prawo = lewo = false;
    }

    public void run() {
        int nowyX = Serwer.gracz[id].x;
        int nowyY = Serwer.gracz[id].y;

        while (true) {
            if (gora || dol || prawo || lewo) {
                if (gora)           nowyY = Serwer.gracz[id].y - Stale.ZMIANA_ROZMIARU;
                else if (dol)    nowyY = Serwer.gracz[id].y + Stale.ZMIANA_ROZMIARU;
                else if (prawo)   nowyX = Serwer.gracz[id].x + Stale.ZMIANA_ROZMIARU;
                else if (lewo)    nowyX = Serwer.gracz[id].x - Stale.ZMIANA_ROZMIARU;

                if (wspolrzedneSaWazne(nowyX, nowyY)) {
                    MenadzerKlienta.wyslijDoWszystkichKlientow(id + " noweKoordynaty " + nowyX + " " + nowyY);

                    Serwer.gracz[id].x = nowyX;
                    Serwer.gracz[id].y = nowyY;
                } else {
                    nowyX = Serwer.gracz[id].x;
                    nowyY = Serwer.gracz[id].y;
                }
                try {
                    sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_WSPOLRZEDNYCH);
                } catch (InterruptedException e) {}
            }
            try {sleep(0);} catch (InterruptedException e) {}
        }
    }

    int getKolumnaMapy(int x) {
        return x/Stale.ROZMIAR_SPRITE_MAPY;
    }
    int getWierszMapy(int y) {
        return y/Stale.ROZMIAR_SPRITE_MAPY;
    }

    // sprawdza, nad którymi sprite'ami mapy znajduje się gracz i czy są one ważne
    boolean wspolrzedneSaWazne(int nowyX, int nowyY) {
        if (!Serwer.gracz[id].zywy)
            return false;

        // sprawdza, czy gracz wszedł w ogień (współrzędne środka ciała)
        int xCialo = nowyX + Stale.SZEROKOSC_SPRITE_GRACZA/2;
        int yCialo = nowyY + 2*Stale.WYSOKOSC_SPRITE_GRACZA/3;

        if (Serwer.mapa[getWierszMapy(yCialo)][getKolumnaMapy(xCialo)].obraz.contains("eksplozja")) {
            Serwer.gracz[id].zywy = false;
            MenadzerKlienta.wyslijDoWszystkichKlientow(id + " nowyStatus martwy");
            return true;
        }

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

        for (int i = 0; i < 4; i++) {
            c[i] = getKolumnaMapy(x[i]);
            l[i] = getWierszMapy(y[i]);
        }

        if (
                (Serwer.mapa[l[0]][c[0]].obraz.equals("podloga-1") || Serwer.mapa[l[0]][c[0]].obraz.contains("eksplozja")) &&
                        (Serwer.mapa[l[1]][c[1]].obraz.equals("podloga-1") || Serwer.mapa[l[1]][c[1]].obraz.contains("eksplozja")) &&
                        (Serwer.mapa[l[2]][c[2]].obraz.equals("podloga-1") || Serwer.mapa[l[2]][c[2]].obraz.contains("eksplozja")) &&
                        (Serwer.mapa[l[3]][c[3]].obraz.equals("podloga-1") || Serwer.mapa[l[3]][c[3]].obraz.contains("eksplozja"))
        )
            return true; // znajduje się na ważnej współrzędnej

        if (
                (Serwer.mapa[l[0]][c[0]].obraz.contains("blok") || Serwer.mapa[l[0]][c[0]].obraz.contains("sciana")) ||
                        (Serwer.mapa[l[1]][c[1]].obraz.contains("blok") || Serwer.mapa[l[1]][c[1]].obraz.contains("sciana")) ||
                        (Serwer.mapa[l[2]][c[2]].obraz.contains("blok") || Serwer.mapa[l[2]][c[2]].obraz.contains("sciana")) ||
                        (Serwer.mapa[l[3]][c[3]].obraz.contains("blok") || Serwer.mapa[l[3]][c[3]].obraz.contains("sciana"))
        )
            return false; // znajduje się na ścianie

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

        for (int i = 0; i < 4; i++) {
            c[i] = getKolumnaMapy(x[i]);
            l[i] = getWierszMapy(y[i]);
        }

        if (
                Serwer.mapa[l[0]][c[0]].obraz.contains("bomba-zasadzona") ||
                        Serwer.mapa[l[1]][c[1]].obraz.contains("bomba-zasadzona") ||
                        Serwer.mapa[l[2]][c[2]].obraz.contains("bomba-zasadzona") ||
                        Serwer.mapa[l[3]][c[3]].obraz.contains("bomba-zasadzona")
        )
            return true; // był na bombie, którą właśnie zasadził, musi wyjść

        return false;
    }

    void klawiszWcisniety(int kodKlawisza) {
        switch (kodKlawisza) {
            case KeyEvent.VK_W:
                gora = true; dol = prawo = lewo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus gora");
                break;
            case KeyEvent.VK_S:
                dol = true; gora = prawo = lewo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus dol");
                break;
            case KeyEvent.VK_D:
                prawo = true; gora = dol = lewo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus prawo");
                break;
            case KeyEvent.VK_A:
                lewo = true; gora = dol = prawo = false;
                MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " nowyStatus lewo");
                break;
        }
    }

    void klawiszZwolniony(int kodKlawisza) {
        if (kodKlawisza != KeyEvent.VK_W && kodKlawisza != KeyEvent.VK_S && kodKlawisza != KeyEvent.VK_D && kodKlawisza != KeyEvent.VK_A)
            return;

        MenadzerKlienta.wyslijDoWszystkichKlientow(this.id + " zatrzymajAktualizacjeStatusu");
        switch (kodKlawisza) {
            case KeyEvent.VK_W: gora = false; break;
            case KeyEvent.VK_S: dol = false; break;
            case KeyEvent.VK_D: prawo = false; break;
            case KeyEvent.VK_A: lewo = false; break;
        }
    }
}



