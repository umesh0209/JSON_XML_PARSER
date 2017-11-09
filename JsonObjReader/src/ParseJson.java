import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


public class ParseJson {

	/****************************************************************************
	 *                         OPEN WEATHER VARIABLES                           *
	 ****************************************************************************/
	private boolean fahrenheit;
	private int service;
	private int jsonMode;
	private String serviceUrl;
	private double temp,temp_max,temp_min,humidity,windSpeed,clouds;
	private String windUnit;
	private GeoLocation geoObj;
	private String unitSymbol;
	
	/****************************************************************************
	 *                         PUBLIC/PRIVATE CONSTANTS                                 *
	 ****************************************************************************/
	private final String DEGREE  = "\u00b0";
	private final String MILES_PER_HR = "miles/hr";
	private final String METERS_PER_SEC = "meters/sec";
	private final String PERCENTAGE ="%";
	public static final int NONE_SERVICE = 0;
	public static final int OPEN_WEATHER_SERVICE = 1;
	public static final int GEOCODE_GOOGLE_SERVICE = 2;
	
	public static final int NONE_MODE=0;
	public static final int OBJECT_MODE=1;
	public static final int STREAM_MODE=2;
	
	/****************************************************************************
	 *                         CLASS FUNCTIONS                                  *
	 ****************************************************************************/
	public ParseJson() {
		System.out.println("JsonParser():called");
		fahrenheit = true;;
		windUnit = MILES_PER_HR;
		unitSymbol = DEGREE+"F";
		service = NONE_SERVICE;
		jsonMode = NONE_MODE;
		serviceUrl=null;
		temp = temp_max= temp_min = 0.0;
		clouds = humidity = windSpeed = 0.0;
	}
	
	public void setGeoObj(GeoLocation g) {
		geoObj=g;
	}
	
	public String getGeoAddress() {
		return geoObj.getCity();
	}
	
	public void displayTemp() {
		System.out.println("**********************************************");
		System.out.println("Temp(CURR)="+temp+unitSymbol+" Temp(MIN)="+temp_min+unitSymbol+" TEMP(MAX)"+temp_max+unitSymbol);
		System.out.println("Humidity="+humidity+PERCENTAGE+" Winds= "+windSpeed+" "+windUnit+" Clouds="+clouds+PERCENTAGE);
		System.out.println("**********************************************");
	}
	
	public void setService(int serv) {
		this.service = serv;
	}
	
	public void setJsonMode(int mod) {
		this.jsonMode = mod;
	}
	
	public void setUnit(boolean f) {
		this.fahrenheit = f;
		if (this.fahrenheit == true) {
			this.unitSymbol = DEGREE+"F";
			this.windUnit = MILES_PER_HR;
		}else {
			this.unitSymbol = DEGREE+"C";
			this.windUnit = METERS_PER_SEC;
		}
	}
	
	public void makeURL() {
		System.out.println("makeURL("+service+"):called");
		if (service <= NONE_SERVICE || service > GEOCODE_GOOGLE_SERVICE ) {
			System.out.println("Please enter proper service(1,2)");
			System.exit(0);
		}
		
		if ( service == OPEN_WEATHER_SERVICE) {
			serviceUrl = makeOpenWeatherURL();
		}
		else if(service == GEOCODE_GOOGLE_SERVICE) {
			serviceUrl = makeGeoLocURL();
		}
	}
	
	public void parseJsonData() throws MalformedURLException{	
		System.out.println("parseJsonData():called");
		if ( service == OPEN_WEATHER_SERVICE) {
			parseJsonOpenWeatherCurrent();
		}else if(service == GEOCODE_GOOGLE_SERVICE) {
			parseJsonGeoLoc();
		}
	}

	
	/****************************************************************************
	 *                  OPEN WEATHER API PRVIATE JSON FUNCTIONS                 *
	 ****************************************************************************/
	
