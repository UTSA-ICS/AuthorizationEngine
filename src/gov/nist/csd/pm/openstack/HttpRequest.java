
public class HttpRequest {

	String filename;
	
	//we have to create a constructor that accepts a string
	public HttpRequest(String request) {
		//after getting the request, first line is important
		//first line contains 3 parts
		//1- request type, 2- file name, 3-http version
		
		String lines[] = request.split("\n"); //get all the lines of request separately
		
		System.out.println(lines[0]);
		lines = lines[0].split(" ");
		filename = lines[1];
		
		System.out.println(filename);

		//split first line using space and selects the second item
		//which is our filename '/'
			
	}

}
