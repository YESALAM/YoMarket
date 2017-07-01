package nigam.yomarket.getset;

import java.io.Serializable;

/**
 * Created by alokit nigam on 5/14/2017.
 */

public class phonebook implements Serializable {
    String name;
    String pic;
    String contact;
    String phonebookID;
    String city;
    String profession;
    String firm_name;
    String product;

    public String getRegisterid() {
        return registerid;
    }

    public void setRegisterid(String registerid) {
        this.registerid = registerid;
    }

    String registerid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhonebookID() {
        return phonebookID;
    }

    public void setPhonebookID(String phonebookID) {
        this.phonebookID = phonebookID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getFirm_name() {
        return firm_name;
    }

    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


}
