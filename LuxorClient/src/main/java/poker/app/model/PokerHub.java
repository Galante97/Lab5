package poker.app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exceptions.DeckException;
import netgame.common.Hub;
import pokerBase.Action;
import pokerBase.Card;
import pokerBase.Deck;
import pokerBase.GamePlay;
import pokerBase.GamePlayPlayerHand;
import pokerBase.Player;
import pokerBase.Rule;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.eGame;
import pokerEnums.eGameState;

public class PokerHub extends Hub {

	private Table HubPokerTable = new Table();
	private GamePlay HubGamePlay;
	private int iDealNbr = 0;
	//private PokerGameState state;
	private eGameState eGameState;

	public PokerHub(int port) throws IOException {
		super(port);
	}

	protected void playerConnected(int playerID) {

		if (playerID == 2) {
			shutdownServerSocket();
		}
	}

	protected void playerDisconnected(int playerID) {
		shutDownHub();
	}

	protected void messageReceived(int ClientID, Object message) {

		if (message instanceof Action) {
			Action act = (Action) message;
			switch (act.getAction()) {
			case GameState:
				sendToAll(HubPokerTable);
				break;
			case TableState:
				resetOutput();
				sendToAll(HubPokerTable);
				break;
			case Sit:
				resetOutput();
				HubPokerTable.AddPlayerToTable(act.getPlayer());				
				sendToAll(HubPokerTable);				
				break;
			case Leave:
				resetOutput();
				HubPokerTable.RemovePlayerFromTable(act.getPlayer());
				sendToAll(HubPokerTable);				
				break;
				
			case StartGame:
				//System.out.println("Starting Game!");
				resetOutput();
				
				//TODO - Lab #5 Do all the things you need to do to start a game!!
				
				//	Determine which game is selected (from RootTableController)
				//		1 line of code
	
				Rule rle = new Rule(act.geteGame());
			
				Player p = HubPokerTable.PickRandomPlayerAtTable();
				System.out.println("Random Player: " + p.getiPlayerPosition());
				
				HubGamePlay = new GamePlay(rle, p);
				
				HubGamePlay.setGamePlayers(HubPokerTable.getHashPlayers());
				
				HubGamePlay.setGameDeck(new Deck(rle.GetNumberOfJokers(), rle.GetWildCards()));
				

				HubGamePlay.setiActOrder(GamePlay.GetOrder(p.getiPlayerPosition()));
				
				HubGamePlay.setPlayerNextToAct(HubGamePlay.ComputePlayerNextToAct(NextPosition(p.getiPlayerPosition(), GamePlay.GetOrder(p.getiPlayerPosition()))));

			
				
				//	Send the state of the game back to the players
				sendToAll(HubGamePlay);
				break;
			case Deal:
				
				/*
				int iCardstoDraw[] = HubGamePlay.getRule().getiCardsToDraw();
				int iDrawCount = iCardstoDraw[iDealNbr];

				for (int i = 0; i<iDrawCount; i++)
				{
					try {
						Card c = HubGamePlay.getGameDeck().Draw();
					} catch (DeckException e) {
						e.printStackTrace();
					}
				}
*/
				break;
			}
		}

		//System.out.println("Message Received by Hub");
	}

}