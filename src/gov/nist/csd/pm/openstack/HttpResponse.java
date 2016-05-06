import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class HttpResponse {

	HttpRequest req;
	
	String response;// final response which get generated
	String root = "C:/Users/Smriti/Downloads/Compressed/eclipse-SDK-3.3-win32-x86_64/root";
	// root path of the server, which is nothing but the folder which contains all the file
	
	
	public HttpResponse(HttpRequest request){
		req = request;
		//now open the file mentioned in the request
		File f = new File(root +req.filename); 
		
		// to read this file
		try {
			FileInputStream fis = new FileInputStream(f);
			/*
			 * HTTP/1.1 200 OK
				Date: Mon, 27 Jul 2009 12:28:53 GMT
				Server: Apache/2.2.14 (Win32)
				Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT
				Content-Length: 88
				Content-Type: text/html
				Connection: Closed
			 */
			
			response = "HTTP/1.1 200 \r\n"; //version of http and 200 for status code
			//200 means all good
			response = response + "Server: Our Java Server/1.0 \r\n"; //identity of the server
			
			response += "Content-Type: text/html  \r\n"; //response is in http format
			
			//this line tells the browser that close the connection
			//no future transmission will take place in this connection
			response+=  "Connection: close \r\n";
			
			response += "Content-Length: " + f.length() +" \r\n"; //length of response
			
			response += "\r\n"; //after blank line need to append file data
	
			int s;
			while((s = fis.read())  !=  -1){  //means end of file
				response += (char) s;
			}
			fis.close();
		} 
		catch (FileNotFoundException e){
			//if we don't get the file then error 404
			//e.printStackTrace();
			response = response.replace("200", "404");
			
		}
		catch (Exception e) {
			//e.printStackTrace();
			//if any other error then 500 internal server error
			response = response.replace("200", "500");
	}
	}
	
}
