/**
 * User: ddumanskiy
 * Date: 6/8/13
 * Time: 12:51 PM
 */
public class Location {

    private String country;

    private String city;

    private String region;

    public Location(String country, String city, String region) {
        this.country = country;
        this.city = city;
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Location{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
