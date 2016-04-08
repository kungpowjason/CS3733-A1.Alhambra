package ywu4;

import org.junit.Test;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestResetDeckMove extends TestCase{

	@Test
	public void test() {
		Alhambra game = new Alhambra();
		GameWindow gw = Main.generateWindow(game, Deck.OrderBySuit);
		
		Card topCard = game.deck.peek();
		
		int maxDeckCards = game.deck.count();
		
		//empties deck to waste
		for(int i = 0; i < maxDeckCards; i++){
			game.waste.add(game.deck.get());
		}
		
		assertTrue(game.deck.empty());
		
		ResetDeckMove rdm = new ResetDeckMove(game.deck, game.waste);
		
		assertTrue(rdm.valid(game));
		
		rdm.doMove(game);
		
		assertEquals(1,game.getNumDeals());
		
		
	}

}
