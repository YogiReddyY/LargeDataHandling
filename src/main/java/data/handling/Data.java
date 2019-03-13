package data.handling;

import java.util.LinkedList;
import java.util.List;

public class Data {
    private List<String> stringList;

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
    }

}
