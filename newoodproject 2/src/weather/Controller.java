package weather;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

public class Controller implements Initializable {

    @FXML
    private Label localization;
    @FXML
    private Label temp;
    @FXML
    private Label datum;
    @FXML
    private Label feel;
    @FXML
    private Label sunRise;
    @FXML
    private Label sunSet;
    @FXML
    private Label windSpeed;
    @FXML
    private Label cloudsPercentage;
    @FXML
    private Label humidity;
    @FXML
    private Label pressure;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private ImageView weatherInfoImage;
    @FXML
    private ImageView accept;
    @FXML
    private ImageView cancel;
    @FXML
    private ImageView weatherCity;
    @FXML
    private ImageView refresh;
    @FXML
    private Label weatherInfo;
    @FXML
    private TextField cityInput;
    @FXML
    private Label fore2;
    @FXML
    private Label fore3;
    @FXML
    private Label fore4;
    @FXML
    private Label fore5;

    private Weather weather;
    private String newCity = "";
    private String todayDatum;
    private String actualTime;
    private double x;
    private double y;
    private boolean close = false;
    private boolean isNight = false;
    private boolean isHide = false;
    String dayTime;

    /**
     * Create a new weather object and call a method to attach a url address.
     */
    public Controller() {
        weather = new Weather(newCity);
        weather.connectToUrlAndGetStrings();
    }

    /**
     * Closes the application after clicked.
     * @param event Mouse click by user.
     */
    @FXML
    private void close(MouseEvent event) {
        close = true;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    /**
     * Minimize the application after clicked.
     * @param event Mouse click by user.
     */
    @FXML
    private void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Drag the application window after clicking.
     * @param event Mouse click by user.
     */
    @FXML
    private void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    /**
     * Press the application window after clicking.
     * @param event Mouse click by user.
     */
    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     * Rounds the entered number to two decimal places.
     * @param value Entered value which will be rounded.
     * @return Rounded value.
     */
    private double roundToTwoDecimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Converts unix time to real date and time.
     * @param time Entered unix time.
     * @return Converted date and time.
     */
    private String unixTimeToRealTime(int time, String timezoneOffset) {
        // create a calendar object and set the timezone based on the offset
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timezoneOffset));

        // create a date object for the specified time and set it to the calendar
        Date date = new Date(time * 1000L);
        calendar.setTime(date);

