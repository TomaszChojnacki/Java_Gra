package org.example;

// Klasa Odbiornik kożysta z Thread i
// odpowiada za odbieranie informacji od wszystkich klientów
public class Odbiornik extends Thread {
    Gracz gracz;

    // Metoda określa, który obiekt Gracz odpowiada danemu identyfikatorowi klienta
    Gracz odKtoregoGraczaJest(int id) {
        if (id == Klient.id)
            return Gra.ty;  // Jeśli ID zgadza się z ID klienta, zwraca aktualnego gracza
        else if (id == (Klient.id + 1) % Stale.ILU_GRACZY)
            return Gra.przeciwnik1; // Zwraca pierwszego
        else if (id == (Klient.id + 2) % Stale.ILU_GRACZY)
            return Gra.przeciwnik2;
        else if (id == (Klient.id + 3) % Stale.ILU_GRACZY)
            return Gra.przeciwnik3;
        return null;
    }

    public void run() {
        String str;
        while (Klient.wejscie.hasNextLine()) {
            this.gracz = odKtoregoGraczaJest(Klient.wejscie.nextInt()); // Odczytanie identyfikatora klienta
            str = Klient.wejscie.next(); // Odczytanie rodzaju komunikatu

            // Przetwarzanie różnych rodzajów komunikatów
            if (str.equals("aktualizacjaMapy")) { // Aktualizacja stanu mapy w grze
                Gra.ustawSpriteMape(Klient.wejscie.next(), Klient.wejscie.nextInt(), Klient.wejscie.nextInt());
                Gra.ty.panel.repaint(); // Odświeżenie panelu gry
            } else if (str.equals("noweKoordynaty")) {

                // Ustawienie nowych współrzędnych dla gracza
                gracz.x = Klient.wejscie.nextInt();
                gracz.y = Klient.wejscie.nextInt();
                Gra.ty.panel.repaint(); // Odświeżenie panelu gry
            } else if (str.equals("nowyStatus")) {
                // Ustawienie nowego statusu dla gracza
                gracz.zs.ustawPetleStatusu(Klient.wejscie.next());

            } else if (str.equals("zatrzymajAktualizacjeStatusu")) {
                gracz.zs.zatrzymajPetleStatusu(); // Zatrzymanie aktualizacji statusu gracza

            } else if (str.equals("graczDolaczyl")) {
                gracz.zywy = true;  // Oznaczenie gracza jako żywego
            }
        }
        Klient.wejscie.close(); // Zamknięcie strumienia wejściowego
    }
}
