package org.example;

//odbiera informacje od wszystkich klientów
public class Odbiornik extends Thread {
   Gracz p;

   Gracz odKtoregoGraczaJest(int id) {
      if (id == Klient.id)
         return Gra.ty;
      else if (id == (Klient.id+1)%Stale.ILOSC_GRACZY)
         return Gra.przeciwnik1;
      else if (id == (Klient.id+2)%Stale.ILOSC_GRACZY)
         return Gra.przeciwnik2;
      else if (id == (Klient.id+3)%Stale.ILOSC_GRACZY)
         return Gra.przeciwnik3;
      return null;
   }

   public void run() {
      String str;
      while (Klient.in.hasNextLine()) {
         this.p = odKtoregoGraczaJest(Klient.in.nextInt()); //id klienta
         str = Klient.in.next();

         if (str.equals("aktualizacjaMapy")) { //p null
            Gra.ustawSpriteMapy(Klient.in.next(), Klient.in.nextInt(), Klient.in.nextInt());
            Gra.ty.panel.repaint();
         }
         else if (str.equals("nowaWspolrzedna")) {
            p.x = Klient.in.nextInt();
            p.y = Klient.in.nextInt();
            Gra.ty.panel.repaint();
         }
         else if (str.equals("nowyStatus")) {
            p.sc.setLoopStatus(Klient.in.next());
         }
         else if (str.equals("zatrzymajAktualizacjeStatusu")) {
            p.sc.stopLoopStatus();
         }
         else if (str.equals("graczDolaczyl")) {
            p.zywy = true;
         }
      }
      Klient.in.close();
   }
}
