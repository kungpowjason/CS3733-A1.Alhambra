package ywu4;

import heineman.klondike.DealCardMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Move;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;

public class AlhambraDeckController extends SolitaireReleasedAdapter {
	/** The game. */
	protected Alhambra theGame;

	/** The WastePile of interest. */
	protected Pile waste;

	/** The Deck of interest. */
	protected MultiDeck deck;

	/**
	 * AlhambraDeckController constructor comment.
	 */
	public AlhambraDeckController(Alhambra theGame, MultiDeck deck, Pile waste) {
		super(theGame);

		this.theGame = theGame;
		this.waste = waste;
		this.deck = deck;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case, no
	 * drag is ever achieved, and we simply deal upon the press.
	 */
	public void mousePressed(java.awt.event.MouseEvent me) {
		// if deck is not empty, then deal a card
		if (!deck.empty()) {
			// Attempting a DealCardMove since deck is not empty
			Move m = new DealCardMove(deck, waste);
			if (m.doMove(theGame)) {
				theGame.pushMove(m); // Successful Deal one card
				theGame.refreshWidgets(); // refresh updated widgets.
			}
		}
		// if deck empty then attempt to reset deck
		else {
			// Attempting to ResetDeckMove since deck is empty
			Move m = new ResetDeckMove(deck, waste);
			if (m.doMove(theGame)) {
				theGame.pushMove(m); // Successful Deal one card
				theGame.refreshWidgets(); // refresh updated widgets.
			}
		}
	}
}
