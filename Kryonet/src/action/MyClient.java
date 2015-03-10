package action;

import java.io.IOException;
import java.net.InetAddress;

import MyObjects.CAllClientNames;
import MyObjects.CAllReady;
import MyObjects.CChoice;
import MyObjects.CDesktopCards;
import MyObjects.CReady;
import MyObjects.CUserName;
import MyObjects.Confirm;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class MyClient {
	public Client mClient;
	public void Init()
	{
		mClient= new Client();
	    InetAddress address = mClient.discoverHost(54777, 5000);
	    //if(address.isAnyLocalAddress()==false)
	    //	return ;
		Kryo kryo = mClient.getKryo();
	    kryo.register(SomeRequest.class);
	    kryo.register(SomeResponse.class);
	    kryo.register(CAllClientNames.class);
	    kryo.register(CAllReady.class);
	    kryo.register(CChoice.class);
	    kryo.register(CDesktopCards.class);
	    kryo.register(Confirm.class);
	    kryo.register(CReady.class);
	    kryo.register(CUserName.class);
	    mClient.start();
	    try {
			mClient.connect(5000, "127.0.0.1", 54555, 54777);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    CUserName request = new CUserName();
	    request.mUserName = "Here is the username";
	    mClient.addListener(new Listener() {
	        public void received (Connection connection, Object object) {
	           if (object instanceof Confirm) {
	        	  Confirm response = (Confirm)object;
	              System.out.println(response.mIndex+"9999");
	           }
	        }
	     });
	    System.out.println("addListener success");
	    mClient.sendTCP(request);
	    mClient.run();
	   
	}
}
