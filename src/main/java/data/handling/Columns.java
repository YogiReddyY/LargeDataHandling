package data.handling;

import java.util.ArrayList;
import java.util.List;

public class Rows {

    private List rows = new ArrayList();

    /*private List<String> stringList;


    public Data() {
        stringList = new LinkedList<>();
    }

    public Data(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public boolean addData(String data) {
        return stringList.add(data);
    }*/
    private String id, first__name, middle__name, last__name, client__name, org__name, org__id, manager__name, lead__name, pin, city, country, longlong;

    public Data(String id, String first__name, String middle__name, String last__name, String client__name, String org__name, String org__id, String manager__name, String lead__name, String pin, String city, String country, String longlong) {
        this.id = id;
        this.first__name = first__name;
        this.middle__name = middle__name;
        this.last__name = last__name;
        this.client__name = client__name;
        this.org__name = org__name;
        this.org__id = org__id;
        this.manager__name = manager__name;
        this.lead__name = lead__name;
        this.pin = pin;
        this.city = city;
        this.country = country;
        this.longlong = longlong;
    }

}
