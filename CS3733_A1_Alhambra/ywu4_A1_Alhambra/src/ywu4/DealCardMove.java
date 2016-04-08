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
public class DealCardMove extends Move{
	MultiDeck deck;
	Pile waste;
	public DealCardMove(MultiDeck deck, Pile waste){
		this.deck = deck;
		this.waste = waste;
	}
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		Card card = deck.get();
		waste.add(card);
		game.updateNumberCardsLeft(-1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		Card c = waste.get();
		deck.add(c);
		game.updateNumberCardsLeft(+1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return !deck.empty();
	}

}
