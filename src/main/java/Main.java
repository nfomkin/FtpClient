import json.JSONArray;
import json.JSONObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String jsonString = "{'name':'user','id':1234,'marks':[{'english':85,'physics':80,'chemistry':75}]}";

        // Parse json object for user data
        JSONObject user = new JSONObject(jsonString);

        // Get json array for user's marks
        JSONArray marks = user.getJSONArray("marks");

        // Get json object for subject's marks
        JSONObject subjects = marks.getJSONObject(0);

        // Print and display commands
        System.out.println(
                String.format("English marks - %s",
                        subjects.getValue("english")));
        System.out.println(
                String.format("Physics marks - %s",
                        subjects.getValue("physics")));
        System.out.println(
                String.format("Chemistry marks - %s",
                        subjects.getValue("chemistry")));
    }
}
