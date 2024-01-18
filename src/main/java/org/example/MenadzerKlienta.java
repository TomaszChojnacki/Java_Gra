package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// dla każdego klienta dołączającego do serwera tworzony jest nowy wątek, który go obsługuje
class MenadzerKlienta extends Thread {
    static List<PrintStream> listaWyjscKlientow = new ArrayList<PrintStream>();

    static void wyslijDoWszystkichKlientow(String liniaWyjsciowa) {
        for (PrintStream wyjscieKlienta : listaWyjscKlientow)
            wyjscieKlienta.println(liniaWyjsciowa);
    }

    private Socket gniazdoKlienta = null;
    private Scanner wejscie = null;
    private PrintStream wyjscie = null;
    private int id;

    RzutnikKoordynatow rk;
    RzutnikAktualizacjiMapy ram;

    MenadzerKlienta(Socket gniazdoKlienta, int id) {
        this.id = id;
        this.gniazdoKlienta = gniazdoKlienta;
        (rk = new RzutnikKoordynatow(this.id)).start();
        (ram = new RzutnikAktualizacjiMapy(this.id)).start();

        try {
            System.out.print("\n" + "Inicjalizacja połączenia " + this.id + "...");
            this.wejscie = new Scanner(gniazdoKlienta.getInputStream()); // otrzymywać od klienta
            this.wyjscie = new PrintStream(gniazdoKlienta.getOutputStream(), true); // do wysyłania do klienta
        } catch (IOException e) {
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");

        listaWyjscKlientow.add(wyjscie);
        Serwer.gracz[id].zalogowany = true;
        Serwer.gracz[id].zywy = true;
        wyslijPoczatkoweUstawienia(); // wysyła jedną linię

        // powiadamia klientów już zalogowanych
        for (PrintStream wyjscieKlienta: listaWyjscKlientow)
            if (wyjscieKlienta != this.wyjscie)
                wyjscieKlienta.println(id + " graczDolaczyl");
    }

    public void run() {
        while (wejscie.hasNextLine()) { // połączenie nawiązane z klientem this.id
            String str[] = wejscie.nextLine().split(" ");

            if (str[0].equals("klawiszWcisniety") && Serwer.gracz[id].zywy) {
                rk.klawiszWcisniety(Integer.parseInt(str[1]));
            }
            else if (str[0].equals("klawiszZwolniony") && Serwer.gracz[id].zywy) {
                rk.klawiszZwolniony(Integer.parseInt(str[1]));
            }
            else if (str[0].equals("wcisnietoSpacje") && Serwer.gracz[id].liczbaBomb >= 1) {
                Serwer.gracz[id].liczbaBomb--;
                ram.ustawBombeZasadzona(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
            }
        }
        klientRozlaczony();
    }

    void wyslijPoczatkoweUstawienia() {
        wyjscie.print(id);
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                wyjscie.print(" " + Serwer.mapa[i][j].obraz);

        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            wyjscie.print(" " + Serwer.gracz[i].zywy);

        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            wyjscie.print(" " + Serwer.gracz[i].x + " " + Serwer.gracz[i].y);
        wyjscie.print("\n");
    }

    void klientRozlaczony() {
        listaWyjscKlientow.remove(wyjscie);
        Serwer.gracz[id].zalogowany = false;
        try {
            System.out.print("Zakończenie połączenia " + this.id + "...");
            wejscie.close();
            wyjscie.close();
            gniazdoKlienta.close();
        } catch (IOException e) {
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");
    }
}

