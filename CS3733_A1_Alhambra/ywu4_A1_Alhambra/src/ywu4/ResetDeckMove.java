package ywu4;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;

/**
 * 
 * Move card from top of deck to top of waste pile
 *
 */
public class ResetDeckMove extends Move {
	MultiDeck deck;
	Pile waste;
	int numDeals;

	public ResetDeckMove(MultiDeck deck, Pile waste, int numDeals) {
		this.deck = deck;
		this.waste = waste;
		this.numDeals = numDeals;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) {
			return false;
		}
		// flip waste pile to deck
		int sizeWaste = waste.count();
		for (int i = 0; i < sizeWaste; i++) {
			deck.add(waste.get());
		}
		// update the number of cards in deck
		game.updateNumberCardsLeft(sizeWaste);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// flip deck back into waste
		int sizeDeck = deck.count();
		for (int i = 0; i < sizeDeck; i++) {
			waste.add(deck.get());
		}
		// update number of cards in deck to previous
		game.updateNumberCardsLeft(-sizeDeck);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// if the deck is empty and the deck has not been re-dealt more than
		// once
		if (deck.empty() && (numDeals < 2)) {
			return true;
		}
		;
		return false;
	}

}
