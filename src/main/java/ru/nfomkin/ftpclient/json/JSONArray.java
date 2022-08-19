package ru.nfomkin.ftpclient.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONArray {
    private final static char arrayFieldsDelimiter;
    private final static char commaChar;

    private List<String> objects;

    static
    {
        arrayFieldsDelimiter = String.valueOf(Constant.ARRAY_FIELDS_DELIMITER)
                .toCharArray()[0];
        commaChar = String.valueOf(Constant.COMMA)
                .toCharArray()[0];
    }

    // Constructor of this class
    public JSONArray(String arg) { getJSONObjects(arg); }

    // Method 1
    // Storing json objects in array list
    public void getJSONObjects(String arg)
    {

        objects = new ArrayList<String>();

        if (arg.startsWith(String.valueOf(
                Constant.SQUARE_OPEN_BRACKETS))
                && arg.endsWith(String.valueOf(
                Constant.SQUARE_CLOSE_BRACKETS))) {

            StringBuilder builder = new StringBuilder(arg);

            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);

            builder = replaceCOMMA(builder);

            // Adding all elements
            // using addAll() method of Collections class
            Collections.addAll(
                    objects,
                    builder.toString().split(
                            String.valueOf(Constant.COMMA)));
        }

        objects.replaceAll(string -> string.replace('|', ','));
    }

    // Method 2
    public StringBuilder replaceCOMMA(StringBuilder arg)
    {
        boolean isObject = false;

        for (int i = 0; i < arg.length(); i++) {
            char a = arg.charAt(i);
            if (isObject) {

                if (String.valueOf(a).compareTo(
                        String.valueOf(Constant.COMMA))
                        == 0) {
                    arg.setCharAt(i, arrayFieldsDelimiter);
                }
            }

            if (String.valueOf(a).compareTo(String.valueOf(
                    Constant.CURLY_OPEN_BRACKETS))
                    == 0)
                isObject = true;

            if (String.valueOf(a).compareTo(String.valueOf(
                    Constant.CURLY_CLOSE_BRACKETS))
                    == 0)
                isObject = false;
        }

        return arg;
    }

    // Method  3
    // Getting json object by index from array list
    public String getObject(int index)
    {
        if (objects != null) {
            return objects.get(index).replace(arrayFieldsDelimiter,
                    commaChar);
        }

        return null;
    }

    public List<String> getObjects() {
        return objects;
    }
    // Method 4
    // Getting json object from array list
    public JSONObject getJSONObject(int index)
    {
        if (objects != null) {
            return new JSONObject(
                    objects.get(index));
        }

        return null;
    }
}
