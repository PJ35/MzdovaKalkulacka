import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MzdaFrame {
    private JPanel mainPanel;
    private JTextField hm;
    private JCheckBox pp;
    private JTextField dns;
    private JButton vypocitat;

    final int SLEVA_NA_POPLATNIKA = 2570;
    final int SLVA_NA_PRVNI_DITE = 1267;
    final int SLEVA_NA_DRUHE_DITE = 1860;
    final int SLEVA_NA_DALSI_DITE = 2320;

    public MzdaFrame() {
        vypocitat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hrubaMzda = Integer.parseInt(hm.getText());
                int slevaPoplatnika = pp.isSelected() ? SLEVA_NA_POPLATNIKA : 0;
                int detiNaSlevu = Integer.parseInt(dns.getText());
                int slevaNaDeti = 0;
                if (detiNaSlevu>0) {
                    slevaNaDeti += SLVA_NA_PRVNI_DITE;
                    if (detiNaSlevu > 1) {
                        slevaNaDeti += SLEVA_NA_DRUHE_DITE;
                        int pocitadlo = 2;
                        while (detiNaSlevu > pocitadlo) {
                            slevaNaDeti += SLEVA_NA_DALSI_DITE;
                            pocitadlo++;
                        }
                    }
                }
                int danZPrijmu = hrubaMzda * 15 / 100 - slevaPoplatnika - slevaNaDeti;
                int socPojisteni = hrubaMzda * 71 / 1000;
                int zdravPojisteni = hrubaMzda * 45 / 1000;
                int cistaMzda = hrubaMzda - danZPrijmu - socPojisteni - zdravPojisteni;
                JOptionPane.showMessageDialog(null, "Čistá mzda: " + cistaMzda + " Kč\n" +
                        "Daň z příjmu: " + danZPrijmu + " Kč\n" +
                        "Sociální pojištění: " + socPojisteni + " Kč\n" +
                        "Zdravotní pojištění: " + zdravPojisteni + " Kč");
            }
        });
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
