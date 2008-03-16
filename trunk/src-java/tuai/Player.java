package tuai;

import java.io.InputStream;

public interface Player {
	/**
	 * Active the player.
	 * 
	 * @return Returns true if everything is ready. Returns false if something's
	 *         wrong.
	 */
	public boolean launch();

	/**
	 * Terminate the player.
	 */
	public void terminate();

	/**
	 * Synchronize the round number of the game.
	 * 
	 * @param time
	 *            Current round number of the game.
	 */
	public void setRound(int round);

	/**
	 * Set the share piece of info of the current time of the game. Shared piece
	 * is visible to all players.
	 * 
	 * @param box
	 *            The shared piece.
	 */
	public void setShare(Box box);

	/**
	 * Set the special piece of info of the current time of the game. Special
	 * piece is visible to this player in the game.
	 * 
	 * @param box
	 *            The special piece.
	 */
	public void setSpecial(Box box);

	/**
	 * Get the reaction of the player. Time limiting should be done inside the
	 * function. The limit is specified by the argument. The running time cost
	 * of this function should be no more than timeLimit.
	 * 
	 * @param timeLimit
	 *            Time limit for reaction.
	 * @return The reaction content, null if there is no reaction in time limit.
	 */
	public Box getReaction(int timeLimit);

	/**
	 * Last time used when invoking getReaction() function.
	 * 
	 * @return Please return -1 if the last getReaction() runs more than
	 *         timeLimit specified or there is not a getReaction() invoking
	 *         before.
	 */
	public int getLastTimeUsed();

	/**
	 * A input stream log for player, will be displayed inside the user
	 * interface. Mainly for debugging.
	 * 
	 * @return Returns the input stream.
	 */
	public InputStream getLog();

	/**
	 * Return the name of the player, should be a short one.
	 * 
	 * @return The name of the player.
	 */
	public String getName();
}
