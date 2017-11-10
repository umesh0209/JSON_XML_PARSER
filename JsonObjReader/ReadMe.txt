WeatherJsonJavaApp

Model to read the current weather of a city. The city is entered in the format "city space State space country". 
If it has multiple words, should be entered with out space.For example: 
OFallon MO USA, 
NewYork NewYork USA 
Bangalore Karnataka India 
Sydney NewSouthWales AUS.

As Lat/Lon provides accurate data,the city, is sent to Google GeoCode, to extract Lat/Lon, which are passed to 
Open weather to get the current weather.

Output looks like:
**********************************************
Temp(CURR)=6.0°C Temp(MIN)=6.0°C TEMP(MAX)6.0°C
Humidity=56.0% Winds= 5.7 meters/sec Clouds=90.0%
Description=overcast clouds
**********************************************