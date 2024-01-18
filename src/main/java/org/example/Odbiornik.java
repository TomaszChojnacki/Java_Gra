package org.example;

// odbiera informacje od wszystkich klientów
public class Odbiornik extends Thread {
    Gracz gracz;

    Gracz odKtoregoGraczaJest(int id) {
        if (id == Klient.id)
            return Gra.ty;
        else if (id == (Klient.id+1)%Stale.ILU_GRACZY)
            return Gra.przeciwnik1;
        else if (id == (Klient.id+2)%Stale.ILU_GRACZY)
            return Gra.przeciwnik2;
        else if (id == (Klient.id+3)%Stale.ILU_GRACZY)
            return Gra.przeciwnik3;
        return null;
    }

    public void run() {
        String str;
        while (Klient.wejscie.hasNextLine()) {
            this.gracz = odKtoregoGraczaJest(Klient.wejscie.nextInt()); // id klienta
            str = Klient.wejscie.next();

            if (str.equals("aktualizacjaMapy")) { // gracz null
                Gra.ustawSpriteMape(Klient.wejscie.next(), Klient.wejscie.nextInt(), Klient.wejscie.nextInt());
                Gra.ty.panel.repaint();
            }
            else if (str.equals("noweKoordynaty")) {
                gracz.x = Klient.wejscie.nextInt();
                gracz.y = Klient.wejscie.nextInt();
                Gra.ty.panel.repaint();
            }
            else if (str.equals("nowyStatus")) {
                gracz.zs.ustawPetleStatusu(Klient.wejscie.next());
            }
            else if (str.equals("zatrzymajAktualizacjeStatusu")) {
                gracz.zs.zatrzymajPetleStatusu();
            }
            else if (str.equals("graczDolaczyl")) {
                gracz.zywy = true;
            }
        }
        Klient.wejscie.close();
    }
}
