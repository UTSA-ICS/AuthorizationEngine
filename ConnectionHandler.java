import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//this class basically handles all the connections which contains the requests
public class ConnectionHandler extends Thread {

	Socket s;
	PrintWriter pw; //for sending the output to client
	BufferedReader br; //for getting input from client 
	
	
	//constructor of the class which accepts a socket as parameter
	public ConnectionHandler (Socket s) throws Exception{
		this.s=s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw = new PrintWriter(s.getOutputStream());
		
	}
	
	//thread class contains a method run which is called automatically when we start a thread
	//in this method, we have to read the request and give the response
	@Override
	public void run(){
		try{
			String reqS = ""; //we get the request string and give it to HttpRequest class
		
		//from br we have to read our request
		//read until request is not get or br is ready
		while(br.ready() || reqS.length() == 0){
			reqS+= (char) br.read();
		}
		System.out.println(reqS);  //for displaying the request
		HttpRequest req = new HttpRequest(reqS);
		
		//now we need to pass the httprequest object to httpresponse class for getting response
		HttpResponse res = new HttpResponse(req);
		
		//write final output to pw
		pw.write(res.response.toCharArray());

		pw.close();
		br.close();
		s.close();
		
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
	}
}