	private  double unboxObjToNum(Object o) {
		double temp=0.0;
		
		System.out.println("unboxObjToNum():called"); 
		
		if (o instanceof Double) {
			System.out.println("unboxObjToNum():instanceof double");
			temp = ((Double) o).doubleValue();
		}
		else if ( o instanceof Long) {
			long t;
			System.out.println("unboxObjToNum():instanceof Long");
			t = ((Long) o).longValue();
			temp = (double)t;
		}
		else if (o instanceof Float) {
			float t;
			System.out.println("unboxObjToNum():instanceof Float");
			t = ((Float) o).floatValue();
			temp = (double)t;
		}
		else if ( o instanceof Integer) {
			int t;
			System.out.println("unboxObjToNum():instanceof Int");
			t = ((Integer)o).intValue();
			temp = (double)t;
		}
		else {
			System.out.println("unboxObjToNum():Should not be reached here...");
			System.exit(0);
		}
		
		return temp;
	}
	
	private String makeOpenWeatherURL() {      
        System.out.println("makeOpenWeatherURL():called");     
        String url;
        Double lat,lon;
        url = new String(OpenWeatherAccess.OPEN_WEATHER_URL);
        
        lat = (Double) geoObj.getLat();
        lon = (Double) geoObj.getLon();
        
        if (url.contains("lat_val")) {
        	url= url.replace("lat_val", lat.toString());
        	System.out.println("makeOpenWeatherURL():Adding lat_val="+lat.doubleValue()+" to Ope(n weather URL");
         }
                
        if (url.contains("lon_val")) {
        	url= url.replace("lon_val", lon.toString());
        	System.out.println("makeOpenWeatherURL():Adding lon_val="+lon.doubleValue()+" to Open weather URL");
         }
        
        //Add units
        if (url.contains("temp_units")) {
        	if ( fahrenheit == true) {
        		url = url.replace("temp_units", "imperial");
        		System.out.println("makeOpenWeatherURL():imperial(F) added");
        	}
        	else {
        		url = url.replace("temp_units", "metric");
        		System.out.println("makeOpenWeatherURL():metric(C) added");      		
        	}
        }
        
        //Add App_id
        if(url.contains("key_id")) {
        	url = url.replace("key_id", OpenWeatherAccess.OPEN_WEATHER_APP_KEY);
        	System.out.println("makeOpenWeatherURL():App id added");
        }
        
        System.out.println("makeOpenWeatherURL():URL is="+url);
        
        return url;
	}
	
