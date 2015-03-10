package MyObjects;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

public class CDesktopCards implements Cloneable {
	public int mCurrentCard;
	public int mTotalLeftCard;
	public int mEndCard;
	public int mCurrentPerson;
	public int mPreWinner;
	public int mExpectTotalChocieNum;//当前期待几个玩家的choice，可能是1，2，3，4
	public Vector<CChoice> mTotalChoice;///用于缓存当前所有客户端发送回来的CChoice;
	//public int mReady;
	public Vector<CClientInfo> mAllClients;
	public Vector<Integer>    mAllCards;
	public CWinRulers mRulers;
	public CDesktopCards(){
		mCurrentCard = 0;
		mTotalLeftCard = 0;
		mEndCard =0;
		mCurrentPerson =0;
		mPreWinner = 0;
		mAllClients = new Vector<CClientInfo>();
		mTotalChoice = new Vector<CChoice>();
		mAllCards = new   Vector<Integer>();
	}
	public Object clone()
	{
		CDesktopCards o =null;
		try {
			o =(CDesktopCards)super.clone();
		}
		catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return o;
	}
	public boolean   Init()
	{
		Random TempRandom = new Random();
		mCurrentCard =TempRandom.nextInt(136);
		mEndCard = (mCurrentCard+135)%136;
		mCurrentPerson = mPreWinner;
		for(int i =0;i<136;i++)
			mAllCards.add(i);
		Collections.shuffle(mAllCards);
		for(int i = 0;i<mAllClients.size();i++)
			mAllClients.get(i).Init();
		for(int i =0;i<52;i++)
		{
			int temp = (int) mAllCards.get(mCurrentCard++%136);
			mAllClients.get(i/13).mHandCards.add(temp);
		}
		mCurrentCard=mCurrentCard%136;
		mTotalLeftCard=84;///剩余136-52张
		return true;
	}
	public CDesktopCards GenerateDesktopData(CDesktopCards inObject,int index)
	{
		CDesktopCards temp= (CDesktopCards)inObject.clone();
		for(int i =0;i<temp.mAllClients.size();i++)
		{
			if (i!=index)
				temp.mAllClients.get(i).mHandCards.clear();
		}
		return temp;///返回去除index所指的客户之外的所有玩家的手牌信息，发送给index所指的用户
	}
	public CChoice GenerateChoice(int Card,int index,int ownIndex)///card表明index所指的对象打的那张牌
	{
		CChoice temp = new CChoice();
		CWinRulers ruler  = new CWinRulers(mAllClients.get(ownIndex).mHandCards);
		temp.mGangCards= ruler.CheckGang(ruler.mCardsToBeCheck,Card,index,ownIndex);
		temp.mPeng     = ruler.CheckPeng(ruler.mCardsToBeCheck, Card,index,ownIndex);
		temp.mEatCards = ruler.CheckEat(ruler.mCardsToBeCheck, Card, index, ownIndex);
		temp.mHu       = ruler.CheckHu(ruler.mCardsToBeCheck);
		if(temp.mGangCards.size()>0) temp.mGang=1;
		if(temp.mPeng==1)  temp.mPengCards.add(Card);
		if(temp.mEatCards.size()>0)temp.mEat=1;
		temp.mIndex = ownIndex;
		return temp;
	}
	public OneCard GetSomeBodyOneCard(int index)
	{
		OneCard temp = new OneCard();
		mCurrentCard=mCurrentCard++%136;
		mTotalLeftCard--;
		temp.mIndex=index;
		temp.mCard = mAllCards.get(mCurrentCard);
		mAllClients.get(index).mHandCards.add(temp.mCard);
		return temp;
	}
	public void ProcessHuPai(int index)
	{
		///需要算翻数
	}
	public void ProcessPeng(int index)
	{
		mCurrentPerson = index;///当前出牌的人指定为它自己
	}
	public void ProcessGang(int index,int Card)//可能有多个杠，这里Card指明玩家杠了哪一个
	{
		
	}
	public void ProcessEat(int index,Vector<Integer>data)
	{
		
	}
	public void SortChoice(){///index为当前打出牌的人的索引，以它的下家为起点进行排序，主要用于多个人选择胡牌，确定哪个是最前面的
		@SuppressWarnings("unchecked")
		Comparator<Object> ct = new MyCompare();
		Collections.sort(mTotalChoice,ct);
	}
}
class MyCompare implements Comparator<Object> //实现Comparator，定义自己的比较方法

{
	public int compare(Object o1, Object o2) {
		CChoice e1=(CChoice)o1;
		CChoice e2=(CChoice)o2;
		if(e1.mIndex > e2.mIndex)//这样比较是降序,如果把-1改成1就是升序.
		{
			return 1;
		}
		else if(e1.mIndex<e2.mIndex)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
}
