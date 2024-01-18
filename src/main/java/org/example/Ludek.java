package org.example;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class Ludek {
    final static String koloryPostaci[] = {
            "bialy",
            "zolty",
            "zielony",
            "czarny"
    };

    final static Hashtable<String, Image> ht = new Hashtable<String, Image>();
    //nie jest w kolejności spritesheet
    final static String kluczoweSlowaMapy[] = {
            "blok",
            "blok-w-ogniu-1", "blok-w-ogniu-2", "blok-w-ogniu-3", "blok-w-ogniu-4", "blok-w-ogniu-5", "blok-w-ogniu-6",
            "bomba1", "bomba2", "bomba3",
            "bomba-ikona-1", "bomba-ikona-2",
            "centrum-wybuchu-1", "centrum-wybuchu-2", "centrum-wybuchu-3", "centrum-wybuchu-4", "centrum-wybuchu-5",
            "czerwonabomba1", "czerwonabomba2", "czerwonabomba3",
            "czerwona-bomba-ikona-1", "czerwona-bomba-ikona-2",
            "dol-wybuchu-1", "dol-wybuchu-2", "dol-wybuchu-3", "dol-wybuchu-4", "dol-wybuchu-5",
            "gora-wybuchu-1", "gora-wybuchu-2", "gora-wybuchu-3", "gora-wybuchu-4", "gora-wybuchu-5",
            "lewo-wybuchu-1", "lewo-wybuchu-2", "lewo-wybuchu-3", "lewo-wybuchu-4", "lewo-wybuchu-5",
            "niszczenie-przedmiotu-1", "niszczenie-przedmiotu-2", "niszczenie-przedmiotu-3", "niszczenie-przedmiotu-4", "niszczenie-przedmiotu-5", "niszczenie-przedmiotu-6", "niszczenie-przedmiotu-7",
            "podloga-1", "podloga-2",
            "prawo-wybuchu-1", "prawo-wybuchu-2", "prawo-wybuchu-3", "prawo-wybuchu-4", "prawo-wybuchu-5",
            "sciana-dol-lewo", "sciana-dol-prawo", "sciana-gora-lewo", "sciana-gora-prawo","sciana-srodek",
            "srodek-horyzontalny-wybuchu-1", "srodek-horyzontalny-wybuchu-2", "srodek-horyzontalny-wybuchu-3", "srodek-horyzontalny-wybuchu-4", "srodek-horyzontalny-wybuchu-5",
            "srodek-pionowy-wybuchu-1", "srodek-pionowy-wybuchu-2", "srodek-pionowy-wybuchu-3", "srodek-pionowy-wybuchu-4", "srodek-pionowy-wybuchu-5",
            "tlo"
    };
    //już jest w kolejności spritesheet do użycia autoCropAndRename.cpp
    static final String kluczoweSlowaPostaci[] = {
            "czekaj-0", "czekaj-1", "czekaj-2", "czekaj-3", "czekaj-4",
            "dol-0", "dol-1", "dol-2", "dol-3", "dol-4", "dol-5", "dol-6", "dol-7",
            "gora-0", "gora-1", "gora-2", "gora-3", "gora-4", "gora-5", "gora-6", "gora-7",
            "lewo-0", "lewo-1", "lewo-2", "lewo-3", "lewo-4", "lewo-5", "lewo-6", "lewo-7",
            "martwy-0", "martwy-1", "martwy-2", "martwy-3", "martwy-4",
            "prawo-0", "prawo-1", "prawo-2", "prawo-3", "prawo-4", "prawo-5", "prawo-6", "prawo-7",
            "uhu-0", "uhu-1", "uhu-2", "uhu-3",
            "wygrana-0", "wygrana-1", "wygrana-2", "wygrana-3", "wygrana-4"
    };

    final static Hashtable<String, Integer> maxPetlaStatusu = new Hashtable<String, Integer>();
    static void ustawMaxPetlaStatusu() {
        maxPetlaStatusu.put("czekaj", 5);
        maxPetlaStatusu.put("dol", 8);
        maxPetlaStatusu.put("gora", 8);
        maxPetlaStatusu.put("lewo", 8);
        maxPetlaStatusu.put("martwy", 5);
        maxPetlaStatusu.put("prawo", 8);
        maxPetlaStatusu.put("uhu", 4);
        maxPetlaStatusu.put("wygrana", 5);
    }

    static void zaladujObrazy() {
        try {
            System.out.print("Ladowanie obrazow...");
            for (String kluczoweSlowo : kluczoweSlowaMapy)
                ht.put(kluczoweSlowo, ImageIO.read(new File("src/main/java/org/example/images/map/basic/"+kluczoweSlowo+".png")));

            for (String kolor : koloryPostaci)
                for (String kluczoweSlowo : kluczoweSlowaPostaci)
                    ht.put(kolor+"/"+kluczoweSlowo, ImageIO.read(new File("src/main/java/org/example/images/person/"+kolor+"/"+kluczoweSlowo+".png")));
        } catch (IOException e) {
            System.out.print(" blad!\n");
            System.exit(1);
        }
        System.out.print(" ok!\n");
    }
}

