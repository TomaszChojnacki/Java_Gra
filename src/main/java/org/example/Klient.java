package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Klient {
   private Socket socket = null;
   static PrintStream out = null;
   static Scanner in = null;
   static int id;

   final static int rateStatusUpdate = 115;
   static Wspolrzedne mapa[][] = new Wspolrzedne[Stale.LINIE][Stale.KOLUMNY];

   static Wspolrzedne punktStartowy[] = new Wspolrzedne[Stale.ILOSC_GRACZY];
   static boolean zywy[] = new boolean[Stale.ILOSC_GRACZY];

   Klient(String host, int port) {
      try {
         System.out.print("Nawiazywanie polaczenia z serwerem...");
         this.socket = new Socket(host, port);
         out = new PrintStream(socket.getOutputStream(), true);  //do wysyłania do serwera
         in = new Scanner(socket.getInputStream()); //do odbierania od serwera
      }
      catch (UnknownHostException e) {
         System.out.println(" blad: " + e + "\n");
         System.exit(1);
      }
      catch (IOException e) {
         System.out.println(" blad: " + e + "\n");
         System.exit(1);
      }

      System.out.print(" ok\n");

      receiveInitialSettings();
      new Odbiornik().start();
   }

   void receiveInitialSettings() {
      id = in.nextInt();

      //mapa
      for (int i = 0; i < Stale.LINIE; i++)
         for (int j = 0; j < Stale.KOLUMNY; j++)
            mapa[i][j] = new Wspolrzedne(Stale.ROZMIAR_SPRITA_MAPY * j, Stale.ROZMIAR_SPRITA_MAPY * i, in.next());

      //początkowy stan (żywy lub martwy) wszystkich graczy
      for (int i = 0; i < Stale.ILOSC_GRACZY; i++)
         Klient.zywy[i] = in.nextBoolean();

      //początkowe koordynaty wszystkich graczy
      for (int i = 0; i < Stale.ILOSC_GRACZY; i++)
         Klient.punktStartowy[i] = new Wspolrzedne(in.nextInt(), in.nextInt());
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

      add(new Gra(Stale.KOLUMNY*Stale.ROZMIAR_SPRITA_MAPY, Stale.LINIE*Stale.ROZMIAR_SPRITA_MAPY));
      setTitle("bomberman");
      pack();
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Nadawca());
   }
}
