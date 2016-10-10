package gov.nist.csd.pm.application.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.*;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handlers {
	public static class RootHandler implements HttpHandler {

		// @Override
		public void handle(HttpExchange he) throws IOException {
			String response = "<h1>Server start success ... </h1>" + "<h1>Port: " + Main.port + "</h1>";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public static class EchoHeaderHandler implements HttpHandler {

		// @Override
		public void handle(HttpExchange he) throws IOException {
			Headers headers = he.getRequestHeaders();
			Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
			String response = "";
			for (Map.Entry<String, List<String>> entry : entries) // iterator
																	// for the
																	// headers
				response += entry.toString() + "\n";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}

	public static class EchoGetHandler implements HttpHandler {
		private PMApp pmapp = null;

		public EchoGetHandler(PMApp pmapp) {
			this.pmapp = pmapp;
		}

		// @Override
		public void handle(HttpExchange he) throws IOException {
			// parse request
			Map<String, Object> parameters = new HashMap<String, Object>();
			URI requestedUri = he.getRequestURI();
			boolean policyCheck = false;
			String response = "{\"policy\":false}";

			// to parse the data in the request body - username, action, or
			// token itself
			InputStream is = he.getRequestBody();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			String data = sb.toString();

			//System.out.println("Is there any data in the request body: " + data);
			//System.out.println(data.substring(0));

			if (data.substring(0).isEmpty()) {
				//System.out.println("Request body empty");
			} else {
				try {
					JSONObject js = new JSONObject(data);
					//System.out.println("JSONObject created");
					String kv1 = js.getString("user");
					//System.out.println(kv1);
					
					String kv2 = js.getString("roles");
//					Object obj = js.get("roles");
//					JSONArray array = (JSONArray)obj;
//					System.out.println(obj.get("roles"));
//					String[] kv2 = new String[array.length()];
//					for (int i=0;i<array.length();i++){
//						kv2[i] = (String) array.get(i);
//					}		
					//System.out.println(kv2);
					String kv3 = js.getString("objectName");
					//System.out.println(kv3);

					policyCheck = this.pmapp.checkPermissionForRoleAndAttr(kv1,  kv2, kv3);
					if (policyCheck)
						response = "{\"policy\":true}";

				} catch (JSONException e1) {
					//System.out.println("JSON exception rise");
					e1.printStackTrace();
				}
			}

			// end
			String query = requestedUri.getRawQuery();
			parseQuery(query, parameters);

			// send response
			for (String key : parameters.keySet())
				response += key + " = " + parameters.get(key) + "\n";
			he.sendResponseHeaders(200, response.length());

			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			//System.out.println("This is the response of parameters passed  = " + response.toString());
			//System.out.println();
			//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$     END    $$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			//System.out.println();
			os.close();
			rd.close();
			is.close();
		}

	}

	public static class EchoPostHandler implements HttpHandler {

		// @Override
		public void handle(HttpExchange he) throws IOException {
			//System.out.println("Served by /echoPost handler...");
			// parse request
			Map<String, Object> parameters = new HashMap<String, Object>();
			InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
			parseQuery(query, parameters);
			// send response
			String response = "";
			for (String key : parameters.keySet())
				response += key + " = " + parameters.get(key) + "\n";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();

		}
	}

	@SuppressWarnings("unchecked")
	public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);
					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}
}
