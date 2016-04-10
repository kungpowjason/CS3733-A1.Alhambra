package ywu4;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class AlhambraReserveController extends SolitaireReleasedAdapter {

	/** The game. */
	protected Alhambra theGame;
	
	// Reserve pile being controlled
	protected PileView src;

	/**
	 * AlhambraReserveController constructor comment.
	 */
	public AlhambraReserveController(Alhambra theGame, PileView src){
		super(theGame);
		
		this.theGame = theGame;
		this.src = src;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case, no
	 * drag is ever achieved, and we simply deal upon the press.
	 */
	public void mousePressed(java.awt.event.MouseEvent me) {

		// The container manages several critical pieces of information; namely,
		// it
		// is responsible for the draggingObject; in our case, this would be a
		// CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();

		/** Return if there is no card to be chosen. */
		Pile reserve = (Pile) src.getModelElement();
		if (reserve.count() == 0) {
			c.releaseDraggingObject();
			return;
		}

		// Get a card to move from PileView. Note: this returns a CardView.
		// Note that this method will alter the model for PileView if the
		// condition is met.
		CardView cardView = src.getCardViewForTopCard(me);

		// an invalid selection of some sort.
		if (cardView == null) {
			c.releaseDraggingObject();
			return;
		}

		// If we get here, then the user has indeed clicked on the top card in
		// the PileView and
		// we are able to now move it on the screen at will. For smooth action,
		// the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println(
					"AlhambraReserveController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that
		// widget the user clicked.
		c.setActiveDraggingObject(cardView, me);

		// Tell container which source widget initiated the drag
		c.setDragSource(src);

		// The only widget that could have changed is ourselves. If we called
		// refresh, there
		// would be a flicker, because the dragged widget would not be redrawn.
		// We simply
		// force the WastePile's image to be updated, but nothing is refreshed
		// on the screen.
		// This is patently OK because the card has not yet been dragged away to
		// reveal the
		// card beneath it. A bit tricky and I like it!
		src.redraw();
	}
}
