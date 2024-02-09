package weather;

public class Connection {

    private String apiKey;
    private String cityName;
    private String urlByCityName;

    /**
     * Create a new url address according to the specified city.
     *
     * @param cityName Entered name of city.
     */
    public Connection(String cityName) {
        this.cityName = cityName;
        this.apiKey = "e5a707d6739a185b03937a1bcd3685fa";
        this.urlByCityName = "http://api.openweathermap.org/data/2.5/weather?q=" + this.cityName + "&appid=" + apiKey;
    }

    /**
     * @return Current url address by city name.
     */
    public String getConnection() {
        return urlByCityName;
    }
}