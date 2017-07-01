package nigam.yomarket.getset;

/**
 * Created by Freeware Sys on 6/4/2017.
 */

public class statepojo {
    String stateid,state;


    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
