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
	private int mTempIndex;////在连接的时候用来表示当前连接的用户是第几个
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
	           if (object instanceof SomeRequest){//测试网络用得，发布的时候可以删除
	              SomeRequest request = (SomeRequest)object;
	              System.out.println(request.text);
	              SomeResponse response = new SomeResponse();
	              response.text = "Thanks";
	              connection.sendTCP(response);
	           }
	           else if(object instanceof CUserName){///相当于有人连上了，需要通知它自己
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
	           else if(object instanceof CReady){///客户的确认
	        	   CReady tempReady = (CReady)object;
	        	   int tempIndex = tempReady.mIndex;
	        	   gDesktopCard.mAllClients.get(tempIndex).mReady=tempReady.mReady;///可能是取消准备
	        	   for(int i =0;i<mClient.size();i++)///通知所有人
	        		   mClient.get(i).sendTCP(tempReady);
	        	   if(CheckAllReady()==1){////所有人都准备好了发牌，点击开始的那个家伙也相当于发送了ready
	        		   gDesktopCard.Init(); 
	        		   for(int i =0;i<mClient.size();i++){
	        			   mClient.get(i).sendTCP(gDesktopCard.GenerateDesktopData(gDesktopCard, i));
	        		   }
	        		   /*这里tempIndex*/mClient.get(tempIndex).sendTCP(gDesktopCard.GetSomeBodyOneCard(tempIndex));//发一张牌给庄家
	        		   for(int i =0;i<mClient.size();i++){
	        			   if(i!=tempIndex)
	        				   mClient.get(i).sendTCP(new OneCard(tempIndex,-1));///通知其他人庄家取了一张牌
	        		   }
	        		   CChoice choice = gDesktopCard.GenerateChoice(gDesktopCard.mAllCards.get(gDesktopCard.mCurrentCard), -1, tempIndex);
	        		   mClient.get(tempIndex).sendTCP(choice);
	        		   gDesktopCard.mExpectTotalChocieNum=1;///当前就期待庄家打出一个choice
	        	   }
	           }
	           else if(object instanceof CChoice)
	           {
	        	   CChoice tempChoice  = (CChoice)object;
	               gDesktopCard.mTotalChoice.add(tempChoice);///此时的选择对象没有按照index来排序
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
		int hu = -1;///没人胡牌
		int gang =-1;
		int peng = -1;
		int eat =-1;
		Vector<CChoice> temp = gDesktopCard.mTotalChoice;
		gDesktopCard.SortChoice();////将CChoice按照CChoice.mIndex从小到大进行排序
		int findHuPaiPosition=0;
		if(gDesktopCard.mCurrentPerson==3 ||temp.size()==1)///只有一个人的choice和打牌的人索引是3的情况////必定是0开始找胡牌的人
		{
			findHuPaiPosition = 0;
		}
		else{
			for(int i=0;i<temp.size();i++)///从哪个地方开始找，i的值就是
			{
				if(temp.get(i).mIndex>gDesktopCard.mCurrentPerson)
				{
					findHuPaiPosition = i;
					break;///找到mCurrentPerson 的下家，从下家开始找胡牌的人
				}
			}
		}///从下家开始找哪个胡牌了
		for(int j = 0;j<temp.size();j++)
		{
			if(temp.get(findHuPaiPosition).mHu==1)
			{
				hu = temp.get(findHuPaiPosition).mIndex;
				break;
			}
			findHuPaiPosition=findHuPaiPosition++%4;
		}
	     ///如果是某一个人取牌了，就只判断她自己，如果是某一个人打了一张牌，从它的下家开始，它自己不会受到choice对象
		for(int i=0;i<temp.size();i++)
		{
			if(temp.get(i).mGang==1)
			{
				gang=temp.get(i).mIndex;
			}
			else if(temp.get(i).mPeng==1)
			{
				peng =temp.get(i).mIndex;///有人选择了碰牌，记录它的mIndex;
				break;
			}
			else if(temp.get(i).mEat==1)
			{
				eat = temp.get(i).mIndex;//有人吃牌，记录它的mIndex;
			}
		}
		if(hu!=-1)
		{
			gDesktopCard.ProcessHuPai(hu);
		}
		else if(gang!=-1)
		{
			gDesktopCard.ProcessGang(gang,temp.get(gang).mGangCards.get(0));///杠牌的话此数组的必须包含唯一的一个元素
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
		gDesktopCard.mTotalChoice.clear();///清空这个集合，等待后续处理
	}
	public int CheckAllReady(){
	   int mAllReady=0;
 	   if(mClient.size()==4){
 		   mAllReady = 1;
 		   for(int i =0;i<mClient.size();i++){
 			   if(gDesktopCard.mAllClients.get(i).mReady==0){////有一个人没有准备好就是没准备好
 				   mAllReady = 0;
 				   break;
 			   }
 		   }
 	   }
	   return mAllReady;
	}
}
