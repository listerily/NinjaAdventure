package net.listerily.NinjaAdventure.ui.components;

import net.listerily.NinjaAdventure.ui.frame.AppBaseFrame;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {
    private JLabel titleLabel;
    public LoadingPanel(AppBaseFrame frame) {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 0, 0, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        titleLabel = new JLabel("Establishing connection", JLabel.CENTER);
        titleLabel.setFont(frame.getTextFont().deriveFont(70f));
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(titleLabel, constraints);

        LoadingBar loadingBar = new LoadingBar();
        loadingBar.setFont(frame.getTextFont().deriveFont(70f));
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(loadingBar, constraints);
    }

    public void setLoadingMessage(String message) {
        titleLabel.setText(message);
    }
}
