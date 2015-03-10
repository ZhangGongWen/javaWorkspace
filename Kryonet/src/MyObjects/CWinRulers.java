package MyObjects;

import java.util.Vector;
/*注:0-8筒，10-18条，20-28万，东南西北分别对应30，40，50，60，中发白对应70，80，90*/
public class CWinRulers implements CRulers {
	public Vector<Integer> mCardsToBeCheck;
	public CWinRulers()
	{
		mCardsToBeCheck = new Vector<Integer>();
	}
	public CWinRulers(Vector<Integer> data)
	{
		mCardsToBeCheck = new Vector<Integer>();
		for(int i=0;i<data.size();i++)
		{
			if(data.get(i)<36){///筒
				mCardsToBeCheck.add(data.get(i)/4);
			}
			else if(data.get(i)<72){///条
				mCardsToBeCheck.add(data.get(i)/4+1);
			}
			else if(data.get(i)<108){///万
				mCardsToBeCheck.add(data.get(i)/4+2);
			}
			else if(data.get(i)<112){//东
				mCardsToBeCheck.add(30);
			}
			else if(data.get(i)<116){//南
				mCardsToBeCheck.add(40);
			}
			else if(data.get(i)<120){//西
				mCardsToBeCheck.add(50);
			}
			else if(data.get(i)<124){//北
				mCardsToBeCheck.add(60);
			}
			else if(data.get(i)<128){///中
				mCardsToBeCheck.add(70);
			}
			else if(data.get(i)<132){//发
				mCardsToBeCheck.add(80);
			}
			else{////////////////白
				mCardsToBeCheck.add(90);
			}
		}
		mCardsToBeCheck.sort(null);
	}
	public int CheckHu(Vector<Integer> data)
	{
		mCardsToBeCheck.sort(null);
		Vector<Integer> Jiang = FindAvalableJiangPosition(data);
		int size = Jiang.size();
		int i = 0;
		Vector<Integer> temp = new Vector<Integer>();
		int start = 0;
		if(size==0)
			return 0;
		for(i = 0;i<size;i++)
		{
			temp=(Vector<Integer>) data.clone();
			start = Jiang.get(i);
			temp.remove(start);
			temp.remove(start);
			if(CheckRemainCards(temp)==1)
				return 1;///胡牌了
		}
		return 0;///没胡牌
	}
	public Vector<Integer>CheckGang(Vector<Integer> data,int card,int currentPerson,int ownIndex)///可能有多个可以杠
	{
		Vector<Integer> temp = new Vector<Integer>();
		if(currentPerson==ownIndex)///当前取牌的人取牌之后，判断手牌是否可以杠，
		{
			int size = data.size();
			if(size==0)
				return null;
			int pre =data.get(0);
			int i =0;
			while(i<size)
			{
				if(i+3<size && pre ==data.get(i+1) && pre ==data.get(i+2)&&pre == data.get(i+3))
				{
					temp.add(pre);
					i = i +4;///移向下一个card
					if(i<size)
					pre=data.get(i);
				}
				else{
					i++;
				}
			}
			return temp;
		}
		else////别人打了一张牌，其他人判断是否可以杠
		{
			int position = data.indexOf(card);
			if(position!=-1)
			{
				if(position+2<data.size() && data.get(position+1)==card && data.get(position+2)==card)
				{
					temp.add(card);
					return temp;
				}
			}
			return null;//没有杠的
		}
	}
	public int  CheckPeng(Vector<Integer> data,int card,int currentPerson,int ownIndex){
		if(currentPerson==ownIndex)///一个人取牌之后，判断自己的手牌
			return 0;
		int position = data.indexOf(card);///某一个人打牌之后其他人是否可以碰
		if(position!=-1)
		{
			if(position+1<data.size() && data.get(position+1)==card)
				return 1;
		}
		return 0;
	}
	public Vector<Integer>CheckEat(Vector<Integer>data,int card,int currentPerson,int ownIndex)///只能吃上家的牌
	{
		if(currentPerson==-1)///表明一个人已经取牌到手上
			return null;
		if((currentPerson+1)%4!=ownIndex)///不是上一家打的牌
			return null;
		Vector<Integer> ret = new Vector<Integer>();
		int positionHight1  = data.indexOf(card+1);
		int positionHight2  = data.indexOf(card+2);
		int positionLow1     = data.indexOf(card-1);
		int positionLow2    = data.indexOf(card-2);
		if(positionLow1!=-1 && positionLow2 !=-1)
		{
			ret.add(card-2);
			ret.add(card-1);
		}
		if(positionLow1!=-1 && positionHight1 !=-1)
		{
			ret.add(card-1);
			ret.add(card+1);
		}
		if(positionHight1!=-1 && positionHight2 !=-1)
		{
			ret.add(card+1);
			ret.add(card+2);
		}
		return ret;
	}
	public Vector<Integer>FindAvalableJiangPosition(Vector<Integer>data)
	{
		Vector<Integer> ret = new Vector<Integer>();
		int i=0;
		int size = data.size();
		if(size==0)
			return null;
		int pre = data.get(0);
		for(i =0;i<size;)
		{
			if((i+1<size) && pre ==data.get(i+1))
			{
				ret.add(i);
				pre = data.get(i);
				i++;
				do{
					i++;
				}while(i<size && pre==data.get(i));
				if(i<size)
				{
					pre = data.get(i);
				}
			}
			else
			{
				if(++i<size)
					pre =data.get(i);
			}
		}
		return ret;
	}
	public int CheckRemainCards(Vector<Integer>data)
	{
		int pre = 0;
		int size =data.size();
		int current =0;
		int status =0;
		int Index =0;
		boolean  HaveScaned[] =new boolean[14];
		for(int i =0;i<size;i++)
			HaveScaned[i] = true;
		while(true)
		{
			switch(status)
			{
			case 0:
				Index = FindNextAvaible(HaveScaned,data,0);
				if(Index==-1)return 1;
				pre = data.get(Index);
				status =1;
				break;
			case 1:
				Index = FindNextAvaible(HaveScaned,data,Index);
				if(Index==-1)return 0;
				
				if(pre==data.get(Index))
				{
					status =2;
					pre = data.get(Index);
				}
				else if((pre+1)==data.get(Index))
				{
					status =3;
					pre = data.get(Index);
				}
				else
					return 0;
				break;
			case 2:
				int temp = Index;
				Index = FindNextAvaible(HaveScaned,data,Index);
				if(Index==-1)return 0;
				if(pre==data.get(Index))
					status=0;
				else if((pre+1)==data.get(Index))
				{
					status=3;
					HaveScaned[temp] =true;
					pre = data.get(Index);
				}
				else
					return 0;
				break;
			case 3:
				Index =FindNextAvaible(HaveScaned,data,Index);
				if(Index==-1)
					return 0;
				if(pre+1==data.get(Index))
					status =0;
				else if(pre==data.get(Index))
				{
					HaveScaned[Index]=true;///将当前的那张牌退回去
					int i = Index;
					boolean temp2[] = new boolean[14];
					for(int j =0;j<14;j++)
						temp2[j] = HaveScaned[j];
					i = FindNextAvaible(temp2,data,i);
					while(i!=-1 && data.get(i)==pre)
						i = FindNextAvaible(temp2,data,i);
					if(i==-1)return 0;
					if(data.get(i)==pre+1)
					{
						HaveScaned[i] = false;
						status =0;
					}
					else
						return 0;
				}
				break;
			}
		}
	}
	public int FindNextAvaible(boolean Scaned[],Vector<Integer>data,int begin)
	{
		int i =0;
		for(i =begin;i<data.size();i++)
		{
			if(Scaned[i]==true)
			{
				Scaned[i]=false;
				return i ;
			}
		}
		return -1;
	}
}
