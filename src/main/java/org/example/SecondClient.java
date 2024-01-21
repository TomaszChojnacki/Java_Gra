package org.example;

import java.awt.*;

public class SecondClient {
    public static void main(String[] args) {
        Player player = new Player(1, "user", "pass", 1);

        // SqlPersistenceManager manager = new SqlPersistenceManager();
        // manager.insert(player);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Logowanie EkranLogowania = new Logowanie();
                EkranLogowania.setVisible(true);
                Toolkit t = Toolkit.getDefaultToolkit();
                Dimension d = t.getScreenSize();
                EkranLogowania.setLocation((d.width / 4), (d.height / 4));

                // EkranLogowania.setBounds(d.width/4, d.height/4,500 ,300);
            }
        });
    }
}
