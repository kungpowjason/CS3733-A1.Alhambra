package ywu4;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * 
 * Move card from top of source pile to top of ace pile
 *
 */
public class CardToAcePileMove extends Move{
	// source pile
	Pile pile;
	// target pile
	Pile apile;
	// card of interest
	Card cardBeingDragged;
	
	// Constructor comment
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
		// add card from source pile to ace pile
		apile.add(cardBeingDragged);
		// update the score
		game.getScore().setValue(game.getScoreValue()+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// add card back from ace pile to source pile
		Card c = apile.get();
		pile.add(c);
		// update the score
		game.getScore().setValue(game.getScoreValue()-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// list of boolean criteria that will judge validity of moves
		boolean sameSuit = true;
		boolean rankHigher = true;
		
		// check the target card
		Card targetCard = apile.peek();
		
		// find the difference in rank
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
