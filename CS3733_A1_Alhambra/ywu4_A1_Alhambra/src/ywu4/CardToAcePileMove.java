package ywu4;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * 
 * Move card from top of pile to top of ace pile
 *
 */
public class CardToAcePileMove extends Move{
	Pile pile;
	Pile apile;
	Card cardBeingDragged;
	
	public CardToAcePileMove(Pile from,Card cbd, Pile to){
		this.pile = from;
		this.apile = to;
		this.cardBeingDragged = cbd;
	}
	@Override
	public boolean doMove(Solitaire game) {
		if(!valid(game)){
			return false;
		}
		// add card from pile to ace pile
		apile.add(cardBeingDragged);
		game.getScore().setValue(game.getScoreValue()+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// add card back from ace pile to pile
		Card c = apile.get();
		pile.add(c);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// list of boolean criteria that will judge validity of moves
		boolean sameSuit = true;
		boolean rankHigher = true;
		
		Card targetCard = apile.peek();
		
		int diff = (cardBeingDragged.getRank() - targetCard.getRank());
		// logic for card moves from pile to ace pile
		if(cardBeingDragged.getSuit() != targetCard.getSuit()){
			sameSuit = false; 
		}
		if(diff != 1){
			rankHigher = false;
		}
		// checks validity of the move
		if(sameSuit && rankHigher){
			return true;
		}
		// if none is true then
		return false;
	}

}
