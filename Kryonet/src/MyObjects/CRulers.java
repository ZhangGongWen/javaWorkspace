package MyObjects;
import java.util.Vector;
public abstract interface CRulers {

	/*ע:0-8Ͳ��10-18����20-28�򣬶��������ֱ��Ӧ30��40��50��60���з��׶�Ӧ70��80��90*/
		public abstract int CheckHu(Vector<Integer> data);
		public abstract Vector<Integer>CheckGang(Vector<Integer> data,int card,int currentPerson,int ownIndex);///�����ж�����Ը�
		public abstract int  CheckPeng(Vector<Integer> data,int card,int currentPerson,int ownIndex);
		public abstract Vector<Integer>CheckEat(Vector<Integer>data,int card,int currentPerson,int ownIndex);///ֻ�ܳ��ϼҵ���
		public abstract Vector<Integer>FindAvalableJiangPosition(Vector<Integer>data);
		public abstract int CheckRemainCards(Vector<Integer>data);
		public abstract int FindNextAvaible(boolean Scaned[],Vector<Integer>data,int begin);
}
