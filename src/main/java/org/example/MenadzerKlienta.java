package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Klasa MenadzerKlienta reprezentuje wątek serwera, który jest tworzony dla każdego klienta dołączającego do serwera.
class MenadzerKlienta extends Thread {
    // Lista przechowująca strumienie wyjściowe do wszystkich klientów
    static List<PrintStream> listaWyjscKlientow = new ArrayList<PrintStream>();

    // Metoda wysyłająca daną linijkę tekstu do wszystkich klientów
    static void wyslijDoWszystkichKlientow(String liniaWyjsciowa) {
        for (PrintStream wyjscieKlienta : listaWyjscKlientow)
            wyjscieKlienta.println(liniaWyjsciowa);
    }

    private Socket gniazdoKlienta = null;  // Gniazdo sieciowe dla połączenia z klientem
    private Scanner wejscie = null;     // Scanner do odczytu danych od klienta
    private PrintStream wyjscie = null;     // PrintStream do wysyłania danych do klienta
    private int id;     // Identyfikator klienta

    RzutnikKoordynatow rk;      // Obiekt do zarządzania koordynatami
    RzutnikAktualizacjiMapy ram;       // Obiekt do zarządzania aktualizacjami mapy

    // Konstruktor klasy MenadzerKlienta
    MenadzerKlienta(Socket gniazdoKlienta, int id) {
        this.id = id;
        this.gniazdoKlienta = gniazdoKlienta;
        // Uruchomienie wątków do zarządzania koordynatami i mapą
        (rk = new RzutnikKoordynatow(this.id)).start();
        (ram = new RzutnikAktualizacjiMapy(this.id)).start();

        try {
            System.out.print("\n" + "Inicjalizacja połączenia " + this.id + "...");
            this.wejscie = new Scanner(gniazdoKlienta.getInputStream()); // otrzymywać od klienta
            this.wyjscie = new PrintStream(gniazdoKlienta.getOutputStream(), true); // do wysyłania do klienta
        } catch (IOException e) {
            // Obsługa wyjątków związanych z błędami wejścia/wyjścia
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");

        // Dodanie strumienia wyjściowego do globalnej listy i aktualizacja statusu gracza
        listaWyjscKlientow.add(wyjscie);
        Serwer.gracz[id].zalogowany = true;
        Serwer.gracz[id].zywy = true;
        wyslijPoczatkoweUstawienia(); // // Wysłanie początkowych ustawień do klienta jedna linia

        // powiadamie serwera o dolaczeniu gracza
        for (PrintStream wyjscieKlienta : listaWyjscKlientow)
            if (wyjscieKlienta != this.wyjscie)
                wyjscieKlienta.println(id + " graczDolaczyl");
    }

    public void run() {
        while (wejscie.hasNextLine()) { // połączenie nawiązane z klientem this.id
            // Pętla działa dopóki istnieją dane do odczytu od klienta
            // Odczytanie linii danych i jej podział na poszczególne elementy
            String str[] = wejscie.nextLine().split(" ");

            // Sprawdzanie, jaki rodzaj akcji wykonuje klient
            if (str[0].equals("klawiszWcisniety") && Serwer.gracz[id].zywy) {

                // Jeśli klient wcisnął klawisz i jest "żywy", wykonaj odpowiednią akcję
                rk.klawiszWcisniety(Integer.parseInt(str[1]));

            } else if (str[0].equals("klawiszZwolniony") && Serwer.gracz[id].zywy) {

                // Jeśli klient zwolnił klawisz i jest "żywy", wykonaj odpowiednią akcję
                rk.klawiszZwolniony(Integer.parseInt(str[1]));

            } else if (str[0].equals("wcisnietoSpacje") && Serwer.gracz[id].liczbaBomb >= 1) {

                // Jeśli klient wcisnął spację i ma dostępną bombę, wykonaj odpowiednią akcję
                Serwer.gracz[id].liczbaBomb--;
                ram.ustawBombeZasadzona(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
            }
        }
        klientRozlaczony(); // Obsługa rozłączenia klienta
    }

    void wyslijPoczatkoweUstawienia() {
        wyjscie.print(id);  // Wysłanie identyfikatora klienta
        // Wysyłanie początkowego stanu mapy
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                wyjscie.print(" " + Serwer.mapa[i][j].obraz);

        // Wysyłanie stanu życia wszystkich graczy
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            wyjscie.print(" " + Serwer.gracz[i].zywy);

        // Wysyłanie początkowych pozycji graczy
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            wyjscie.print(" " + Serwer.gracz[i].x + " " + Serwer.gracz[i].y);
        wyjscie.print("\n");
    }

    void klientRozlaczony() {
        listaWyjscKlientow.remove(wyjscie); // Usunięcie wyjścia tego klienta z listy aktywnych klientów
        Serwer.gracz[id].zalogowany = false;    // Oznaczenie, że gracz nie jest już zalogowany
        try {
            // Zamknięcie wszystkich strumieni i gniazda sieciowego
            System.out.print("Zakończenie połączenia " + this.id + "...");
            wejscie.close();
            wyjscie.close();
            gniazdoKlienta.close();
        } catch (IOException e) {
            // Obsługa błędów
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");
    }
}


