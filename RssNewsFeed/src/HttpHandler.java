import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.File;
import java.io.FileWriter;

public class HttpHandler {
	private final String HTTPHANDLER = this.getClass().getSimpleName();
	
	private SharedData sharedDataObj = SharedData.getInstance();
			
	public HttpHandler() {
		System.out.println(HTTPHANDLER+"():Object Creation");
		
		int type = sharedDataObj.getType();
		sharedDataObj.getCatList().get(type).getfName();
	}
	   
	public void makeServiceCall(String reqUrl) throws IOException {
		System.out.println(HTTPHANDLER+".makeServiceCall():called");
		String response = null;
	      
		System.out.println(HTTPHANDLER+".makeServiceCall():"+reqUrl);
		try {
			URL url = new URL(reqUrl);
			System.out.println("makeServiceCall():connecting....");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			System.out.println("makeServiceCall():response recvd....");
			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());
			response = convertStreamToString(in);
			writeToFile(response);
			} catch (MalformedURLException e) {
				System.out.println("MalformedURLException: " + e.getMessage());
				System.exit(0);
			} catch (ProtocolException e) {
	    	  System.out.println("ProtocolException: " + e.getMessage());
	    	  System.exit(0);
			} catch (Exception e) {
	    	  System.out.println("Exception: " + e.getCause().toString());
	    	  System.exit(0);
			}	      
	}
	   
		
	private void writeToFile(String str){
		System.out.println(HTTPHANDLER+".writeToFile():called");
		try {
			
			int type = sharedDataObj.getType();
			String fileName = sharedDataObj.getCatList().get(type).getfName();
			File file = new File(fileName);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(str);
			fileWriter.flush();
			fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line;
		System.out.println(HTTPHANDLER+"convertStreamToString():called");
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
	        
		return sb.toString();
	}
}
