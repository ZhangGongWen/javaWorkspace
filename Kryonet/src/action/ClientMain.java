package action;





public class ClientMain {
	public static void  main(String[] args){
		MyClient client = new MyClient();
		try {
			client.Init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
