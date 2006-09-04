package games.stendhal.server.entity.npc;

import games.stendhal.server.StendhalRPWorld;
import games.stendhal.server.entity.Player;
import games.stendhal.server.entity.item.Dice;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.events.TurnNotifier;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class CroupierNPC extends SpeakerNPC implements Dice.DiceListener {
	
	/**
	 * The time (in turns) it takes before the NPC removes
	 * thrown dice from the table.
	 */
	private static final int CLEAR_PLAYING_AREA_TIME = 3 * 10;
	
	/**
	 * The area on which the dice have to be thrown.  
	 */
	private Rectangle2D playingArea;
	
	/**
	 * An array where each possible dice sum is the index of the element
	 * which is either the name of the prize for this dice sum, or null
	 * if the player doesn't win anything for this sum.
	 */
	private String[] prizes;
	
	public CroupierNPC(String name, Rectangle playingArea) {
		super(name);
		this.playingArea = playingArea;
	}
	
	public Rectangle2D getPlayingArea() {
		return playingArea;
	}

	public void setPrizes(String[] prizes) {
		this.prizes = prizes;
	}
	
	/**
	 * Checks whether a dice has been thrown onto the playing area.
	 * @param dice The dice
	 * @return true iff the dice is lying on the playing area
	 */
	private boolean isDiceOnPlayingArea(Dice dice) {
		return playingArea.contains(dice.getX(), dice.getY());
	}

	public void onThrown(Dice dice, Player player) {
		if (isDiceOnPlayingArea(dice)) {
			int sum = dice.getSum();
			String prizeName = prizes[sum];
			if (prizeName != null) {
				Item prize = StendhalRPWorld.get().getRuleManager().getEntityManager().getItem(prizeName);
				say("Congratulations, "
						+ player.getName()
						+ ", you have "
						+ sum
						+ " points. You have won this "
						+ prizeName
						+ ".");
				player.equip(prize, true);
			} else {
				say("Sorry, "
						+ player.getName()
						+ ", you only have "
						+ sum
						+ " points. You haven't won anything. Better luck next time!");
			}
			// The croupier takes the dice away from the table after some time.
			// This is simulated by shortening the degradation time of the dice.
			TurnNotifier.get().dontNotify(dice, null);
			TurnNotifier.get().notifyInTurns(CLEAR_PLAYING_AREA_TIME, dice, null);
		}
	}
}
