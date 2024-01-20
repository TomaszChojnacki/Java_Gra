package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Klient {
    private Socket gniazdo = null;
    static PrintStream wyjscie = null;
    static Scanner wejscie = null;
    static int id;

    final static int szybkoscAktualizacjiStatusu = 115;
    static Koordynaty mapa[][] = new Koordynaty[Stale.WIERSZE][Stale.KOLUMNY];

    static Koordynaty spawn[] = new Koordynaty[Stale.ILU_GRACZY];
    static boolean zywy[] = new boolean[Stale.ILU_GRACZY];

    Klient(String host, int port) {
        try {
            System.out.print("Nawiązywanie połączenia z serwerem...");
            this.gniazdo = new Socket(host, port);
            wyjscie = new PrintStream(gniazdo.getOutputStream(), true);  // do wysyłania do serwera
            wejscie = new Scanner(gniazdo.getInputStream()); // do odbierania od serwera
        }
        catch (UnknownHostException e) {
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println(" błąd: " + e + "\n");
            System.exit(1);
        }
        System.out.print(" ok\n");

        odbierzPoczatkoweUstawienia();
        new Odbiornik().start();
    }

    void odbierzPoczatkoweUstawienia() {
        id = wejscie.nextInt();

        // mapa
        for (int i = 0; i < Stale.WIERSZE; i++)
            for (int j = 0; j < Stale.KOLUMNY; j++)
                mapa[i][j] = new Koordynaty(Stale.ROZMIAR_SPRITE_MAPY * j, Stale.ROZMIAR_SPRITE_MAPY * i, wejscie.next());

        // początkowy stan (żywy lub martwy) wszystkich graczy
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            Klient.zywy[i] = wejscie.nextBoolean();

        // początkowe współrzędne wszystkich graczy
        for (int i = 0; i < Stale.ILU_GRACZY; i++)
            Klient.spawn[i] = new Koordynaty(wejscie.nextInt(), wejscie.nextInt());
    }

    public static void main(String[] args) {
        new Klient("127.0.0.1", 8383);
        new Okno();
    }
}

class Okno extends JFrame {
    private static final long serialVersionUID = 1L;

    Okno() {
        Ludek.zaladujObrazy();
        Ludek.ustawMaxPetlaStatusu();

        add(new Gra(Stale.KOLUMNY*Stale.ROZMIAR_SPRITE_MAPY, Stale.WIERSZE*Stale.ROZMIAR_SPRITE_MAPY));
        setTitle("bomberman");
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addKeyListener(new Nadawca());
    }
}


