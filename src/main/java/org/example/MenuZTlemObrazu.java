package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuZTlemObrazu extends JFrame {

    public MenuZTlemObrazu() {
        // Ustawienia
        setTitle("Bomberman");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ustawienie tła jako obrazu
        setContentPane(new JLabel(new ImageIcon("png\\zdj.png")));

        // Układ z GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Dodanie przycisku "Graj"
        JButton grajButton = utworzPrzycisk("Graj");
        grajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obsługa zdarzenia dla przycisku "Graj"
                JOptionPane.showMessageDialog(MenuZTlemObrazu.this, "Rozpocznij grę!");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets( 0, -350, 0, 0);  // Ustawienie przycisku "Graj" na 1/4 wysokości ekranu
        add(grajButton, gbc);

        JButton informacjeButton = utworzPrzycisk("Informacje");
        informacjeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obsługa zdarzenia dla przycisku "Informacje"
                JOptionPane.showMessageDialog(MenuZTlemObrazu.this, "Informacje o grze.");
            }
        });
        gbc.gridy++;
        add(informacjeButton, gbc);

        JButton ustawieniaButton = utworzPrzycisk("Ustawienia");
        ustawieniaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obsługa zdarzenia dla przycisku "Ustawienia"
                JOptionPane.showMessageDialog(MenuZTlemObrazu.this, "Ustawienia gry.");
            }
        });
        gbc.gridy++;
        add(ustawieniaButton, gbc);

        JButton wyjscieButton = utworzPrzycisk("Wyjście");
        wyjscieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });
        gbc.gridy++;
        add(wyjscieButton, gbc);
    }
/// trzeba zmienic na maven


    private JButton utworzPrzycisk(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(230, 50));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuZTlemObrazu menu = new MenuZTlemObrazu();
                menu.setVisible(true);
            }
        });
    }
}

//// zmiana
