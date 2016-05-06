import java.net.ServerSocket;
import java.net.Socket;


public class ServerMain {

	ServerSocket serverSocket;
	//entry point of our program
	public static void main(String[] args) throws Exception {
			new ServerMain().runServer();  //to avoid any problems with static fields
	
	}
	
	public void runServer() throws Exception{
		System.out.println("Server Started\n");
		serverSocket = new ServerSocket(6543);
		
		acceptRequests(); //for accepting the request
	}
	
	private void acceptRequests() throws Exception{
		while(true){  //need to accept all the requests
			//connection to client is in the form of socket which contain the stream of input and output
			Socket s = serverSocket.accept();
			ConnectionHandler ch = new ConnectionHandler(s);
			
			//ch is a thread, so we have to start the thread
			ch.start();   // this will call the run method automatically
			
		}
	}

}
