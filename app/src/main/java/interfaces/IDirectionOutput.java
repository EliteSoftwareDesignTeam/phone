package interfaces;

/**
 * Represents a way of communicating a direction to the user, e.g. vibrations, screen text or voice.
 * Cen
 */

public interface IDirectionOutput {

    /**
     * Gives directions to the user. Might only use the first parameter.
     * @param direction Which direction to direct the user to. -1 for left, 1 for right. Values in-between suggest a milder turn (e.g. 0.3 would mean to turn slightly to the right)
     * @param strength Strength of the signal. Might be higher for obstacles than regular directions. Likely not all outputs will use this.
     */
    public void giveDirection(double direction, double strength);


}
