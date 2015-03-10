package MyObjects;

import java.util.Vector;

public class CChoice {
	public int mIndex;
	public int mHu;
	public int mGang ;
	public int mEat;
	public int mGeneralPost;
	public int mPeng;
	public int mQuit;
	public int mAgreeQuit;
	public Vector<Integer> mCards;
	public Vector<Integer> mGangCards;///
	public Vector<Integer> mEatCards;
	public Vector<Integer> mPengCards;
	public void CChoice(){
		mIndex = 0;
		mGang =0;
		mEat = 0;
		mGeneralPost=0;
		mPeng=0;
		mQuit=0;
		mAgreeQuit=0;
		mCards     = new Vector<Integer>();
		mGangCards = new Vector<Integer>();
		mEatCards  = new Vector<Integer>();
		mPengCards = new Vector<Integer>();
	}
}
