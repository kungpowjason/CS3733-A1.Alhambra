package ywu4;

import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class AlhambraAcePileController extends java.awt.event.MouseAdapter {
	/** The Alhambra Game. */
	Alhambra theGame;

	/** The specific waste pileView being controlled. */
	PileView src;

	/**
	 * FoundationController constructor comment.
	 */
	public AlhambraAcePileController(Alhambra theGame, PileView apile) {
		super();

		this.theGame = theGame;
		this.src = apile;
	}

	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards can
	 * be dragged both from the WastePile (as a CardView object) and the
	 * BuildablePileView (as a ColumnView).
	 * 
	 * @param me
	 *            java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();
		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile apile = (Pile) src.getModelElement();
		Pile reserve = (Pile) fromWidget.getModelElement();
		
		CardView cardView = (CardView) draggingWidget;
		Card theCard = (Card) cardView.getModelElement();

		Move move = new CardToAcePileMove(reserve, theCard, apile);
		
		if (move.doMove(theGame)) {
			// Success
			theGame.pushMove(move);
		} else {
			fromWidget.returnWidget(draggingWidget);
		}
		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();

		// finally repaint
		c.repaint();

	}
}

