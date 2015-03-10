package action;

import java.io.IOException;
import java.util.Vector;

import MyObjects.CAllClientNames;
import MyObjects.CAllReady;
import MyObjects.CChoice;
import MyObjects.CClientInfo;
import MyObjects.CDesktopCards;
import MyObjects.CReady;
import MyObjects.CUserName;
import MyObjects.Confirm;
import MyObjects.OneCard;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class MyServer {
	private Server mServer;
	public CDesktopCards gDesktopCard;
	public Vector<Connection> mClient;
	private int mTempIndex;////�����ӵ�ʱ��������ʾ��ǰ���ӵ��û��ǵڼ���
	public MyServer(CDesktopCards desktop){
		gDesktopCard=desktop;
		mTempIndex = 0;
		mClient = new Vector<Connection>();
	}
	public void Init(){
		mServer = new Server();
	    Kryo kryo = mServer.getKryo();
	    kryo.register(SomeRequest.class);
	    kryo.register(SomeResponse.class);
	    kryo.register(CAllClientNames.class);
	    kryo.register(CAllReady.class);
	    kryo.register(CChoice.class);
	    kryo.register(CDesktopCards.class);
	    kryo.register(Confirm.class);
	    kryo.register(CReady.class);
	    kryo.register(CUserName.class);
	    kryo.register(OneCard.class);
	    mServer.start();
	    try {
			mServer.bind(54555, 54777);
		}
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    mServer.addListener(new Listener() {
	        public void received (Connection connection, Object object){
	           if (object instanceof SomeRequest){//���������õã�������ʱ�����ɾ��
	              SomeRequest request = (SomeRequest)object;
	              System.out.println(request.text);
	              SomeResponse response = new SomeResponse();
	              response.text = "Thanks";
	              connection.sendTCP(response);
	           }
	           else if(object instanceof CUserName){///�൱�����������ˣ���Ҫ֪ͨ���Լ�
	        	   CUserName tempUserName = (CUserName)object;
	        	   System.out.println(tempUserName.mUserName);
	        	   mClient.add(connection);
	        	   CClientInfo tempClientInfo = new CClientInfo();
	        	   tempClientInfo.mIndex=mTempIndex;
	        	   tempClientInfo.mUserName=tempUserName.mUserName;
	        	   gDesktopCard.mAllClients.add(tempClientInfo);
	        	   Confirm tempConfirm = new Confirm();
	        	   connection.sendTCP(tempConfirm);
	        	   System.out.println("Send Confirm success");
	        	   tempUserName.mIndex = mTempIndex++;
	        	   for(int i =0;i<mClient.size();i++){
	        		   mClient.get(i).sendTCP(tempUserName);
	        	   }
	           }
	           else if(object instanceof CReady){///�ͻ���ȷ��
	        	   CReady tempReady = (CReady)object;
	        	   int tempIndex = tempReady.mIndex;
	        	   gDesktopCard.mAllClients.get(tempIndex).mReady=tempReady.mReady;///������ȡ��׼��
	        	   for(int i =0;i<mClient.size();i++)///֪ͨ������
	        		   mClient.get(i).sendTCP(tempReady);
	        	   if(CheckAllReady()==1){////�����˶�׼�����˷��ƣ������ʼ���Ǹ��һ�Ҳ�൱�ڷ�����ready
	        		   gDesktopCard.Init(); 
	        		   for(int i =0;i<mClient.size();i++){
	        			   mClient.get(i).sendTCP(gDesktopCard.GenerateDesktopData(gDesktopCard, i));
	        		   }
	        		   /*����tempIndex*/mClient.get(tempIndex).sendTCP(gDesktopCard.GetSomeBodyOneCard(tempIndex));//��һ���Ƹ�ׯ��
	        		   for(int i =0;i<mClient.size();i++){
	        			   if(i!=tempIndex)
	        				   mClient.get(i).sendTCP(new OneCard(tempIndex,-1));///֪ͨ������ׯ��ȡ��һ����
	        		   }
	        		   CChoice choice = gDesktopCard.GenerateChoice(gDesktopCard.mAllCards.get(gDesktopCard.mCurrentCard), -1, tempIndex);
	        		   mClient.get(tempIndex).sendTCP(choice);
	        		   gDesktopCard.mExpectTotalChocieNum=1;///��ǰ���ڴ�ׯ�Ҵ��һ��choice
	        	   }
	           }
	           else if(object instanceof CChoice)
	           {
	        	   CChoice tempChoice  = (CChoice)object;
	               gDesktopCard.mTotalChoice.add(tempChoice);///��ʱ��ѡ�����û�а���index������
	               gDesktopCard.mExpectTotalChocieNum++;
	               if(gDesktopCard.mExpectTotalChocieNum==gDesktopCard.mTotalChoice.size())
	            	   ProcessChoice();
	           }
	           else{
	        	   System.out.println("Default");
	           }
	        }
	     });
	}
	public void ProcessChoice()
	{
		int hu = -1;///û�˺���
		int gang =-1;
		int peng = -1;
		int eat =-1;
		Vector<CChoice> temp = gDesktopCard.mTotalChoice;
		gDesktopCard.SortChoice();////��CChoice����CChoice.mIndex��С�����������
		int findHuPaiPosition=0;
		if(gDesktopCard.mCurrentPerson==3 ||temp.size()==1)///ֻ��һ���˵�choice�ʹ��Ƶ���������3�����////�ض���0��ʼ�Һ��Ƶ���
		{
			findHuPaiPosition = 0;
		}
		else{
			for(int i=0;i<temp.size();i++)///���ĸ��ط���ʼ�ң�i��ֵ����
			{
				if(temp.get(i).mIndex>gDesktopCard.mCurrentPerson)
				{
					findHuPaiPosition = i;
					break;///�ҵ�mCurrentPerson ���¼ң����¼ҿ�ʼ�Һ��Ƶ���
				}
			}
		}///���¼ҿ�ʼ���ĸ�������
		for(int j = 0;j<temp.size();j++)
		{
			if(temp.get(findHuPaiPosition).mHu==1)
			{
				hu = temp.get(findHuPaiPosition).mIndex;
				break;
			}
			findHuPaiPosition=findHuPaiPosition++%4;
		}
	     ///�����ĳһ����ȡ���ˣ���ֻ�ж����Լ��������ĳһ���˴���һ���ƣ��������¼ҿ�ʼ�����Լ������ܵ�choice����
		for(int i=0;i<temp.size();i++)
		{
			if(temp.get(i).mGang==1)
			{
				gang=temp.get(i).mIndex;
			}
			else if(temp.get(i).mPeng==1)
			{
				peng =temp.get(i).mIndex;///����ѡ�������ƣ���¼����mIndex;
				break;
			}
			else if(temp.get(i).mEat==1)
			{
				eat = temp.get(i).mIndex;//���˳��ƣ���¼����mIndex;
			}
		}
		if(hu!=-1)
		{
			gDesktopCard.ProcessHuPai(hu);
		}
		else if(gang!=-1)
		{
			gDesktopCard.ProcessGang(gang,temp.get(gang).mGangCards.get(0));///���ƵĻ�������ı������Ψһ��һ��Ԫ��
		}
		else if(peng!=-1)
		{
			gDesktopCard.ProcessPeng(peng);
		}
		else if(eat!=-1)
		{
			gDesktopCard.ProcessEat(eat, gDesktopCard.mTotalChoice.get(eat).mEatCards);
		}
		gDesktopCard.mExpectTotalChocieNum=0;
		gDesktopCard.mTotalChoice.clear();///���������ϣ��ȴ���������
	}
	public int CheckAllReady(){
	   int mAllReady=0;
 	   if(mClient.size()==4){
 		   mAllReady = 1;
 		   for(int i =0;i<mClient.size();i++){
 			   if(gDesktopCard.mAllClients.get(i).mReady==0){////��һ����û��׼���þ���û׼����
 				   mAllReady = 0;
 				   break;
 			   }
 		   }
 	   }
	   return mAllReady;
	}
}
