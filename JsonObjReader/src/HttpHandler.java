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

	   //private static final String TAG = HttpHandler.class.getSimpleName();
	   private String fileName;

	   public HttpHandler() {
		   fileName = null;
	   }
	   
	   public void setFileName(String name) {
		   fileName = name;
		   System.out.println("setFileName()"+fileName);
	   }
	   
	   public void makeServiceCall(String reqUrl) throws IOException {
	      String response = null;
	      
	      System.out.println("makeServiceCall():"+reqUrl);
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
	      } catch (ProtocolException e) {
	    	  System.out.println("ProtocolException: " + e.getMessage());
	      } catch (Exception e) {
	    	  System.out.println("Exception: " + e.getCause().toString());
	      }	      
	   }
	   
		
	   private void writeToFile(String str){
		   try {
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
	      try {
	         while ((line = reader.readLine()) != null) {
	            sb.append(line).append('\n');
	         }
	      } catch (IOException e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            is.close();
	         } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	        
	      return sb.toString();
	   }
	}
