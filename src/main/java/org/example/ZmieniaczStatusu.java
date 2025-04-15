package org.example;

class ZmieniaczStatusu extends Thread {
    Gracz gracz;    // Obiekt gracza, którego status będzie zmieniany
    String status;  // Aktualny status gracza
    int indeks;     // Indeks dla animacji statusu
    boolean graczWRuchu;    // Flaga określająca, czy gracz jest w ruchu

    ZmieniaczStatusu(Gracz gracz, String poczatkowyStatus) {
        this.gracz = gracz;
        this.status = poczatkowyStatus;
        indeks = 0;                         // Początkowy indeks dla animacji
        graczWRuchu = true;                 // Domyślnie ustawia graczWRuchu na true
    }

    // Metoda run uruchamiana przy starcie wątku
    public void run() {
        while (true) {
            gracz.status = status + "-" + indeks;       // Ustawienie aktualnego statusu gracza
            if (graczWRuchu) {

                // Zwiększenie indeksu dla animacji statusu
                indeks = (++indeks) % Ludek.maxPetlaStatusu.get(status);
                gracz.panel.repaint();  // Odświeżenie panelu gracza
            }

            try {
                // Uśpienie wątku na określony czas
                Thread.sleep(Stale.CZESTOTLIWOSC_AKTUALIZACJI_STATUSU_GRACZA);
            } catch (InterruptedException e) {
                // Obsługa wyjątku przerwania wątku
            }

            if (gracz.status.equals("martwy-4")) {
                gracz.zywy = false;     // Ustawienie statusu gracza na martwy
                if (Gra.ty == gracz)
                    System.exit(1);     // Zakończenie gry, jeśli gracz umarl
            }
        }
    }

    // Metoda do ustawienia pętli statusu
    void ustawPetleStatusu(String status) {
        this.status = status;
        indeks = 1;
        graczWRuchu = true;    // Ustawienie flagi ruchu na true
    }

    // Metoda do zatrzymania pętli statusu
    void zatrzymajPetleStatusu() {
        graczWRuchu = false;        // Zatrzymanie flagi ruchu
        indeks = 0;                 // Resetowanie indeksu
    }
}
