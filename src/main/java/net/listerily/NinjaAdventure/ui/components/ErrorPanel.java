package net.listerily.NinjaAdventure.ui.components;

import net.listerily.NinjaAdventure.ui.WindowManager;
import net.listerily.NinjaAdventure.ui.frame.AppBaseFrame;

import javax.swing.*;
import java.awt.*;

public class ErrorPanel extends JPanel {
    private final AppBaseFrame frame;
    private final JLabel summaryLabel;
    public ErrorPanel(AppBaseFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(40, 0, 0, 40);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("!!ERROR!!", JLabel.CENTER);
        title.setFont(frame.getTextFont().deriveFont(100f));
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(title, constraints);

        summaryLabel = new JLabel("UNDEFINED ERROR", JLabel.CENTER);
        summaryLabel.setFont(frame.getTextFont().deriveFont(25f));
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(summaryLabel, constraints);


        JButton backButton = new JButton("CLOSE");
        backButton.setFont(frame.getTextFont().deriveFont(30f));
        backButton.setBackground(new Color(52,52,52));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        constraints.gridx = 0;
        constraints.gridy = 2;
        backButton.addActionListener(e -> onBackClicked());
        add(backButton, constraints);
    }

    public void setErrorMessage(String message) {
        summaryLabel.setText(message);
    }

    public void onBackClicked() {
        WindowManager windowManager = frame.getWindowManager();
        windowManager.hideGameFrame();
        windowManager.showMenuFrame();
    }
}
