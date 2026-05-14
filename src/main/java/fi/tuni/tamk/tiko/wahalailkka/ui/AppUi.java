package fi.tuni.tamk.tiko.wahalailkka.ui;

/**
 * Common interface for application user interfaces.
 * <p>
 * Implementations are responsible for starting and running the
 * application user interface, such as a command-line interface
 * or graphical user interface.
 */
@FunctionalInterface
public interface AppUi {
    /**
     * Starts and runs the user interface.
     */
    void run();
}
