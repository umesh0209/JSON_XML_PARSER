import java.net.MalformedURLException;

public class GeoLocation {

	public static final String GEOCODE_URL =
			"https://maps.googleapis.com/maps/api/geocode/json?address=city&key=key_id";
	public static final String GEOCODE_APP_KEY="AIzaSyCHgwZch0_BgNZoQRB971kzT3a26FmnI3A";
	public static final String GEOCODE_JSON_FNAME = "Geocode.json";
	
	private String city;
	private double lat,lon;
	private ParseJson jsonObj;
	
	public GeoLocation() {
		System.out.println("GeoLocation():called");
		city = null;
		lat = lon = 0;
		jsonObj = null;
	}
	
	public void setCity(String city) {
		System.out.println("setCity():called"+city);
		this.city = city;
	}
	
	public void setJsonObj(ParseJson obj) {
		System.out.println("setJsonObj():called"+city);
		this.jsonObj = obj;
	}
	
	public String getCity() {
		System.out.println("getCity():called");
		return city;
	}
	
	public void extractGeoLoc() throws MalformedURLException{
		System.out.println("extractGeoLoc():called");
		jsonObj.setService(ParseJson.GEOCODE_GOOGLE_SERVICE);
		jsonObj.makeURL();
		jsonObj.parseJsonData();
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

}
