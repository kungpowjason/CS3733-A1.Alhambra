package ywu4;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * 
 * Move card from top of reserve pile to top of waste pile
 *
 */
public class CardToWasteMove extends Move{
	// reserve/source pile
	Pile reserve;
	// waste pile target
	Pile waste;
	// card of interest
	Card cardBeingDragged;
	// Constructor comment
	public CardToWasteMove(Pile from,Card cbd, Pile to){
		this.reserve = from;
		this.waste = to;
		this.cardBeingDragged = cbd;
	}
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		// add card from reserve to waste pile
		waste.add(cardBeingDragged);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// add card back from waste to reserve pile
		Card c = waste.get();
		reserve.add(c);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// if waste has no cards, then automatically valid move
		if (waste.empty()){return true;};
		// list of boolean criteria that will judge validity of moves
		boolean sameSuit = true;
		boolean rankDiff = true;
		
		// checks the target pile's top card
		Card targetCard = waste.peek();
		
		// finds difference in rank
		int diff = Math.abs(cardBeingDragged.getRank() - targetCard.getRank());
		
		// logic for card moves from reserve to waste
		if(cardBeingDragged.getSuit() != targetCard.getSuit()){
			sameSuit = false; 
		}
		if(diff != 1){
			rankDiff = false;
		}
		// checks validity of the move
		if(sameSuit && rankDiff){
			return true;
		}
		// if none is true then
		return false;
	}

}
