package MyObjects;

import java.util.Vector;

public class CClientInfo {
	public int mIndex;
	public Vector<Integer>    mHandCards;
	public Vector<Integer>    mCardsHavePickOut;
	public Vector<Integer>    mCardsEat;
	public Vector<Integer>    mCardsPeng;
	public Vector<Integer>    mGang;
	public String    mUserName;
	public int       mReady;/// «∑ÒReady
	public void Init()
	{
		mHandCards.clear();
		mCardsHavePickOut.clear();
		mCardsEat.clear();
		mCardsPeng.clear();
		mGang.clear();
		mUserName="";
		mReady =0;
	}
}
