package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// dla każdego klienta, który łączy się z serwerem, tworzony jest nowy wątek do jego obsługi
class MenedzerKlientow extends Thread {
   static List<PrintStream> listaWyjscKlientow = new ArrayList<PrintStream>();

   static void wyslijDoWszystkichKlientow(String liniaWyjscia) {
      for (PrintStream wyjscieKlienta : listaWyjscKlientow)
         wyjscieKlienta.println(liniaWyjscia);
   }

   private Socket socket = null;
   private Scanner in = null;
   private PrintStream out = null;
   private int id;

   WyrzutnikWspolrzednych ct;
   RzucanieAktualizacjiMapy mt;

   MenedzerKlientow(Socket gniazdoKlienta, int id) {
      this.id = id;
      this.socket = gniazdoKlienta;
      (ct = new WyrzutnikWspolrzednych(this.id)).start();
      (mt = new RzucanieAktualizacjiMapy(this.id)).start();

      try {
         System.out.print("Rozpoczynanie polaczenia z graczem " + this.id + "...");
         this.in = new Scanner(gniazdoKlienta.getInputStream()); // do odbierania od klienta
         this.out = new PrintStream(gniazdoKlienta.getOutputStream(), true); // do wysyłania do klienta
      } catch (IOException e) {
         System.out.println(" blad: " + e + "\n");
         System.exit(1);
      }
      System.out.print(" ok\n");

      listaWyjscKlientow.add(out);
      Serwer.gracz[id].zalogowany = true;
      Serwer.gracz[id].zywy = true;
      wyslijPoczatkoweUstawienia(); // wysyła pojedynczy ciąg znaków

      //powiadamia zalogowanych już klientów
      for (PrintStream wyjscieKlienta: listaWyjscKlientow)
         if (wyjscieKlienta != this.out)
            wyjscieKlienta.println(id + " playerJoined");
   }

   public void run() {
      while (in.hasNextLine()) { // połączenie nawiązane z klientem this.id
         String str[] = in.nextLine().split(" ");

         if (str[0].equals("keyCodePressed") && Serwer.gracz[id].zywy) {
            ct.wcisnietoKlawisz(Integer.parseInt(str[1]));
         }
         else if (str[0].equals("keyCodeReleased") && Serwer.gracz[id].zywy) {
            ct.zwolnionoKlawisz(Integer.parseInt(str[1]));
         }
         else if (str[0].equals("pressedSpace") && Serwer.gracz[id].iloscBomb >= 1) {
            Serwer.gracz[id].iloscBomb--;
            mt.setBombaZasadzona(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
         }
      }
      klientRozlaczony();
   }

   void wyslijPoczatkoweUstawienia() {
      out.print(id);
      for (int i = 0; i < Stale.LINIE; i++)
         for (int j = 0; j < Stale.KOLUMNY; j++)
            out.print(" " + Serwer.mapa[i][j].img);

      for (int i = 0; i < Stale.ILOSC_GRACZY; i++)
         out.print(" " + Serwer.gracz[i].zywy);

      for (int i = 0; i < Stale.ILOSC_GRACZY; i++)
         out.print(" " + Serwer.gracz[i].x + " " + Serwer.gracz[i].y);
      out.print("\n");
   }

   void klientRozlaczony() {
      listaWyjscKlientow.remove(out);
      Serwer.gracz[id].zalogowany = false;
      try {
         System.out.print("Zamykanie polaczenia z graczem " + this.id + "...");
         in.close();
         out.close();
         socket.close();
      } catch (IOException e) {
         System.out.println(" blad: " + e + "\n");
         System.exit(1);
      }
      System.out.print(" ok\n");
   }
}
