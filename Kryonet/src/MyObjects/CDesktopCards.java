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
	public int mExpectTotalChocieNum;//��ǰ�ڴ�������ҵ�choice��������1��2��3��4
	public Vector<CChoice> mTotalChoice;///���ڻ��浱ǰ���пͻ��˷��ͻ�����CChoice;
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
		mTotalLeftCard=84;///ʣ��136-52��
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
		return temp;///����ȥ��index��ָ�Ŀͻ�֮���������ҵ�������Ϣ�����͸�index��ָ���û�
	}
	public CChoice GenerateChoice(int Card,int index,int ownIndex)///card����index��ָ�Ķ�����������
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
		///��Ҫ�㷭��
	}
	public void ProcessPeng(int index)
	{
		mCurrentPerson = index;///��ǰ���Ƶ���ָ��Ϊ���Լ�
	}
	public void ProcessGang(int index,int Card)//�����ж���ܣ�����Cardָ����Ҹ�����һ��
	{
		
	}
	public void ProcessEat(int index,Vector<Integer>data)
	{
		
	}
	public void SortChoice(){///indexΪ��ǰ����Ƶ��˵��������������¼�Ϊ������������Ҫ���ڶ����ѡ����ƣ�ȷ���ĸ�����ǰ���
		@SuppressWarnings("unchecked")
		Comparator<Object> ct = new MyCompare();
		Collections.sort(mTotalChoice,ct);
	}
}
class MyCompare implements Comparator<Object> //ʵ��Comparator�������Լ��ıȽϷ���

{
	public int compare(Object o1, Object o2) {
		CChoice e1=(CChoice)o1;
		CChoice e2=(CChoice)o2;
		if(e1.mIndex > e2.mIndex)//�����Ƚ��ǽ���,�����-1�ĳ�1��������.
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
