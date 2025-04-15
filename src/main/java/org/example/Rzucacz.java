package org.example;

class Rzucacz extends Thread {
    String slowoKlucz, indeks[];
    int w, k;   // Współrzędne na mapie
    int opoznienie;     // Opoznienie między kolejnymi zmianami

    // Konstruktor klasy Rzucacz
    Rzucacz(String slowoKlucz, String indeks[], int opoznienie, int w, int k) {
        this.slowoKlucz = slowoKlucz;       // Kluczowa nazwa dla zmian na mapie
        this.indeks = indeks;               // Tablica indeksów określających kolejność zmian
        this.opoznienie = opoznienie;       // Opoznienie między kolejnymi zmianami
        this.w = w;                         // Współrzędna wiersza na mapie
        this.k = k;                         // Współrzędna kolumny na mapie
    }

    // Metoda run, wywoływana przy uruchomieniu wątku
    public void run() {
        for (String i : indeks) {
            // Wywołanie zmiany na mapie zgodnie z aktualnym indeksem
            RzutnikAktualizacjiMapy.zmienMape(slowoKlucz + "-" + i, w, k);
            try {
                sleep(opoznienie);  // Opoznienie przed następną zmianą
            } catch (InterruptedException e) {
                // Obsługa wyjątku przerwania wątku
            }
        }
        // Zmiana stanu na mapie po zakończeniu serii zmian ( po eksplozji)
        RzutnikAktualizacjiMapy.zmienMape("podloga-1", w, k);
    }
}