package ywu4;

import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class AlhambraWasteController extends java.awt.event.MouseAdapter {
	/** The Alhambra Game. */
	Alhambra theGame;

	/** The specific waste pileView being controlled. */
	PileView src;

	/**
	 * AlhambraWasteController constructor comment.
	 */
	public AlhambraWasteController(Alhambra theGame, PileView waste) {
		super();

		this.theGame = theGame;
		this.src = waste;
	}

	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * @param me
	 *            java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();
		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println("AlhambraWasteController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();
			return;
		}

		/** Recover the from reserve Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println("AlhambraWasteController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile waste = (Pile) src.getModelElement();
		Pile reserve = (Pile) fromWidget.getModelElement();

		CardView cardView = (CardView) draggingWidget;
		Card theCard = (Card) cardView.getModelElement();

		// create the move object
		Move move = new CardToWasteMove(reserve, theCard, waste);

		if (move.doMove(theGame)) {
			// Success
			theGame.pushMove(move);
			System.out.println(waste.count());
		} else {
			fromWidget.returnWidget(draggingWidget);
		}
		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();

		// finally repaint
		c.repaint();

	}

	public void mousePressed(java.awt.event.MouseEvent me) {

		// The container manages several critical pieces of information; namely,
		// it
		// is responsible for the draggingObject; in our case, this would be a
		// CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();

		/** Return if there is no card to be chosen. */
		Pile waste = (Pile) src.getModelElement();
		if (waste.count() == 0) {
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
					"WasteController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
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
