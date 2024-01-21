package org.example;

import org.example.security.Authorization;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.security.Authorization.isValidLogin;


public class Logowanie extends JFrame {

    public Logowanie() {

        setTitle("MyStats - Ekran logowania");
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    JPanel panel = new JPanel();


    public void initComponents() {

        JButton PrzyciskZamknij = new JButton("Zamknij");
        JButton PrzyciskOK = new JButton("OK");
        JLabel Login = new JLabel("Login: ");
        JLabel Haslo = new JLabel("Hasło: ");
        JTextField LoginUzytkownika = new JTextField(6);
        JPasswordField HasloUzytkownika = new JPasswordField(6);
        PrzyciskZamknij.setSize(90, 30);
        PrzyciskOK.setSize(90, 30);
        Login.setSize(100, 30);
        Haslo.setSize(100, 30);
        LoginUzytkownika.setSize(100, 30);
        HasloUzytkownika.setSize(100, 30);
        PrzyciskZamknij.setLocation(getWidth() - 190, getHeight() - 100);
        PrzyciskOK.setLocation(getWidth() - 310, getHeight() - 100);
        Login.setLocation(getWidth() - 300, getHeight() - 220);
        Haslo.setLocation(getWidth() - 300, getHeight() - 180);
        LoginUzytkownika.setLocation(getWidth() - 250, getHeight() - 220);
        HasloUzytkownika.setLocation(getWidth() - 250, getHeight() - 180);
        panel.setLayout(null);
        panel.add(PrzyciskZamknij);
        panel.add(PrzyciskOK);
        panel.add(Login);
        panel.add(Haslo);
        panel.add(LoginUzytkownika);
        panel.add(HasloUzytkownika);
        PrzyciskZamknij.setToolTipText("Zamknij Program.");
        PrzyciskOK.setToolTipText("Zaloguj się.");
        LoginUzytkownika.setToolTipText("Podaj swój login.");
        HasloUzytkownika.setToolTipText("Podaj swoje hasło.");
        this.getContentPane().add(panel);
        PrzyciskZamknij.addActionListener(new ButtonZamknij());
        PrzyciskOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String user = LoginUzytkownika.getText();
                String pass = HasloUzytkownika.getText();

                if (isValidLogin(user, pass)) {
                    System.out.print("zalogowano");
                    new Klient("127.0.0.1", 8383);
                    new Okno();
                } else {
                    System.out.print("Nie znaleziono takiego  usera w bazie danych");
                }
            }

        });

    }

    //akcja przy przycisku zamknij
    private class ButtonZamknij implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
