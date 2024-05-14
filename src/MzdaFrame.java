import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    final int SLEVA_NA_POPLATNIKA = 2570;
    final int SLEVA_NA_PRVNI_DITE = 1267;
    final int SLEVA_NA_DRUHE_DITE = 1860;
    final int SLEVA_NA_DALSI_DITE = 2320;

    public MzdaFrame() {
        deti.setEnabled(false);
        prohlaseni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deti.setEnabled(prohlaseni.isSelected());
            }
        });
        cena.setEnabled(false);
        emisni.setEnabled(false);
        nizkoemisni.setEnabled(false);
        bezemisni.setEnabled(false);
        auto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cena.setEnabled(auto.isSelected());
                emisni.setEnabled(auto.isSelected());
                nizkoemisni.setEnabled(auto.isSelected());
                bezemisni.setEnabled(auto.isSelected());
            }
        });
        vypocitat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hrubaMzda = Integer.parseInt(mzda.getText());
                int cenaAuta = auto.isSelected() ? Integer.parseInt(cena.getText()) : 0;
                int zakladOdvodu = hrubaMzda;
                if (auto.isSelected()) {
                    if (emisni.isSelected()) {
                        zakladOdvodu += cenaAuta / 100;
                    } else if (nizkoemisni.isSelected()) {
                        zakladOdvodu += cenaAuta * 5 / 1000;
                    } else if (bezemisni.isSelected()) {
                        zakladOdvodu += cenaAuta * 25 / 10000;
                    }
                }
                int slevaPoplatnika = prohlaseni.isSelected() ? SLEVA_NA_POPLATNIKA : 0;
                int detiNaSlevu = prohlaseni.isSelected() ? Integer.parseInt(deti.getText()) : 0;
                int danZPrijmu = getDanZPrijmu(detiNaSlevu, zakladOdvodu, slevaPoplatnika);
                int socPojisteni = zakladOdvodu * 71 / 1000;
                int zdravPojisteni = zakladOdvodu * 45 / 1000;
                int cistaMzda = hrubaMzda - danZPrijmu - socPojisteni - zdravPojisteni;
                JOptionPane.showMessageDialog(null, "Čistá mzda: " + cistaMzda + " Kč\n\n" +
                        "Daň z příjmu: " + danZPrijmu + " Kč\n" +
                        "Sociální pojištění: " + socPojisteni + " Kč\n" +
                        "Zdravotní pojištění: " + zdravPojisteni + " Kč");
            }
        });
    }

    private int getDanZPrijmu(int detiNaSlevu, int zakladOdvodu, int slevaPoplatnika) {
        int slevaNaDeti = 0;
        if (detiNaSlevu >0) {
            slevaNaDeti += SLEVA_NA_PRVNI_DITE;
            if (detiNaSlevu > 1) {
                slevaNaDeti += SLEVA_NA_DRUHE_DITE;
                int pocitadlo = 2;
                while (detiNaSlevu > pocitadlo) {
                    slevaNaDeti += SLEVA_NA_DALSI_DITE;
                    pocitadlo++;
                }
            }
        }
        return zakladOdvodu * 15 / 100 - slevaPoplatnika - slevaNaDeti;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mzdová kalkulačka");
        frame.setContentPane(new MzdaFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
