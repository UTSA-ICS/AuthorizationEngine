package gov.nist.csd.pm.application.HttpServer;

import java.util.HashMap;

public class Main {
	public static int port = 9000;
	private static PMApp pmapp = null;
	private static String hst;
	private static int prt;

	public  HashMap<String, String> SessIDMap = new HashMap<String, String>();
	
	public void addInMap( String key, String value) {
        SessIDMap.put(key, value);
   }
	
	public String getFromMap(String key){
		return SessIDMap.get(key);
	}
	
	public  boolean checkMap(String key){
		return SessIDMap.containsKey(key);
	}

	public static void main(String[] args) {
	
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-enginehost")) {
				hst = args[++i];
			} else if (args[i].equals("-engineport")) {
				prt = Integer.valueOf(args[++i]).intValue();
			}
		}
		
	
		
		//start PM App
		startPMApp();
		// start http server
		SimpleHttpServer httpServer = new SimpleHttpServer(pmapp);
		httpServer.Start(port);


//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				startPMApp();
//			}
//		});
	}

	private static void startPMApp() {
		pmapp = new PMApp();
		pmapp.initialize(hst, prt);
	}

	public static PMApp getPMapp() {
		return pmapp;
	}
}