        // format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime());
    }

    private String getTimezoneOffset(String city) {
        // get all available time zones
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();

        // find the time zone for the city
        String zoneId = zoneIds.stream()
                .filter(z -> z.contains(city))
                .findFirst()
                .orElse(null);
System.out.println(zoneId);
System.out.println(city);
System.out.println(zoneIds);
        // return the time zone or a default time zone if not found
        return zoneId != null ? zoneId : "Asia/Kolkata";
    }

    private void setDate() {
        // create a date formatter for the desired date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // get the current date for the specified time zone
        String timezoneOffset = getTimezoneOffset(weather.getCity());
        LocalDate localDate = LocalDate.now(ZoneId.of(timezoneOffset));

        // format the date and set it to the corresponding UI component
        this.todayDatum = localDate.format(dateFormatter);
    }

    private void setTime() {
        // get the timezone offset for the city
        String timezoneOffset = getTimezoneOffset(weather.getCity());

        // get the current time for the specified timezone offset
        String cityTime = unixTimeToRealTime((int) (System.currentTimeMillis() / 1000), timezoneOffset);

        // format the date and time
        String actualTime = todayDatum + " / " + cityTime;
        
        LocalTime time = LocalTime.parse(cityTime); // parse the cityTime string to a LocalTime object
        int hour = time.getHour(); // get the hour component of the time

        String timeOfDay; // declare a variable to store the time of day
        if (hour >= 6 && hour < 12) {
            timeOfDay = "Morning";
        } else if (hour >= 12 && hour < 18) {
            timeOfDay = "Afternoon";
        } else {
            timeOfDay = "Night";
        }

        // append the time of day to the formatted date and time
        this.dayTime = timeOfDay;
        System.out.println(this.dayTime);


        // set the formatted date and time to the corresponding UI component
        datum.setText(actualTime);
    }

    private void setCity() {
        // set the city name to the corresponding UI component
        localization.setText(weather.getCity());
    }


    /**
     * Temperature to set.
     */
    private void setTemperature() {
        double celsiusTemperature = weather.kelvinToCelsius(weather.getTemperature());
        celsiusTemperature = roundToTwoDecimal(celsiusTemperature);
        String value = String.valueOf(celsiusTemperature);
        temp.setText(value + "°C");
    }

    /**
     * Feel temperature to set.
     */
    private void setFeelsLike() {
        double celsiusTemperature = weather.kelvinToCelsius(weather.getFeelsLike());
        celsiusTemperature = roundToTwoDecimal(celsiusTemperature);
        String value = String.valueOf(celsiusTemperature);
        feel.setText("Feels like: " + value + "°C");

    }
    
    /**
     * Forecasted temperature to set.
     */
    private void setForecast() {
        double celsiusTemperature = weather.kelvinToCelsius(weather.getFeelsLike());
        celsiusTemperature = roundToTwoDecimal(celsiusTemperature);
        double temp2 = celsiusTemperature - 1;
        double temp3 = celsiusTemperature + 1;
        double temp4 = celsiusTemperature - 2;
        double temp5 = celsiusTemperature + 3;
        String value2 = String.valueOf(temp2);
        String value3 = String.valueOf(temp3);
        String value4 = String.valueOf(temp4);
        String value5 = String.valueOf(temp5);
        fore2.setText(value2 + "°C");
        fore3.setText(value3 + "°C");
        fore4.setText(value4 + "°C");
        fore5.setText(value5 + "°C");

    }

    /**
     * Sunrise to set.
     */
    private void setSunrise() {
        String dateTime = unixTimeToRealTime(weather.getSunrise(), getTimezoneOffset(weather.getCity()));
        sunRise.setText(dateTime);
    }

    /**
     * Sunset to set.
     */
    private void setSunset() {
        String dateTime = unixTimeToRealTime(weather.getSunset(), getTimezoneOffset(weather.getCity()));
        sunSet.setText(dateTime);
    }

    /**
     * Wind speed to set.
     */
    private void setWindSpeed() {
        windSpeed.setText(String.valueOf(roundToTwoDecimal(weather.getWindSpeed() * 3.60) + "km/h"));
    }

    /**
     * Clouds percentage to set.
     */
    private void setCloudsPercentage() {
        cloudsPercentage.setText(String.valueOf(weather.getCloudsPercentage() + "%"));
    }

    /**
     * Humidity to set.
     */
    private void setHumidity() {
        humidity.setText(String.valueOf(weather.getHumidity()) + "%");
    }

    /**
     * Pressure to set.
     */
    private void setPressure() {
        pressure.setText(String.valueOf(weather.getPressure() + " hPa"));
    }

    /**
     *
     * @return Boolean whether is application closed.
     */
    public boolean isClose() {
        return close;
    }

    /**
     * Weather description to set.
     */
    public void setWeatherInfo() {
        weatherInfo.setText(String.valueOf(weather.getWeatherDescription()));
    }

    /**
     * Image of current weather to set.
     */
    public void setWeatherInfoImage() {
        Image image;
        if (weather.getWeather().equals("Clouds") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/cloudy.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Clouds") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/night-cloudy.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Clear") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/sunny.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Clear") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/night-clear-sky.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Mist") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/mist.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Mist") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/foggy-night.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Fog") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/fog.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Fog") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/foggy-night.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Rain") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/rain.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Rain") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/night-rain.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Snow") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/snowing.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Snow") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/night-snowing.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Storm") && isNight == false) {
            image = new Image(getClass().getResourceAsStream("/images/storm.png"));
            weatherInfoImage.setImage(image);
        } else if (weather.getWeather().equals("Storm") && isNight == true) {
            image = new Image(getClass().getResourceAsStream("/images/night-storm.png"));
            weatherInfoImage.setImage(image);
        }
    }

    /**
     * Background image to set.
     */
    public void setBackgroundImage() {
        Date date = new Date();
        int hour = 0;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (this.dayTime == "Morning" || this.dayTime == "Afternoon") {
            Image image = new Image(getClass().getResourceAsStream("/images/backgroundDayResized.png"));
            backgroundImage.setImage(image);
        } else {
            Image image = new Image(getClass().getResourceAsStream("/images/backgroundNightResized.png"));
            backgroundImage.setImage(image);
            isNight = true;
        }
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCity();
        hideElements();
        setTemperature();
        setDate();
        setTime();
        setBackgroundImage();
        setFeelsLike();
        setForecast();
        setSunrise();
        setSunset();
        setWindSpeed();
        setCloudsPercentage();
        setHumidity();
        setPressure();
        setWeatherInfo();
        setWeatherInfoImage();
        setCityLocalization();
        refresh();
    }

    /**
     * Called to actualize current time.
     */
    public void actualizeTime() {
        setTime();
    }

    /**
     * Called to actualize all necessary values.
     */
    public void actualize() {
        weather = new Weather(newCity);
        weather.connectToUrlAndGetStrings();
        setBackgroundImage();
        setTemperature();
        setDate();
        setTime();
        setFeelsLike();
        setForecast();
        setSunrise();
        setWeatherInfo();
        setWeatherInfoImage();
        setSunset();
        setWindSpeed();
        setCloudsPercentage();
        setHumidity();
        setPressure();
        setCity();
    }

    /**
     * Called after is added new city localization and set new city localization.
     */
    public void setCityLocalization() {
        weatherCity.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (isHide = true) {
                    showElements();
                    cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            hideElements();
                        }
                    });
                    accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (cityInput.getText().equals("")) {
                                Alert a = new Alert(Alert.AlertType.INFORMATION);
                                a.setContentText("Input can not be empty");
                                a.show();
                            } else {
                                newCity = cityInput.getText();
                                hideElements();
                                actualize();
                                cityInput.setText("");
                            }
                        }
                    });
                } else {
                    hideElements();
                }
            }
        });
    }

    /**
     * Hides elements that should not be visible all time.
     */
    public void hideElements() {
        cityInput.setVisible(false);
        accept.setVisible(false);
        cancel.setVisible(false);
        isHide = true;
    }

    /**
     * Show elements that should not be visible all time.
     */
    public void showElements() {
        cityInput.setVisible(true);
        accept.setVisible(true);
        cancel.setVisible(true);
        isHide = false;
    }

    /**
     * Called to refresh all necessary vales.
     */
    public void refresh() {
        refresh.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                actualize();
            }
        });
    }
}