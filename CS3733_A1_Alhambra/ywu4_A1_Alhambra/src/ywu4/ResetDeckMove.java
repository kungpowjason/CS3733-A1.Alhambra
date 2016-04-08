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

	public ResetDeckMove(MultiDeck deck, Pile waste) {
		this.deck = deck;
		this.waste = waste;
	}

	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) {
			return false;
		}
		Alhambra agame = (Alhambra) game;
		// flip waste pile to deck
		int sizeWaste = waste.count();
		for (int i = 0; i < sizeWaste; i++) {
			deck.add(waste.get());
		}
		// update the number of cards in deck
		agame.setNumDeals(agame.getNumDeals()+1);
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
		Alhambra agame = (Alhambra) game;
 		if (deck.empty() && (agame.getNumDeals() < 2)) {
			return true;
		}
		;
		return false;
	}

}
