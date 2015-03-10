package MyObjects;

import java.util.Vector;

public class CAllClientNames {

	public Vector<CUserName> AllClientNames;///当有用户连上的时候，需要广播给所有人
	public CAllClientNames()
	{
		AllClientNames = new Vector<CUserName>();
	}
}
