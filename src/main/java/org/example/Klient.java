package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Klient {
    private Socket gniazdo = null;  // Gniazdo sieciowe dla połączenia z klientem
    static PrintStream wyjscie = null;  // Statyczny strumień wyjściowy, umożliwiający wysyłanie danych do serwera
    static Scanner wejscie = null;  // Statyczny strumień wejściowy, służący do odbierania danych od serwera
    static int id;      // identyfikator klienta

    final static int szybkoscAktualizacjiStatusu = 115; // Częstotliwość aktualizacji statusu w milisekundach
    static Koordynaty mapa[][] = new Koordynaty[Stale.WIERSZE][Stale.KOLUMNY]; // Tablica reprezentująca mapę

    static Koordynaty spawn[] = new Koordynaty[Stale.ILU_GRACZY]; // Tablica zawierająca punkty startowe graczy
    static boolean zywy[] = new boolean[Stale.ILU_GRACZY]; // Tablica przechowująca status życia graczy

    Klient(String host, int port) {
        try {
            System.out.print("Nawiązywanie połączenia z serwerem...");
            this.gniazdo = new Socket(host, port); // Nawiązanie połączenia z serwerem
            wyjscie = new PrintStream(gniazdo.getOutputStream(), true);  // Strumień wyjściowy do wysyłania danych do serwera
            wejscie = new Scanner(gniazdo.getInputStream()); // Strumień wejściowy do odbierania danych od serwera
        } catch (UnknownHostException e) {
            System.out.println(" błąd: " + e + "\n");// Obsługa wyjątku nieznanego hosta
            System.exit(1);
        } catch (IOException e) {
            System.out.println(" błąd: " + e + "\n"); // Obsługa innych wyjątków wejścia/wyjścia "wątki"
            System.exit(1);
        }
        System.out.print(" ok\n");

        odbierzPoczatkoweUstawienia(); // Odbiór początkowych ustawień gry
        new Odbiornik().start(); // Start wątku odbierającego dane
    }

    void odbierzPoczatkoweUstawienia() {
        id = wejscie.nextInt(); // Odbiór identyfikatora gracza

        // Odbiór i ustawienie mapy gry
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                mapa[i][j] = new Koordynaty(Stale.ROZMIAR_SPRITE_MAPY * j, Stale.ROZMIAR_SPRITE_MAPY * i, wejscie.next());

        // Odbiór początkowego stanu życia wszystkich graczy
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            Klient.zywy[i] = wejscie.nextBoolean();

        // Odbiór początkowych współrzędnych wszystkich graczy
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            Klient.spawn[i] = new Koordynaty(wejscie.nextInt(), wejscie.nextInt());
    }
}




