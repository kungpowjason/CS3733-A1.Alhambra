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
		/* Note** it is important that the reset deck move is attempted before the deal card move,
		otherwise it will deal card and then reset in the same mouse press
		*/
		// Attempting to ResetDeckMove
		Move rdm = new ResetDeckMove(deck, waste);
		if (rdm.doMove(theGame)) {
			theGame.pushMove(rdm); // Successful Reset deck
			theGame.refreshWidgets(); // refresh updated widgets.
		}
		// Attempting a DealCardMove
		Move dcm = new DealCardMove(deck, waste);
		if (dcm.doMove(theGame)) {
			theGame.pushMove(dcm); // Successful Deal one card
			theGame.refreshWidgets(); // refresh updated widgets.
		}
	}
}
