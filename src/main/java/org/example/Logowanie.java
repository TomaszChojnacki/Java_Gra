package org.example;

import org.example.security.Authorization;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.security.Authorization.isValidLogin;


public class Logowanie extends JFrame {

    public Logowanie() {
        setTitle("Logowanie");
        setSize(400, 300);
        setResizable(false);    // Zablokowanie zmiany rozmiaru okna
        setLocationRelativeTo(null);    // Ustawianie okna na środku ekranu
        initComponents();   // Inicjalizacja komponentów interfejsu
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // Ustawienie domyślnej operacji zamknięcia
    }


    JPanel panel = new JPanel();    // Utworzenie panelu do umieszczenia komponentów


    public void initComponents() {
        // Inicjalizacja komponentów interfejsu użytkownika
        JButton PrzyciskZamknij = new JButton("Zamknij");
        JButton PrzyciskOK = new JButton("OK");
        JLabel Login = new JLabel("Login: ");
        JLabel Haslo = new JLabel("Hasło: ");
        JTextField LoginUzytkownika = new JTextField(6);// Pole tekstowe na login
        JPasswordField HasloUzytkownika = new JPasswordField(6);// Pole na hasło
        // Ustawianie rozmiarów i pozycji elementów
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
        panel.setLayout(null);  // Ustawienie niestandardowego układu komponentów
        // Dodanie komponentów do panelu
        panel.add(PrzyciskZamknij);
        panel.add(PrzyciskOK);
        panel.add(Login);
        panel.add(Haslo);
        panel.add(LoginUzytkownika);
        panel.add(HasloUzytkownika);
        // Ustawienie podpowiedzi (tooltip) dla komponentów
        PrzyciskZamknij.setToolTipText("Zamknij Program.");
        PrzyciskOK.setToolTipText("Zaloguj się.");
        LoginUzytkownika.setToolTipText("Podaj swój login.");
        HasloUzytkownika.setToolTipText("Podaj swoje hasło.");
        this.getContentPane().add(panel);   // Dodanie panelu do głównego kontenera okna
        // Dodanie słuchaczy zdarzeń do przycisków
        PrzyciskZamknij.addActionListener(new ButtonZamknij());
        PrzyciskOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String user = LoginUzytkownika.getText();
                String pass = HasloUzytkownika.getText();

                // Sprawdzenie poprawności logowania
                if (isValidLogin(user, pass)) {
                    // Logowanie zakończone sukcesem
                    System.out.print("zalogowano");
                    new Klient("127.0.0.1", 1234);
                    new Okno();
                } else {
                    // Logowanie nieudane
                    System.out.print("Nie znaleziono takiego  usera w bazie danych");
                }
            }

        });

    }

    // Klasa obsługująca zdarzenie naciśnięcia przycisku "Zamknij"
    private class ButtonZamknij implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);// Zamknięcie aplikacji
        }
    }
}
