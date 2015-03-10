package action;

import java.util.Random;

import MyObjects.CDesktopCards;

//import MyObjects.CDesktopCards;

public class ServerMain {
	public static void  main(String[] args){
		CDesktopCards  gDesktopCard = new CDesktopCards();
		Random tempRand= new Random();
		gDesktopCard.mPreWinner=tempRand.nextInt(4);
		gDesktopCard.mCurrentPerson=gDesktopCard.mPreWinner;
		MyServer server = new MyServer(gDesktopCard);
		server.Init();
		System.out.println("Hello World");
	}
}
