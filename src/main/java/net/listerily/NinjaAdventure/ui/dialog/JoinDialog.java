package net.listerily.NinjaAdventure.ui.dialog;

import net.listerily.NinjaAdventure.App;

import javax.swing.*;
import java.awt.*;

public class JoinDialog extends AppBaseDialog {
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
        JTextField textFieldAddress = new JTextField("localhost");
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
        JTextField textFieldPort = new JTextField("2022");
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
        buttonCancel.addActionListener(e -> onCancelClicked());
        buttonsPanel.add(buttonCancel, constraints);
        JButton buttonJoin = new JButton("Join");
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        buttonJoin.setFont(getTextFont().deriveFont(20f));
        buttonJoin.addActionListener(e -> onJoinClicked());
        buttonsPanel.add(buttonJoin, constraints);
        this.add(buttonsPanel);
    }

    public void onCancelClicked() {
        this.setVisible(false);
    }

    public void onJoinClicked() {
        this.setVisible(false);
        app.getWindowManager().hideMenuFrame();
        app.getWindowManager().showGameFrame();
    }
}