	private void parseJsonOpenWeatherCurrent() throws MalformedURLException {
				
		System.out.println("parseJsonOpenWeatherCurrent():called");
		
        switch(jsonMode){
        case OBJECT_MODE:
    	{
    		HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response          

    		try{
    			String jsonFile = OpenWeatherAccess.OPEN_WEATHER_JSON_FNAME;
    			sh.setFileName(jsonFile);
                sh.makeServiceCall(serviceUrl);
                
        		JSONParser parser = new JSONParser();     	
            	Object obj = parser.parse(new FileReader(jsonFile));
            	JSONObject currLocReader = (JSONObject)obj;
            	System.out.println(currLocReader);
            	
            	Long code = (Long)currLocReader.get("cod");
            	              	
            	if ( code.intValue() == 200){
            		//code = 200,Success;401,failure
            		Object t;
            		System.out.println("parseJsonOpenWeatherCurrent():code="+code.intValue()+" Success");
            		JSONObject main = (JSONObject)currLocReader.get("main");
            		System.out.println(main);
            		//System.out.println("parseJson():Temp ="+temp);  
            		t = main.get("temp");
            		temp = unboxObjToNum(t);
            		
            		t = (Object)main.get("temp_max");
            		temp_max = unboxObjToNum(t);
            		
            		t = (Object)main.get("temp_min");
            		temp_min = unboxObjToNum(t);
            		
            		t = (Object)main.get("humidity");
            		humidity = unboxObjToNum(t);
            		
            		JSONObject clouds1 = (JSONObject)currLocReader.get("clouds");
            		System.out.println(clouds1);
            		t = (Object)clouds1.get("all");
            		clouds= unboxObjToNum(t);
            		
            		JSONObject winds = (JSONObject)currLocReader.get("wind");
            		System.out.println(winds);
            		t = (Object)winds.get("speed");
            		windSpeed=unboxObjToNum(t);
            	}
            	else{
            		//401
            		System.out.println("parseJsonOpenWeatherCurrent():code="+code.intValue()+" Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.");
            		System.exit(0);
            	}

            }catch(IOException ie){
            	System.out.println(ie.getMessage());
            	System.exit(0);
            }catch (Exception e) {
    	    	System.out.println("parseJsonOpenWeatherCurrent():Check the input especially appid");
    	    	e.getClass().toString();
    	    	e.printStackTrace();
    	    	System.exit(0);
    	    }
    		System.out.println("parseJsonOpenWeatherCurrent():Object mode exiting...");
    	}
    	break;
    	
    	case STREAM_MODE:
    		{
     			System.out.println("parseJsonOpenWeatherCurrent():Stream mode selected");
  			
    			URL url = new URL(serviceUrl);
    			JsonFactory jfactory = new JsonFactory();
    			try (   InputStream is = url.openStream();
    					JsonParser parser = jfactory.createParser(is))
    					//JsonParser parser = jfactory.createParser(new File("stream.json")))
    				 {
    				    //parse till the end of stream }
    					System.out.println("parseJsonOpenWeatherCurrent():Entering streaming mode");
    					
    					String fieldName;
    					int status = 0;
    					boolean parseStream;
    					
    					JsonToken tok = null;
    					//Skipping all null at the beginning of stream...
    					while((tok=parser.nextToken())!=JsonToken.START_OBJECT) {
    						System.out.println("parseJsonOpenWeatherCurrent(): Skipping..."+(String)parser.getCurrentName());
    					}

    					//exit the streaming once code, status of streaming is found
    					//if cod is success, means required fields are found
    					parseStream = true;
    					while( parseStream == true) {
    						fieldName =  parser.getCurrentName();
    						if ("main".equals(fieldName)) {
    							tok = parser.nextToken();
    							System.out.println("parseJsonOpenWeatherCurrent(): main="+tok);
    							while (tok != JsonToken.END_OBJECT) {
    								fieldName =  parser.getCurrentName();
    								if ("temp".equals(fieldName)) {
     	    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting temp...="+parser.nextToken());
    	    							temp = parser.getDoubleValue(); 	    							
    	    						}else if ("temp_min".equals(fieldName)) {
    	    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting temp_min...="+parser.nextToken());
    	    							temp_min = parser.getDoubleValue();
    	    						}else if ("temp_max".equals(fieldName)) {
    	    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting temp_max...="+parser.nextToken());
    	    							temp_max = parser.getDoubleValue();
    	    						}else if ("humidity".equals(fieldName)) {
    	    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting humidity...="+parser.nextToken());
    	    							humidity = parser.getDoubleValue();
    	    						}
	    							tok = parser.nextToken();
    							}
    						}else if("wind".equals(fieldName)) {
    							tok = parser.nextToken();
    							System.out.println("parseJsonOpenWeatherCurrent(): wind="+tok);
    							while(tok != JsonToken.END_OBJECT) {
    								fieldName =  parser.getCurrentName();
    								if ("speed".equals(fieldName)) {
    	    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting wind speed...="+parser.nextToken());
    	    							windSpeed = parser.getDoubleValue();
    	    						}
	    							tok = parser.nextToken();
    							}
    						}else if ("clouds".equals(fieldName)) {
       							tok = parser.nextToken();
       							System.out.println("parseJsonOpenWeatherCurrent(): cloud="+tok);
    							while(tok != JsonToken.END_OBJECT) {
    								fieldName =  parser.getCurrentName();
    								if ("all".equals(fieldName)) {
    	    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting clouds...="+parser.nextToken());
    	    							clouds = parser.getDoubleValue();
    	    						}
	    							tok = parser.nextToken();
    							}
    						}else if ("cod".equals(fieldName)){
    							System.out.println("parseJsonOpenWeatherCurrent(): Extracting cod...="+parser.nextToken());
    							status = parser.getIntValue();
    							parseStream = false;
      						}

        					System.out.println("parseJsonOpenWeatherCurrent(): Skipping...="+fieldName+" token="+tok);
        					System.out.println("Jump to next token...");
        					tok=parser.nextToken();
    					}
    					
    	            	if (status != 200){
    	            		//401
    	            		System.out.println("parseJsonOpenWeatherCurrent():code="+status+" Invalid API key...");
    	            		System.exit(0);
    	            	}else {
    	            		System.out.println("parseJsonOpenWeatherCurrent():code=SUCCESS");
    	            	}

    				 }catch(JsonParseException jpe){ 
    					 jpe.printStackTrace();
    					 System.exit(0);
    				 }catch (MalformedURLException mue){
    					 mue.printStackTrace();
    					 System.exit(0);
    				 }catch(IOException ie){
    					 System.out.println("parseJsonOpenWeatherCurrent():Check the input especially appid");
    					 System.exit(0);
    				 }
    			System.out.println("parseJsonOpenWeatherCurrent():Stream mode exiting...");
    		}	
    		break;
    	     	
    	default:
    		break;
        } 
	}
	
