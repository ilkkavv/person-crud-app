package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class SwingGui extends JFrame implements AppUi {
    /** Controller used to handle person-related application logic. */
    private final PersonController controller;
    private final int minWindowWidth = 800;
    private final int minWindowHeight = 600;

    public SwingGui(final PersonController controller) {
        super("Person CRUD App");
        this.controller = controller;

        initializeFrame();
    }

    @Override
    public void run() {
        this.setVisible(true);
    }

    private void initializeFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(minWindowWidth, minWindowHeight));
        this.setSize(minWindowWidth, minWindowHeight);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
    }
}
