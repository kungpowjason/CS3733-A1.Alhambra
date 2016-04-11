package ywu4;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestAlhambraMove extends TestCase {

	Alhambra game;
	GameWindow gw;

	@Override
	protected void setUp() {
		game = new Alhambra();
		gw = Main.generateWindow(game, Deck.OrderBySuit);
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

	@Test
	public void testDealCardMove() {

		emptyDeck();
		assertTrue(game.deck.empty());

		DealCardMove dcm0 = new DealCardMove(game.deck, game.waste);

		assertFalse(dcm0.valid(game));

		assertFalse(dcm0.doMove(game));

		tearDown();
		setUp();

		Card topCard = game.deck.peek();
		DealCardMove dcm1 = new DealCardMove(game.deck, game.waste);

		assertTrue(dcm1.valid(game));

		assertEquals(64, game.deck.count());
		assertTrue(dcm1.doMove(game));

		assertEquals(topCard, game.waste.peek());
		int value = game.getNumLeft().getValue();

		assertEquals(63, value);

		dcm1.undo(game);

		assertEquals(64, game.deck.count());

	}

	@Test
	public void testResetDeckMove() {
		// empty the deck
		emptyDeck();

		// test for valid move
		assertTrue(game.deck.empty());
		ResetDeckMove rdm = new ResetDeckMove(game.deck, game.waste);
		assertTrue(rdm.valid(game));

		// attempt to reset deck
		rdm.doMove(game);
		assertEquals(1, game.getNumDeals());
		// undo case
		rdm.undo(game);
		assertEquals(0, game.getNumDeals());

		// redo move Deals = 1
		rdm.doMove(game);
		assertEquals(1, game.getNumDeals());

		// empty deck then do move again Deals = 2
		emptyDeck();
		rdm.doMove(game);
		assertEquals(2, game.getNumDeals());

		// empty deck then try again but should fail
		emptyDeck();
		rdm.doMove(game);
		assertEquals(2, game.getNumDeals());

	}

	@Test
	public void testCardToAcePileMove() {
		tearDown();
		setUp();
		// get initial top cards of reserve and ace
		Card initialReserve = game.reserve[2].peek();
		Card initialAce0 = game.apile[3].peek();

		// card being dragged is simply a card from reserve
		Card drag0 = game.reserve[2].get();
		CardToAcePileMove ctapm0 = new CardToAcePileMove(game.reserve[2], drag0, game.apile[3]);
		assertTrue(ctapm0.valid(game));
		ctapm0.doMove(game);
		// score check
		assertEquals(1, game.getScoreValue());

		// undo test case
		ctapm0.undo(game);
		assertEquals(initialReserve, game.reserve[2].peek());
		assertEquals(initialAce0, game.apile[3].peek());
		// score check
		assertEquals(0, game.getScoreValue());

		// card being dragged is not the same rank or not one suit higher or
		// lower
		Card drag1 = game.reserve[0].get();
		CardToAcePileMove ctapm1 = new CardToAcePileMove(game.reserve[2], drag1, game.apile[2]);
		assertFalse(ctapm1.doMove(game));

		// same scenario test for waste pile instead
		// get initial top cards of reserve and ace

		for (int i = 0; i < 4; i++) {
			DealCardMove dcm = new DealCardMove(game.deck, game.waste);
			dcm.doMove(game);
		}

		Card initialWaste = game.waste.peek();
		Card initialAce1 = game.apile[1].peek();

		// card being dragged is simply a card from waste
		Card drag2 = game.waste.get();
		CardToAcePileMove ctapm2 = new CardToAcePileMove(game.waste, drag2, game.apile[1]);
		assertTrue(ctapm2.valid(game));
		ctapm2.doMove(game);
		// score check
		assertEquals(1, game.getScoreValue());

		// undo test case
		ctapm2.undo(game);
		assertEquals(initialWaste, game.waste.peek());
		assertEquals(initialAce1, game.apile[1].peek());
		// score check
		assertEquals(0, game.getScoreValue());
	}

	@Test
	public void testCardToKingPileMove() {
		tearDown();
		setUp();
		// Attempt to move wrong card
		// card being dragged is simply a card from waste

		// put one card in waste
		DealCardMove dcm0 = new DealCardMove(game.deck, game.waste);
		dcm0.doMove(game);

		// attempt to do a king pile move -> should fail
		Card drag1 = game.waste.peek();
		CardToKingPileMove ctkpm1 = new CardToKingPileMove(game.waste, drag1, game.kpile[0]);
		assertFalse(ctkpm1.valid(game));
		ctkpm1.doMove(game);

		// deal cards until a valid card for move appears
		for (int i = 0; i < 5; i++) {
			DealCardMove dcm1 = new DealCardMove(game.deck, game.waste);
			dcm1.doMove(game);
		}

		// records initial conditions
		Card initialWaste = game.waste.peek();
		Card initialKing1 = game.kpile[0].peek();

		// card being dragged is simply a card from waste and king move is
		// attempted -> should work
		Card drag2 = game.waste.get();
		CardToKingPileMove ctkpm2 = new CardToKingPileMove(game.waste, drag2, game.kpile[0]);
		assertTrue(ctkpm2.valid(game));
		ctkpm2.doMove(game);
		// score check
		assertEquals(1, game.getScoreValue());

		// undo test case
		ctkpm2.undo(game);
		assertEquals(initialWaste, game.waste.peek());
		assertEquals(initialKing1, game.kpile[0].peek());
		// score check
		assertEquals(0, game.getScoreValue());
	}

	@Test
	public void testCardToWasteMove() {
		tearDown();
		setUp();
		/** case where waste is empty **/
		// get initial top cards of reserve and waste
		Card initialReserve = game.reserve[2].peek();
		Card initialWaste = game.waste.peek(); // should be empty

		// card being dragged is simply a card from reserve
		Card drag0 = game.reserve[2].get();
		CardToWasteMove ctwm0 = new CardToWasteMove(game.reserve[2], drag0, game.waste);
		assertTrue(ctwm0.valid(game));
		ctwm0.doMove(game);

		// undo test case
		ctwm0.undo(game);
		assertEquals(initialReserve, game.reserve[2].peek());
		assertEquals(initialWaste, game.waste.peek());

		/** Case where there is card in waste **/

		DealCardMove dcm = new DealCardMove(game.deck, game.waste);
		dcm.doMove(game);

		// Reserve card is one rank higher
		Card drag1 = game.reserve[7].get();
		CardToWasteMove ctwm1 = new CardToWasteMove(game.reserve[7], drag1, game.waste);
		assertTrue(ctwm1.doMove(game));

		ctwm1.undo(game);

		// Reserve card is one rank lower
		for (int i = 0; i < 17; i++) {
			dcm.doMove(game);
		}
		Card drag2 = game.reserve[0].get();
		CardToWasteMove ctwm2 = new CardToWasteMove(game.reserve[0], drag2, game.waste);
		assertTrue(ctwm2.doMove(game));
		
		// Fail case scenario neither suit nor rank matches
		Card drag3 = game.reserve[3].get();
		CardToWasteMove ctwm3 = new CardToWasteMove(game.reserve[3], drag3, game.waste);
		assertFalse(ctwm3.doMove(game));

	}

}