	/****************************************************************************
	 *                  GEO LOCATION PRIVATE API JSON FUNCTIONS                 *
	 ****************************************************************************/
	private String makeGeoLocURL() {
		System.out.println("makeGeoLocURL():called");     
        String url=new String(GeoLocation.GEOCODE_URL);     
        String a = getGeoAddress();
        
        //In the context of a regex, \s will remove anything that is a space 
        //character (including space, tab characters etc). You need to escape 
        //the backslash in Java so the regex turns into \\s. 
        //Also, since Strings are immutable it is important that 
        //you assign the return value of the regex to a.
        a = a.replace(" ", "+");
        
        if (url.contains("city")) {
        	url= url.replace("city", a);
        	System.out.println("makeGeoLocURL():replace addr="+a);
         }
        
        //Add App_id
        if(url.contains("key_id")) {
        	url = url.replace("key_id", GeoLocation.GEOCODE_APP_KEY);
        	System.out.println("makeGeoLocURL():Key id added");
        }
        
        System.out.println("makeGeoLocURL():URL is="+url);
        
        return url;
	}
	
	private void parseJsonGeoLoc() throws MalformedURLException {
		System.out.println("parseJsonGeoLoc():called");
		Double glat,glon;
		
        switch(jsonMode){
        case OBJECT_MODE:
    	{
    		HttpHandler sh = new HttpHandler();
    		
            // Making a request to url and getting response          

    		try{
    			String jsonFile = GeoLocation.GEOCODE_JSON_FNAME;
    			sh.setFileName(jsonFile);
                sh.makeServiceCall(serviceUrl);
                
        		JSONParser parser = new JSONParser();     	
            	Object obj = parser.parse(new FileReader(jsonFile));
            	JSONObject currLocReader = (JSONObject)obj;
            	System.out.println("currLocReader="+currLocReader);
            	
            	String stat = (String)currLocReader.get("status");
            	              	
            	if ( stat.compareTo("OK") == 0){
            		//code = 200,Success;401,failure
            		System.out.println("parseJsonGeoLoc(): Success");
            		JSONArray resArr = (JSONArray)currLocReader.get("results");
            		JSONObject jsonobj = (JSONObject)resArr.get(0);
            		JSONObject geometry = (JSONObject) jsonobj.get("geometry");
            		JSONObject location = (JSONObject) geometry.get("location");
            		
            		glat = (Double)location.get("lat");
            		glon = (Double)location.get("lng");
            		geoObj.setLat(glat.doubleValue());
            		geoObj.setLon(glon.doubleValue());
            	}else{
            		//Error
            		if (stat.compareTo("REQUEST_DENIED")==0) {
            			String err_msg = (String)currLocReader.get("error_message");
            			System.out.println("parseJsonGeoLoc():"+err_msg);
            		}else {
            			System.out.println("parseJsonGeoLoc():Error");
            		}
            		System.exit(0);
            	}
            }catch(IOException ie){
            	System.out.println(ie.getMessage());
            	System.exit(0);
            }catch (Exception e) {
    	    	System.out.println("parseJsonGeoLoc():Check the input especially appid");
    	    	e.getClass().toString();
    	    	e.printStackTrace();
    	    	System.exit(0);
    	    }
    		System.out.println("parseJsonGeoLoc():Object mode exiting...");
    	}
    	break;
    	
    	case STREAM_MODE:
		{
 			System.out.println("Stream mode selected");
			
 			String STATUS;
 			
			URL url = new URL(serviceUrl);
			JsonFactory jfactory = new JsonFactory();
			try (   InputStream is = url.openStream();
					JsonParser parser = jfactory.createParser(is))
					//JsonParser parser = jfactory.createParser(new File("stream.json")))
				 {
				    //parse till the end of stream }
					System.out.println("parseJsonGeoLoc():Entering streaming mode");
					
					String fieldName;
					boolean parseStream;
					JsonToken tok = null;
					//Skipping all null at the beginning of stream...
					while((tok=parser.nextToken())!=JsonToken.START_OBJECT) {
						System.out.println("parseJsonGeoLoc(): Skipping..."+(String)parser.getCurrentName());
					}

					parseStream = true;
					STATUS = "OK";
					//exit the streaming once code, status of streaming is found
					//if cod is success, means required fields are found
					while( parseStream == true) {
						fieldName =  parser.getCurrentName();
					
						if("location".equals(fieldName)) {
							tok = parser.nextToken();
							System.out.println("parseJsonGeoLoc(): location="+tok);
							while(tok != JsonToken.END_OBJECT) {
								fieldName =  parser.getCurrentName();
								if ("lat".equals(fieldName)) {
									System.out.println("parseJsonGeoLoc(): Extracting Status...="+parser.nextToken());
									glat = (Double)parser.getDoubleValue();
									System.out.println("parseJsonGeoLoc():Lat="+glat);
									geoObj.setLat(glat.doubleValue());
	    						}else if ("lng".equals(fieldName)) {
	    							System.out.println("parseJsonGeoLoc(): Extracting Status...="+parser.nextToken());
	    							glon = (Double)parser.getDoubleValue();
	    							System.out.println("parseJsonGeoLoc():Lng="+glon);
	    							geoObj.setLon(glon.doubleValue());
	    						}
								tok = parser.nextToken();
							}
						}
						else if ("status".equals(fieldName)) {
							System.out.println("parseJsonGeoLoc(): Extracting Status...="+parser.nextToken());
							STATUS = (String)parser.getValueAsString();
							System.out.println("parseJsonGeoLoc():stat="+STATUS);
							parseStream = false;
							continue;
						}
        				System.out.println("Jump to next token...");
						tok = parser.nextToken();
					}

	            	if (!STATUS.equals("OK")){
	            		//REQ RJECTD
	            		System.out.println("parseJsonGeoLoc():code="+STATUS+" Invalid API key...");
	            		System.exit(0);
	            	}
	            	else {
	            		System.out.println("parseJsonGeoLoc():code="+STATUS);
	            	}

				 }catch(JsonParseException jpe){ 
					 jpe.printStackTrace();
					 System.exit(0);
				 }catch (MalformedURLException mue){
					 mue.printStackTrace();
					 System.exit(0);
				 }catch(IOException ie){
					 System.out.println("parseJsonGeoLoc():Check the input especially appid");
					 System.exit(0);
				 }
			System.out.println("parseJsonGeoLoc():Stream mode exiting...");
		}	
		break;        	
    	
    	default:
    		break;
        }
	}	
}
