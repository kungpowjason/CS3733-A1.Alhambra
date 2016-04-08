package ywu4;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestDealCardMove extends TestCase{

	@Test
	public void test() {
		Alhambra game = new Alhambra();
		GameWindow gw = Main.generateWindow(game, Deck.OrderBySuit);
		
		Card topCard = game.deck.peek();
		DealCardMove dcm = new DealCardMove(game.deck, game.waste);
		
		assertTrue(dcm.valid(game));
		
		assertEquals(64, game.deck.count());
		
		dcm.doMove(game);
		
		assertEquals(topCard, game.waste.peek());
		int value = game.getNumLeft().getValue();
		
		assertEquals(63,value);
		
		dcm.undo(game);
		
		assertEquals(64, game.deck.count());
		
	}

}
