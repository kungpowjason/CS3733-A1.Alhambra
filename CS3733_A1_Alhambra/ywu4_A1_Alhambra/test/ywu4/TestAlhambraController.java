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
		// create mouse press at coordinates within the deckview; should deal card
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
}
