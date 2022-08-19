package ru.nfomkin.ftpclient.json;

import java.util.HashMap;

public class JSONObject {
    private final static char arrayFieldsDelimiter;
    private final static char arrayObjectsDelimiter;
    private final static char commaChar;
    private HashMap<String, String> objects;

    static {
        arrayFieldsDelimiter = String.valueOf(Constant.ARRAY_FIELDS_DELIMITER)
                .toCharArray()[0];
        arrayObjectsDelimiter = String.valueOf(Constant.ARRAY_OBJECTS_DELIMITER)
                .toCharArray()[0];
        commaChar = String.valueOf(Constant.COMMA)
                .toCharArray()[0];
    }

    // Constructor if this class
    public JSONObject(String arg) {
        getJSONObjects(arg);
    }

    // Method 1
    // Storing json objects as key value pair in hash map
    public void getJSONObjects(String arg) {
        arg = arg.replaceAll("\\s", "");
        objects = new HashMap<String, String>();

        if (arg.startsWith(String.valueOf(
                Constant.CURLY_OPEN_BRACKETS))
                && arg.endsWith(String.valueOf(
                Constant.CURLY_CLOSE_BRACKETS))) {

            StringBuilder builder = new StringBuilder(arg);
            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);
            builder = replaceCOMMA(builder);

            for (String objects : builder.toString().split(
                    String.valueOf(Constant.COMMA))) {

                String[] objectValue = objects.split(
                        String.valueOf(Constant.COLON), 2);

                if (objectValue.length == 2)
                    this.objects.put(
                            objectValue[0]
                                    .replace("'", "")
                                    .replace("\"", ""),
                            objectValue[1]
                                    .replace("'", "")
                                    .replace("\"", ""));
            }
        }
    }

    public StringBuilder replaceCOMMA(StringBuilder arg) {

        boolean isJsonArray = false;
        boolean isCurlyOpen = false;

        for (int i = 0; i < arg.length(); i++) {
            char a = arg.charAt(i);

            if (isJsonArray) {

                if (String.valueOf(a).compareTo(
                        String.valueOf(Constant.COMMA)) == 0) {
                    if (isCurlyOpen) {
                        arg.setCharAt(i, arrayFieldsDelimiter);
                    } else {
                        arg.setCharAt(i, arrayObjectsDelimiter);
                    }
                }
            }

            if (String.valueOf(a).compareTo(String.valueOf(
                    Constant.SQUARE_OPEN_BRACKETS))
                    == 0)
                isJsonArray = true;
            if (String.valueOf(a).compareTo(String.valueOf(
                    Constant.SQUARE_CLOSE_BRACKETS))
                    == 0)
                isJsonArray = false;
            if (String.valueOf(a).compareTo(String.valueOf(
                    Constant.CURLY_OPEN_BRACKETS))
                    == 0) {
                isCurlyOpen = true;
            }
            if (String.valueOf(a).compareTo(String.valueOf(
                    Constant.CURLY_CLOSE_BRACKETS))
                    == 0) {
                isCurlyOpen = false;
            }

        }

        return arg;
    }

    // Getting json object value by key from hash map
    public String getValue(String key) {
        if (objects != null) {
            return objects.get(key)
                    .replace(arrayFieldsDelimiter, commaChar)
                    .replace(arrayObjectsDelimiter, commaChar);
        }
        return null;
    }


    // Getting json array by key from hash map
    public JSONArray getJSONArray(String key) {
        if (objects != null)
            return new JSONArray(
                    objects.get(key)
                            .replace('|', ',')
                            .replace(';', ','));
        return null;
    }
}

