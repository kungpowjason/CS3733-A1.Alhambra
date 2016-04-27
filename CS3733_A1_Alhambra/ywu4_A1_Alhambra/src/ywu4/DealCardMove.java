package ywu4;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
/**
 * 
 * Move card from top of deck to top of waste pile.
 *
 */
public class DealCardMove extends Move{
	// source deck of cards
	MultiDeck deck;
	// target waste pile
	Pile waste;
	// Constructor comment
	public DealCardMove(MultiDeck deck, Pile waste){
		this.deck = deck;
		this.waste = waste;
	}
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		// adds card from top of deck to top of waste pile
		Card card = deck.get();
		waste.add(card);
		// update the number of cards left in the deck
		game.updateNumberCardsLeft(-1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// adds card from top of waste pile to top of deck
		Card c = waste.get();
		deck.add(c);
		// update the number of cards left in the deck
		game.updateNumberCardsLeft(+1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// valid if deck is not empty
		return !deck.empty();
	}

}
