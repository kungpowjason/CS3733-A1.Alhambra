package ywu4;

import heineman.klondike.KlondikeDeckController;
import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class Alhambra extends Solitaire {
	// Entity Objects
	MultiDeck deck;
	Pile waste;
	Pile[] apile = new Pile[4];
	Pile[] kpile = new Pile[4];
	Pile[] reserve = new Pile[8];
	int numDeals;
	// Boundary View Objects
	DeckView deckView;
	PileView wasteView;
	PileView[] apileView = new PileView[4];
	PileView[] kpileView = new PileView[4];
	PileView[] reserveView = new PileView[8];
	IntegerView numLeftView;
	IntegerView scoreView;

	@Override
	public String getName() {
		return "ywu4 - Alhambra";
	}

	// @Override
	// When all foundations have been built (1 pnt per card placed) 12 x 8 = 96
	public boolean hasWon() {
		return getScore().getValue() == 96;
	}
	// getter for numDeals
	public int getNumDeals(){
		return numDeals;
	}
	// setter for numDeals
	public void setNumDeals(int num){
		this.numDeals = num;
	}

	public void initializeModel(int seed) {
		// initial score is set to ZERO (every Solitaire game by default has a
		// score)
		// and there are 104 cards left.
		numLeft = getNumLeft();
		numLeft.setValue(104);
		score = getScore();
		score.setValue(0);

		// add a deck to the model, shuffled
		deck = new MultiDeck("deck", 2);
		deck.create(seed);
		model.addElement(deck);
		// add foundation pile elements
		for (int i = 0; i < 4; i++) {
			apile[i] = new Pile("apile" + i);
			kpile[i] = new Pile("kpile" + i);
			model.addElement(apile[i]);
			model.addElement(kpile[i]);
		}
		// add reserve pile elements
		for (int i = 0; i < 8; i++) {
			reserve[i] = new Pile("reserve" + i);
			model.addElement(reserve[i]);
		}
		// add waste pile element
		waste = new Pile("waste");
		model.addElement(waste);

		updateScore(0);
	}

	public void initializeView() {

		// Get the card artwork to be used. This is needed for the dimensions.
		CardImages ci = getCardImages();

		// add a deck to the view, shuffled
		deckView = new DeckView(deck);
		deckView.setBounds(60 + 3 * ci.getWidth(), 100 + 2 * ci.getHeight(), ci.getWidth(), ci.getHeight());
		container.addWidget(deckView);

		// add foundation, reserve, and waste piles
		for (int fndtnNum = 0; fndtnNum < 4; fndtnNum++) {
			apileView[fndtnNum] = new PileView(apile[fndtnNum]);
			apileView[fndtnNum].setBounds(20 + 20 * fndtnNum + (fndtnNum) * ci.getWidth(), 20, ci.getWidth(),
					ci.getHeight());
			container.addWidget(apileView[fndtnNum]);
			kpileView[fndtnNum] = new PileView(kpile[fndtnNum]);
			kpileView[fndtnNum].setBounds(20 + 20 * (fndtnNum + 4) + (fndtnNum + 4) * ci.getWidth(), 20, ci.getWidth(),
					ci.getHeight());
			container.addWidget(kpileView[fndtnNum]);

		}

		for (int rsrvNum = 0; rsrvNum < 8; rsrvNum++) {
			reserveView[rsrvNum] = new PileView(reserve[rsrvNum]);
			reserveView[rsrvNum].setBounds(20 + 20 * rsrvNum + rsrvNum * ci.getWidth(), ci.getHeight() + 60,
					ci.getWidth(), ci.getHeight());
			container.addWidget(reserveView[rsrvNum]);
		}

		wasteView = new PileView(waste);
		wasteView.setBounds(120 + 4 * ci.getWidth(), 100 + 2 * ci.getHeight(), ci.getWidth(), ci.getHeight());
		addViewWidget(wasteView);

		scoreView = new IntegerView(getScore());
		scoreView.setBounds(20 + ci.getWidth(), 120 + 2 * ci.getHeight(), 100, 60);
		addViewWidget(scoreView);

		numLeftView = new IntegerView(getNumLeft());
		numLeftView.setBounds(20 + ci.getWidth(), 200 + 2 * ci.getHeight(), 100, 60);
		addViewWidget(numLeftView);

	}

	public void initializeController() {
		// Initialize Controllers for DeckView
		deckView.setMouseAdapter(new AlhambraDeckController(this, deck, waste));
		deckView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		deckView.setUndoAdapter(new SolitaireUndoAdapter(this));

		// Initialize Controllers for wasteView
		wasteView.setMouseAdapter(new AlhambraWasteController(this, wasteView));
		wasteView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		wasteView.setUndoAdapter(new SolitaireUndoAdapter(this));
		
		// Initialize Controllers for Foundations; Ace and King Piles
		for (int i = 0; i < 4; i++) {
			apileView[i].setMouseAdapter(new AlhambraAcePileController(this,apileView[i]));
			apileView[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			apileView[i].setUndoAdapter(new SolitaireUndoAdapter(this));
			kpileView[i].setMouseAdapter(new AlhambraKingPileController(this,kpileView[i]));
			kpileView[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			kpileView[i].setUndoAdapter(new SolitaireUndoAdapter(this));
		}
		// Initialize Controllers for reserveView
		for (int i = 0; i < 8; i++) {
			reserveView[i].setMouseAdapter(new AlhambraReserveController(this, reserveView[i]));
			reserveView[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			reserveView[i].setUndoAdapter(new SolitaireUndoAdapter(this));
		}
	}

	@Override
	public void initialize() {
		// Initialize model, view, and controllers.
		initializeModel(getSeed());
		initializeView();
		initializeController();

		
		// finds 4 different suit aces in deck
		int maxDeckCards = deck.count();
		for(int i =0; i < maxDeckCards; i++){
			if((deck.peek().getRank() == Card.ACE) && (deck.peek().getSuit() == Card.CLUBS) && apile[0].count() < 1){
				apile[0].add(deck.get());
			}
			else if((deck.peek().getRank() == Card.ACE) && (deck.peek().getSuit() == Card.DIAMONDS) && apile[1].count() < 1){
				apile[1].add(deck.get());
			}
			else if((deck.peek().getRank() == Card.ACE) && (deck.peek().getSuit() == Card.HEARTS) && apile[2].count() < 1){
				apile[2].add(deck.get());
			}
			else if((deck.peek().getRank() == Card.ACE) && (deck.peek().getSuit() == Card.SPADES) && apile[3].count() < 1){
				apile[3].add(deck.get());
			}
			else{
				waste.add(deck.get());
			}
		}
		updateNumberCardsLeft(-4);
		// finds 4 different suit kings from waste pile
		int maxWasteCards = waste.count();
		for(int i =0; i < maxWasteCards; i++){
			if((waste.peek().getRank() == Card.KING) && (waste.peek().getSuit() == Card.CLUBS) && kpile[0].count() < 1){
				kpile[0].add(waste.get());
			}
			else if((waste.peek().getRank() == Card.KING) && (waste.peek().getSuit() == Card.DIAMONDS) && kpile[1].count() < 1){
				kpile[1].add(waste.get());
			}
			else if((waste.peek().getRank() == Card.KING) && (waste.peek().getSuit() == Card.HEARTS) && kpile[2].count() < 1){
				kpile[2].add(waste.get());
			}
			else if((waste.peek().getRank() == Card.KING) && (waste.peek().getSuit() == Card.SPADES) && kpile[3].count() < 1){
				kpile[3].add(waste.get());
			}
			else{
				deck.add(waste.get());
			}
		}
		updateNumberCardsLeft(-4);
		// fill each reserve with 4 cards
		for(int i =0; i < 8; i++){
			for(int j = 0; j < 4; j++){
				reserve[i].add(deck.get());
			}
		}
		updateNumberCardsLeft(-32);
	}

	/** Code to launch solitaire variation. */
	public static void main(String[] args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		Main.generateWindow(new Alhambra(), Deck.OrderBySuit);
	}

}
