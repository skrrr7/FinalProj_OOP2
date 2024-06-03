//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class SettingsMenu extends JFrame {
//    private JCheckBox audioCheckBox;
//    private JCheckBox musicCheckBox;
//    private Settings settings;
//
//    public SettingsMenu(Settings settings) {
//        this.settings = settings;
//        initUI();
//    }
//
//    private void initUI() {
//        setTitle("Settings");
//        setSize(300, 200);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(3, 1));
//
//        audioCheckBox = new JCheckBox("Enable Audio");
//        audioCheckBox.setSelected(settings.isAudioEnabled());
//        audioCheckBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                settings.setAudioEnabled(audioCheckBox.isSelected());
//            }
//        });
//
//        musicCheckBox = new JCheckBox("Enable Music");
//        musicCheckBox.setSelected(settings.isMusicEnabled());
//        musicCheckBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                settings.setMusicEnabled(musicCheckBox.isSelected());
//            }
//        });
//
//        panel.add(audioCheckBox);
//        panel.add(musicCheckBox);
//
//        getContentPane().add(panel, BorderLayout.CENTER);
//
//        JButton saveButton = new JButton("Save");
//        saveButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Save settings and close the menu
//                dispose();
//            }
//        });
//
//        getContentPane().add(saveButton, BorderLayout.SOUTH);
//    }
//
//    public static void main(String[] args) {
//        Settings settings = new Settings();
//        EventQueue.invokeLater(() -> {
//            SettingsMenu ex = new SettingsMenu(settings);
//            ex.setVisible(true);
//        });
//    }
//}
