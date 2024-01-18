package org.example;

import java.awt.event.*;

//nasłuchuje, kiedy okno (JFrame) jest w fokusie
public class Nadawca extends KeyAdapter {
   int ostatniWcisnietyKodKlawisza;

   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE)
         Klient.out.println("pressedSpace " + Gra.ty.x + " " + Gra.ty.y);
      else if (jestNowyKodKlawisza(e.getKeyCode()))
         Klient.out.println("keyCodePressed " + e.getKeyCode());
   }

   public void keyReleased(KeyEvent e) {
      Klient.out.println("keyCodeReleased " + e.getKeyCode());
      ostatniWcisnietyKodKlawisza = -1; //następny klawisz zawsze będzie nowy
   }

   boolean jestNowyKodKlawisza(int keyCode) {
      boolean ok = (keyCode != ostatniWcisnietyKodKlawisza) ? true : false;
      ostatniWcisnietyKodKlawisza = keyCode;
      return ok;
   }
}
