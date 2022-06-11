package net.listerily.NinjaAdventure.ui.dialog;

import net.listerily.NinjaAdventure.App;

import javax.swing.*;
import java.awt.*;

public class MessageBoxDialog extends AppBaseDialog {
    private JLabel messageLabel;
    public MessageBoxDialog(App app, String title, String message) {
        super(app);
        setSize(480, 320);
        setResizable(false);
        setModal(true);
        setTitle(title);
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 20, 20, 20);

        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(getTextFont().deriveFont(30f));
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(messageLabel, constraints);

        JButton closeButton = new JButton("Close");
        constraints.gridx = 0;
        constraints.gridy = 1;
        closeButton.setBackground(new Color(52,52,52));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> setVisible(false));
        closeButton.setFont(getTextFont().deriveFont(20f));
        this.add(closeButton, constraints);
    }

    public void setMessageText(String message) {
        messageLabel.setText(message);
    }
}
