WeatherJsonJavaApp

Model to read the current weather of a city. The city is entered in the format "city space State space country". 
If it has multiple words, should be entered with out space.For example: 
OFallon MO USA, 
NewYork NewYork USA 
Bangalore Karnataka India 
Sydney NewSouthWales AUS.

As Lat/Lon provides accurate data,the city, is sent to Google GeoCode, to extract Lat/Lon, which are passed to 
Open weather to get the current weather.