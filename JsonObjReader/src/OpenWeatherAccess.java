import java.net.MalformedURLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OpenWeatherAccess {


	public static final String OPEN_WEATHER_URL = 
			"http://api.openweathermap.org/data/2.5/weather?lat=lat_val&lon=lon_val&units=temp_units&appid=key_id";
	public static final String OPEN_WEATHER_APP_KEY = "3fca5279c45094c8c4d23b9d13d53d7d";
	public static final String OPEN_WEATHER_JSON_FNAME = "Loc.json";
	
	private ParseJson jsonObj;
	private GeoLocation geoObj;
	
	public OpenWeatherAccess() {
		System.out.println("OpenWeatherAccess():called");
		jsonObj = new ParseJson();
		geoObj = new GeoLocation();
		jsonObj.setGeoObj(geoObj);
	}
	
	public void scanData()
	{
		int jsonMode;
		String city;
		int unit;
		
		System.out.println("scanData():called");
		//opens a scanner, keyboard
		try {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Json Mode(OBJECT=1,STREAM=2)");
        jsonMode = input.nextInt();
        if(jsonMode == 1) {
        	jsonObj.setJsonMode(ParseJson.OBJECT_MODE);
        }
        else if (jsonMode == 2) {
        	jsonObj.setJsonMode(ParseJson.STREAM_MODE);
        }
        else {
        	System.out.println("Wrong units enter 1 or 2, Try again!!!");
        	System.exit(0);
        }
        
        System.out.println("Enter Units 0=>Celsius, 1=>Fahrenheit");
        unit = input.nextInt();
        if ( unit == 1) {//Fahrenheit
        	jsonObj.setUnit(true);
        }else if ( unit == 0) {//Celsius
        	jsonObj.setUnit(false);
        }
        else {
        	System.out.println("Wrong units enter 0 or 1, Try again!!!");
        	System.exit(0);
        }
        System.out.println("Enter <city>space<State>space<Country>");
        city = input.next();
        city = city.concat(" ").concat(input.next()).concat(" ").concat(input.next());
        input.close();
        geoObj.setCity(city);
		}catch (InputMismatchException ime) {
			System.out.println("Wrong Entry...please check the input...Try again");
			System.exit(0);
		}     
	}
	
	private void extractGeoLoc() throws MalformedURLException{
		System.out.println("extractGeoLoc():called");
		jsonObj.setService(ParseJson.GEOCODE_GOOGLE_SERVICE);
		jsonObj.makeURL();
		jsonObj.parseJsonData();
	}
	
	public void extractLocTemp() throws MalformedURLException {
		System.out.println("extractLocTemp():called");
		extractGeoLoc();
		
		jsonObj.setService(ParseJson.OPEN_WEATHER_SERVICE);
		jsonObj.makeURL();
		jsonObj.parseJsonData();
		jsonObj.displayTemp();
		System.exit(0);
	}
}
