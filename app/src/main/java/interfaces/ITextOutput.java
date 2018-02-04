package interfaces;

/**
 * Represents a way of conveying any text message to the user, e.g. voice or screen text
 */

public interface ITextOutput {

    /**
     * Sends a message to the user, either displaying it on a screen and speaking via headphones
     * @param message The message to send to the user
     */
    public void sendMessage(String message);

}
