package net.listerily.NinjaAdventure.ui.dialog;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.ui.frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class JoinDialog extends AppBaseDialog {
    private static final String ADDRESS_DEFAULT = "localhost";
    private static final String PORT_DEFAULT = "2022";
    private final JTextField textFieldPort;
    private final JTextField textFieldAddress;
    public JoinDialog(App app) {
        super(app);
        setSize(720, 480);
        setResizable(false);
        setModal(true);
        setTitle("Join a game");
        setLayout(new GridLayout(4, 1));

        JLabel titleLabel = new JLabel("Join Others' World", JLabel.CENTER);
        titleLabel.setFont(getTextFont().deriveFont(40f));
        this.add(titleLabel);

        GridBagConstraints constraints = new GridBagConstraints();

        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new GridBagLayout());
        JLabel labelAddress = new JLabel("Address:");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        labelAddress.setFont(getTextFont().deriveFont(20f));
        addressPanel.add(labelAddress, constraints);
        textFieldAddress = new JTextField(ADDRESS_DEFAULT);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        textFieldAddress.setColumns(20);
        textFieldAddress.setFont(getTextFont().deriveFont(30f));
        addressPanel.add(textFieldAddress, constraints);
        this.add(addressPanel);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new GridBagLayout());
        JLabel labelPort = new JLabel("Port:");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        labelPort.setFont(getTextFont().deriveFont(20f));
        portPanel.add(labelPort, constraints);
        textFieldPort = new JTextField(PORT_DEFAULT);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        textFieldPort.setColumns(20);
        textFieldPort.setFont(getTextFont().deriveFont(30f));
        portPanel.add(textFieldPort, constraints);
        this.add(portPanel);

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
        JButton buttonJoin = new JButton("Join");
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        buttonJoin.setFont(getTextFont().deriveFont(20f));
        buttonJoin.setBackground(new Color(52,52,52));
        buttonJoin.setForeground(Color.WHITE);
        buttonJoin.setFocusPainted(false);
        buttonJoin.setBorderPainted(false);
        buttonJoin.addActionListener(e -> onJoinClicked());
        buttonsPanel.add(buttonJoin, constraints);
        this.add(buttonsPanel);
    }

    public void onCancelClicked() {
        this.setVisible(false);
    }

    public void onJoinClicked() {
        String addressString = textFieldAddress.getText();
        String portString = textFieldPort.getText();
        if (!checkValidity(addressString, portString))
            return;
        this.setVisible(false);
        app.getWindowManager().hideMenuFrame();
        app.getWindowManager().showGameFrame(new GameFrame.GameLaunchOptions(addressString, Integer.parseInt(portString)));
    }

    private boolean checkValidity(String addressString, String portString) {
        if (addressString.equals("localhost") || addressString.equals("::1")) {
            addressString = "127.0.0.1";
        }
        String[] addressParts = addressString.split("\\.");
        if (addressParts.length != 4) {
            showErrorModal("Invalid Address");
            return false;
        }
        for (String addressPart : addressParts) {
            try {
                int number = Integer.parseInt(addressPart);
                if (number < 0 || number >= 256) {
                    showErrorModal("Invalid Address");
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorModal("Invalid Address");
                return false;
            }
        }
        try {
            int number = Integer.parseInt(portString);
            if (number < 0 || number >= 65536) {
                showErrorModal("Invalid Port");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorModal("Invalid Port");
            return false;
        }
        return true;
    }

    public void showErrorModal(String message){
        new MessageBoxDialog(app, "Error", message).setVisible(true);
    }
}
