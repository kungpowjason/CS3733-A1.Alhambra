package ywu4;

import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class AlhambraKingPileController extends java.awt.event.MouseAdapter {
	/** The Alhambra Game. */
	Alhambra theGame;

	/** The specific king foundation pileView being controlled. */
	PileView src;

	/**
	 * KingPileController constructor comment.
	 */
	public AlhambraKingPileController(Alhambra theGame, PileView kpile) {
		super();

		this.theGame = theGame;
		this.src = kpile;
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
			System.err.println("AlhambraKingPileController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();
			return;
		}

		/** Recover the from reserve OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println("AlhambraKingPileController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile apile = (Pile) src.getModelElement();
		Pile reserve = (Pile) fromWidget.getModelElement();
		
		CardView cardView = (CardView) draggingWidget;
		Card theCard = (Card) cardView.getModelElement();

		// create move object
		Move move = new CardToKingPileMove(reserve, theCard, apile);
		
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
