import java.net.MalformedURLException;

public class MainCurrentTemp {
	public MainCurrentTemp() {
		
	}

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException{
        OpenWeatherAccess weatherObj=null;
        
        weatherObj = new OpenWeatherAccess();
        weatherObj.scanData();
        weatherObj.extractLocTemp();
 	}		
}

