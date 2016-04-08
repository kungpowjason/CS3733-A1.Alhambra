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
public class CardToKingPileMove extends Move{
	Pile pile;
	Pile kpile;
	Card cardBeingDragged;
	
	public CardToKingPileMove(Pile from,Card cbd, Pile to){
		this.pile = from;
		this.kpile = to;
		this.cardBeingDragged = cbd;
	}
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		// add card from pile to king pile
		kpile.add(cardBeingDragged);
		game.getScore().setValue(game.getScoreValue()+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// add card back from king pile to pile
		Card c = kpile.get();
		pile.add(c);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// list of boolean criteria that will judge validity of moves
		boolean sameSuit = true;
		boolean rankLower = true;
		
		Card targetCard = kpile.peek();
		
		int diff = (cardBeingDragged.getRank() - targetCard.getRank());
		// logic for card moves from pile to king pile
		if(cardBeingDragged.getSuit() != targetCard.getSuit()){
			sameSuit = false; 
		}
		if(diff != -1){
			rankLower = false;
		}
		// checks validity of the move
		if(sameSuit && rankLower){
			return true;
		}
		// if none is true then
		return false;
	}

}
