package net.listerily.NinjaAdventure.ui.dialog;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.OptionsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;

public class OptionsDialog extends AppBaseDialog {
    private final JLabel picLabel;
    private final JComboBox<String> characterCombobox;
    private final JTextField textFieldNickname;
    private String nicknameNew;
    private String characterNew;
    public OptionsDialog(App app) {
        super(app);
        setSize(1080, 960);
        setResizable(false);
        setModal(true);
        setTitle("Options");

        OptionsManager optionsManager = app.getOptionsManager();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Game Options", JLabel.CENTER);
        titleLabel.setFont(getTextFont().deriveFont(40f));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(50, 5, 50, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(titleLabel, constraints);


        JPanel characterPanel = new JPanel();
        constraints.insets = new Insets(10, 5, 10, 5);
        characterPanel.setLayout(new GridBagLayout());
        JLabel labelAddress = new JLabel("Character:", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        labelAddress.setFont(getTextFont().deriveFont(20f));
        characterPanel.add(labelAddress, constraints);
        picLabel = new JLabel("", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        setImage(optionsManager.getCharacter());
        characterPanel.add(picLabel, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        characterCombobox = new JComboBox<>();
        for (String character : optionsManager.getCharacters()) {
            characterCombobox.addItem(character);
        }
        characterCombobox.setSelectedItem(optionsManager.getCharacter());
        characterCombobox.setFont(getTextFont().deriveFont(30f));
        characterPanel.add(characterCombobox, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 5, 30, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(characterPanel, constraints);

        JPanel nicknamePanel = new JPanel();
        nicknamePanel.setLayout(new GridBagLayout());
        JLabel labelPort = new JLabel("Nickname:", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        labelPort.setFont(getTextFont().deriveFont(20f));
        nicknamePanel.add(labelPort, constraints);
        textFieldNickname = new JTextField(optionsManager.getNickname());
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        textFieldNickname.setColumns(20);
        textFieldNickname.setFont(getTextFont().deriveFont(30f));
        nicknamePanel.add(textFieldNickname, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(30, 5, 30, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(nicknamePanel, constraints);

        JPanel buttonsPanel = new JPanel();
        constraints.insets = new Insets(15, 5, 15, 5);
        buttonsPanel.setLayout(new GridBagLayout());
        JButton buttonCancel = new JButton("Cancel");
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonCancel.setFont(getTextFont().deriveFont(20f));
        buttonCancel.setBackground(new Color(52,52,52));
        buttonCancel.setForeground(Color.WHITE);
        buttonCancel.setFocusPainted(false);
        buttonCancel.setBorderPainted(false);
        buttonCancel.addActionListener(e -> onCancelClicked());
        buttonsPanel.add(buttonCancel, constraints);
        JButton buttonOK = new JButton("OK");
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        buttonOK.setFont(getTextFont().deriveFont(20f));
        buttonOK.setBackground(new Color(52,52,52));
        buttonOK.setForeground(Color.WHITE);
        buttonOK.setFocusPainted(false);
        buttonOK.setBorderPainted(false);
        buttonOK.addActionListener(e -> onOKClicked());
        buttonsPanel.add(buttonOK, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.insets = new Insets(30, 5, 30, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(buttonsPanel, constraints);

        this.nicknameNew = optionsManager.getNickname();
        this.characterNew = optionsManager.getCharacter();
        characterCombobox.addActionListener(e -> {
            characterNew = (String) characterCombobox.getSelectedItem();
            setImage(characterNew);
        });

        textFieldNickname.addActionListener(e -> nicknameNew = textFieldNickname.getText());
    }

    public void onCancelClicked() {
        setVisible(false);
    }

    public void setImage(String name) {
        try {
            picLabel.setIcon(new ImageIcon(app.getResourceManager().getCachedResources().readImage("Characters/" + name + "/Faceset.png").getScaledInstance(180, 180, 0)));
        } catch (IOException e) {
            app.getAppLogger().log(Level.SEVERE, "Unable to load character face image. Skipping.", e);
        }
    }

    public void onOKClicked() {
        nicknameNew = textFieldNickname.getText();
        if (nicknameNew.length() == 0) {
            MessageBoxDialog messageBoxDialog = new MessageBoxDialog(app, "Error!", "Invalid Nickname.");
            messageBoxDialog.setVisible(true);
        } else {
            app.getOptionsManager().setCharacter(characterNew);
            app.getOptionsManager().setNickname(nicknameNew);
            setVisible(false);
        }
    }
}
