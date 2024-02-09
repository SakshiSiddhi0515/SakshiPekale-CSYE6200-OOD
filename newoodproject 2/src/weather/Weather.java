package weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Weather {

    Connection connection;
    private double temperature = 0;
    private double feelsLike = 0;
    private double maxTemperature = 0;
    private double minTemperature = 0;
    private int pressure = 0;
    private int humidity = 0;
    private double windSpeed = 0;
    private int windDegrees = 0;
    private int cloudsPercentage = 0;
    private int sunrise = 0;
    private int sunset = 0;
    private int timezone = 0;
    private String city;
    private String country;
    private String weather;
    private String weatherDescription;
    private String cityName;

    /**
     * Called to initialize a weather after its root element has been completely processed.
     *
     * @param newCity Entered city. If the variable is null, the default city is Presov.
     */
    public Weather(String newCity) {
        if (newCity.equals("")) {
            newCity = "New Delhi";
        }
        this.cityName = newCity;
        this.connection = new Connection(cityName);
    }

    /**
     * Depending on the specified city, it connects to the url address and downloads information about the current weather.
     */
    public void connectToUrlAndGetStrings() {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(connection.getConnection());
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            jSonToVariables(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * All downloaded values are saved to variables.
     *
     * @param result Downloaded values from URL address.
     */
    private void jSonToVariables(StringBuilder result) {
        String jsonString = result.toString();
        JSONObject jsonObject = new JSONObject(jsonString);

        temperature = jsonObject.getJSONObject("main").getDouble("temp");
        feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like");
        maxTemperature = jsonObject.getJSONObject("main").getDouble("temp_max");
        minTemperature = jsonObject.getJSONObject("main").getDouble("temp_min");
        pressure = jsonObject.getJSONObject("main").getInt("pressure");
        humidity = jsonObject.getJSONObject("main").getInt("humidity");
        windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
        windDegrees = jsonObject.getJSONObject("wind").getInt("deg");
        cloudsPercentage = jsonObject.getJSONObject("clouds").getInt("all");
        sunrise = jsonObject.getJSONObject("sys").getInt("sunrise");
        sunset = jsonObject.getJSONObject("sys").getInt("sunset");
        country = jsonObject.getJSONObject("sys").getString("country");
        timezone = jsonObject.getInt("timezone");
        city = jsonObject.getString("name");

        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        for (int i = 0; i < jsonArray.length(); i++) {
            weather = jsonArray.getJSONObject(i).getString("main");
            weatherDescription = jsonArray.getJSONObject(i).getString("description");
        }
    }

    /**
     * Conversion of temperature in Kelvin to degrees Celsius.
     *
     * @param value Entered value in Kelvin.
     * @return Converted temperature in Celsius.
     */
    public double kelvinToCelsius(double value) {
        value = value - 273.15;
        return value;
    }

    /**
     * @return Current city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @return Current temperature.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @return Current feels temperature.
     */
    public double getFeelsLike() {
        return feelsLike;
    }

    /**
     * @return Current sunrise time.
     */
    public int getSunrise() {
        return sunrise;
    }

    /**
     * @return Current sunset time.
     */
    public int getSunset() {
        return sunset;
    }

    /**
     * @return Current wind speed.
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * @return Current clouds percentage.
     */
    public int getCloudsPercentage() {
        return cloudsPercentage;
    }

    /**
     * @return Current humidity.
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * @return Current pressure.
     */
    public int getPressure() {
        return pressure;
    }

    /**
     * @return Current object weather.
     */
    public String getWeather() {
        return weather;
    }

    /**
     * @return Current weather description.
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }
}