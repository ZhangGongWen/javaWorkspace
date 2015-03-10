package MyObjects;
import java.util.Vector;
public abstract interface CRulers {

	/*注:0-8筒，10-18条，20-28万，东南西北分别对应30，40，50，60，中发白对应70，80，90*/
		public abstract int CheckHu(Vector<Integer> data);
		public abstract Vector<Integer>CheckGang(Vector<Integer> data,int card,int currentPerson,int ownIndex);///可能有多个可以杠
		public abstract int  CheckPeng(Vector<Integer> data,int card,int currentPerson,int ownIndex);
		public abstract Vector<Integer>CheckEat(Vector<Integer>data,int card,int currentPerson,int ownIndex);///只能吃上家的牌
		public abstract Vector<Integer>FindAvalableJiangPosition(Vector<Integer>data);
		public abstract int CheckRemainCards(Vector<Integer>data);
		public abstract int FindNextAvaible(boolean Scaned[],Vector<Integer>data,int begin);
}
