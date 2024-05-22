import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MzdaFrame {
    private JPanel mainPanel;
    private JTextField mzda;
    private JCheckBox prohlaseni;
    private JTextField deti;
    private JButton vypocitat;
    private JCheckBox auto;
    private JRadioButton emisni;
    private JRadioButton nizkoemisni;
    private JRadioButton bezemisni;
    private JTextField cena;

    static int slevaNaPoplatnika;
    static int slevaNaPrvniDite;
    static int slevaNaDruheDite;
    static int slevaNaDalsiDite;
    static int sazba_emisni_auto;
    static int sazba_nizkoemisni_auto;
    static int sazba_bezemisni_auto;
    static int sazba_soc_pojisteni;
    static int sazba_zdrav_pojisteni;
    static int sazba_dan_z_prijmu;
    final int TISICINA_PROCENTA = 10000;

    public MzdaFrame() {
        deti.setEnabled(false);
        prohlaseni.addActionListener(e -> deti.setEnabled(prohlaseni.isSelected()));
        cena.setEnabled(false);
        emisni.setEnabled(false);
        nizkoemisni.setEnabled(false);
        bezemisni.setEnabled(false);
        auto.addActionListener(e -> {
            cena.setEnabled(auto.isSelected());
            emisni.setEnabled(auto.isSelected());
            nizkoemisni.setEnabled(auto.isSelected());
            bezemisni.setEnabled(auto.isSelected());
        });
        vypocitat.addActionListener(e -> {
            try {
                int hrubaMzda = Integer.parseInt(mzda.getText());
                int cenaAuta = auto.isSelected() ? Integer.parseInt(cena.getText()) : 0;
                int zakladOdvodu = getZakladOdvodu(hrubaMzda, cenaAuta);
                int slevaPoplatnika = prohlaseni.isSelected() ? slevaNaPoplatnika : 0;
                int detiNaSlevu = prohlaseni.isSelected() ? Integer.parseInt(deti.getText()) : 0;
                int danZPrijmu = getDanZPrijmu(detiNaSlevu, zakladOdvodu, slevaPoplatnika);
                int socPojisteni = zakladOdvodu * sazba_soc_pojisteni / TISICINA_PROCENTA;
                int zdravPojisteni = zakladOdvodu * sazba_zdrav_pojisteni / TISICINA_PROCENTA;
                int cistaMzda = hrubaMzda - danZPrijmu - socPojisteni - zdravPojisteni;
                JOptionPane.showMessageDialog(null, "Čistá mzda: " + cistaMzda + " Kč\n\n" +
                        "Daň z příjmu: " + danZPrijmu + " Kč\n" +
                        "Sociální pojištění: " + socPojisteni + " Kč\n" +
                        "Zdravotní pojištění: " + zdravPojisteni + " Kč");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Zadejte číselné hodnoty\n" +
                        ex.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private int getZakladOdvodu(int hrubaMzda, int cenaAuta) {
        int zakladOdvodu = hrubaMzda;
        if (auto.isSelected()) {
            if (emisni.isSelected()) {
                zakladOdvodu += cenaAuta * sazba_emisni_auto / TISICINA_PROCENTA;
            } else if (nizkoemisni.isSelected()) {
                zakladOdvodu += cenaAuta * sazba_nizkoemisni_auto / TISICINA_PROCENTA;
            } else if (bezemisni.isSelected()) {
                zakladOdvodu += cenaAuta * sazba_bezemisni_auto / TISICINA_PROCENTA;
            } else {
                throw new IllegalArgumentException("Vyberte typ automobilu");
            }
        }
        return zakladOdvodu;
    }

    private int getDanZPrijmu(int detiNaSlevu, int zakladOdvodu, int slevaPoplatnika) {
        int slevaNaDeti = 0;
        if (detiNaSlevu > 0) {
            slevaNaDeti += slevaNaPrvniDite;
            if (detiNaSlevu > 1) {
                slevaNaDeti += slevaNaDruheDite;
                int pocitadlo = 2;
                while (detiNaSlevu > pocitadlo) {
                    slevaNaDeti += slevaNaDalsiDite;
                    pocitadlo++;
                }
            }
        }
        return zakladOdvodu * sazba_dan_z_prijmu / TISICINA_PROCENTA - slevaPoplatnika - slevaNaDeti;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mzdová kalkulačka");
        frame.setContentPane(new MzdaFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        nactiSlevy();
        nactiSazby();
    }

    private static void nactiSlevy() {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("slevy.txt")))) {
            String radek = sc.nextLine();
            String[] slevy = radek.split(";");
            slevaNaPoplatnika = Integer.parseInt(slevy[0]);
            slevaNaPrvniDite = Integer.parseInt(slevy[1]);
            slevaNaDruheDite = Integer.parseInt(slevy[2]);
            slevaNaDalsiDite = Integer.parseInt(slevy[3]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Nepodařilo se načíst slevy ze souboru slevy.txt");
        }
    }

    private static void nactiSazby() {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("sazby.txt")))) {
            String radek = sc.nextLine();
            String[] sazby = radek.split(";");
            sazba_emisni_auto = Integer.parseInt(sazby[0]);
            sazba_nizkoemisni_auto = Integer.parseInt(sazby[1]);
            sazba_bezemisni_auto = Integer.parseInt(sazby[2]);
            sazba_soc_pojisteni = Integer.parseInt(sazby[3]);
            sazba_zdrav_pojisteni = Integer.parseInt(sazby[4]);
            sazba_dan_z_prijmu = Integer.parseInt(sazby[5]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Nepodařilo se načíst sazby ze souboru sazby.txt");
        }
    }
}
