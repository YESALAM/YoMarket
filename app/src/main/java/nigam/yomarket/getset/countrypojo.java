package nigam.yomarket.getset;

/**
 * Created by Freeware Sys on 6/4/2017.
 */

public class countrypojo {
    String id,country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return country;
    }
}
