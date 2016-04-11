package ywu4;

import java.awt.event.MouseEvent;

import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.common.view.CardImages;
import ks.launcher.Main;
import ks.tests.KSTestCase;

public class TestAlhambraController extends KSTestCase {

	Alhambra game;
	GameWindow gw;
	CardImages ci;

	@Override
	protected void setUp() {
		game = new Alhambra();
		gw = Main.generateWindow(game, Deck.OrderBySuit);
		ci = game.getCardImages();
	}

	@Override
	protected void tearDown() {
		gw.dispose();
	}

	public void emptyDeck() {
		int maxDeckCards = game.deck.count();

		// empties deck to waste
		for (int i = 0; i < maxDeckCards; i++) {
			game.waste.add(game.deck.get());
		}
	}

	public void testDeckController() {
		// create mouse press at coordinates within the deckview; should deal
		// card
		MouseEvent press = this.createPressed(game, game.deckView, 60 + 3 * ci.getWidth(), 100 + 2 * ci.getHeight());
		game.deckView.getMouseManager().handleMouseEvent(press);

		// what do we know about the game after press on deck? Card dealt!
		assertEquals("5D", game.waste.peek().toString());

		emptyDeck();
		assertTrue(game.deck.empty());

		game.deckView.getMouseManager().handleMouseEvent(press);

		assertFalse(game.deck.empty());

		assertTrue(game.waste.empty());
	}

	public void testWasteController() {
		tearDown();
		setUp();

		// deal a card
		DealCardMove dcm1 = new DealCardMove(game.deck, game.waste);
		dcm1.doMove(game);

		assertEquals("5D", game.waste.peek().toString());

		// create mouse press at coordinates within the reserve view
		int rsrvNum = 7;
		MouseEvent press = this.createPressed(game, game.reserveView[7], 20 + 20 * rsrvNum + rsrvNum * ci.getWidth(),
				ci.getHeight() + 60);
		game.reserveView[7].getMouseManager().handleMouseEvent(press);

		// create mouse release at coordinates within the waste view; card
		// should be placed
		MouseEvent release = this.createReleased(game, game.wasteView,
				120 + 4 * ci.getWidth(), 100 + 2 * ci.getHeight());
		game.wasteView.getMouseManager().handleMouseEvent(release);

		// what do we know about the game after release on waste pile? The card
		// from reserve is now card on waste pile
		assertEquals("7D", game.reserve[7].peek().toString());

		// waste pile should now have 6 of Diamonds
		assertEquals("6D", game.waste.peek().toString());

		// create mouse press within wasteview
		game.wasteView.getMouseManager().handleMouseEvent(press);

		// card on waste should be 5D
		assertEquals("5D", game.waste.peek().toString());
	}

	public void testAcePileController() {
		tearDown();
		setUp();

		// deal cards until a valid card for move appears
		for (int i = 0; i < 4; i++) {
			DealCardMove dcm1 = new DealCardMove(game.deck, game.waste);
			dcm1.doMove(game);
		}

		assertEquals("2D", game.waste.peek().toString());

		// create mouse press at coordinates within the wasteview
		MouseEvent press = this.createPressed(game, game.wasteView, 120 + 4 * ci.getWidth(), 100 + 2 * ci.getHeight());
		game.wasteView.getMouseManager().handleMouseEvent(press);

		// create mouse release at coordinates within the ace pile view; card
		// should be placed
		int fndtnNum = 1;
		MouseEvent release = this.createReleased(game, game.apileView[1],
				20 + 20 * fndtnNum + (fndtnNum) * ci.getWidth(), 20);
		game.apileView[1].getMouseManager().handleMouseEvent(release);

		// what do we know about the game after release on ace pile? The card
		// from waste is now card on ace pile
		assertEquals("2D", game.apile[1].peek().toString());

		// waste pile should now have 3 of Diamonds
		assertEquals("3D", game.waste.peek().toString());

		// create mouse press within wasteview
		game.wasteView.getMouseManager().handleMouseEvent(press);

		// create mouse release at coordinates within the ace pile view
		// (different one); card should not be placed
		fndtnNum = 2;
		MouseEvent release1 = this.createReleased(game, game.apileView[2],
				20 + 20 * fndtnNum + (fndtnNum) * ci.getWidth(), 20);
		game.apileView[2].getMouseManager().handleMouseEvent(release1);

		// card should be returned to the waste
		assertEquals("3D", game.waste.peek().toString());
	}

	public void testKingPileController() {
		tearDown();
		setUp();

		// deal cards until a valid card for move appears
		for (int i = 0; i < 6; i++) {
			DealCardMove dcm1 = new DealCardMove(game.deck, game.waste);
			dcm1.doMove(game);
		}

		assertEquals("QC", game.waste.peek().toString());

		// create mouse press at coordinates within the waste view
		MouseEvent press = this.createPressed(game, game.wasteView, 120 + 4 * ci.getWidth(), 100 + 2 * ci.getHeight());
		game.wasteView.getMouseManager().handleMouseEvent(press);

		// create mouse release at coordinates within the king pile view; card
		// should be placed
		int fndtnNum = 1;
		MouseEvent release = this.createReleased(game, game.kpileView[0],
				20 + 20 * (fndtnNum + 4) + (fndtnNum + 4) * ci.getWidth(), 20);
		game.kpileView[0].getMouseManager().handleMouseEvent(release);

		// what do we know about the game after release on king pile? The card
		// from waste is now card on ace pile
		assertEquals("QC", game.kpile[0].peek().toString());

		// waste pile should now have 3 of Diamonds
		assertEquals("KC", game.waste.peek().toString());

		// create mouse press within wasteview
		game.wasteView.getMouseManager().handleMouseEvent(press);

		// create mouse release at coordinates within the ace pile view
		// (different one); card should not be placed
		fndtnNum = 2;
		MouseEvent release1 = this.createReleased(game, game.kpileView[2],
				20 + 20 * fndtnNum + (fndtnNum) * ci.getWidth(), 20);
		game.kpileView[2].getMouseManager().handleMouseEvent(release1);

		// card should be returned to the waste
		assertEquals("KC", game.waste.peek().toString());
	}

	public void testReserveController() {

		tearDown();
		setUp();
		// create mouse press at coordinates within the reserve view;
		int rsrvNum = 0;
		MouseEvent press = this.createPressed(game, game.reserveView[0], 20 + 20 * rsrvNum + rsrvNum * ci.getWidth(),
				ci.getHeight() + 60);
		game.reserveView[0].getMouseManager().handleMouseEvent(press);

		// card should be grabbed and current card on reserve should be jack of
		// spades
		assertEquals("JS", game.reserve[0].peek().toString());
	}

}
