package ywu4;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * 
 * Move card from top of source pile to top of king pile
 *
 */
public class CardToKingPileMove extends Move{
	//source pile
	Pile pile;
	//target pile
	Pile kpile;
	//card of interest
	Card cardBeingDragged;
	// Constructor comment
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
		// add card from source pile to king pile
		kpile.add(cardBeingDragged);
		// update the score
		game.getScore().setValue(game.getScoreValue()+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// add card back from king pile to source pile
		Card c = kpile.get();
		pile.add(c);
		// update the score
		game.getScore().setValue(game.getScoreValue()-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// list of boolean criteria that will judge validity of moves
		boolean sameSuit = true;
		boolean rankLower = true;
		
		// checks the target card
		Card targetCard = kpile.peek();
		
		// finds the difference in rank
		int diff = (cardBeingDragged.getRank() - targetCard.getRank());
		
		// logic for card moves from source pile to king pile
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
