package grn.database;

import java.util.ArrayList;
import java.util.List;

public class QueryRow {
    private List<Object> values = new ArrayList<>();

    public void addValue (Object object) {
        values.add(object);
    }

    public Object get (int i) {
        return values.get(i - 1);
    }
}
