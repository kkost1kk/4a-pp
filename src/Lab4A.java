import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lab4A {
    public static void main(String[] args) throws IOException {
        GradeBook gb = new GradeBook("Иванов", "Иван", "Иванович", 2, "ПО-21");
        gb.getOrCreateSession(1).addItem("Матанализ", true, 9);
        gb.getOrCreateSession(1).addItem("Физра", false, 1);
        JSONObject json = new JSONObject(gb);
        json.remove("excellent");
        json.remove("passing");
        Files.writeString(Path.of("gradebook.json"), json.toString(2));
        System.out.println("gradebook.json записан\n");
//schema
       JSONObject rawSchema = new JSONObject(Files.readString(Path.of("schema.json")));
        Schema schema = SchemaLoader.builder().schemaJson(rawSchema).draftV7Support().build().load().build();
        JSONObject readJson = new JSONObject(Files.readString(Path.of("gradebook.json")));
        schema.validate(readJson);
        System.out.println("данные:");
        printReadable(readJson);
//ломаем
        JSONObject brokenJson = new JSONObject(readJson.toString());
        brokenJson.put("course", 99);
        Files.writeString(Path.of("gradebook_broken.json"), brokenJson.toString(2));

        try {
            JSONObject readBroken = new JSONObject(Files.readString(Path.of("gradebook_broken.json")));
            schema.validate(readBroken);
        } catch (ValidationException e) {
            System.out.println("сломано тут: " + e.getMessage());
        }
    }

    // рашен
    private static void printReadable(JSONObject json) {
        System.out.println("Фамилия: " + json.getString("lastName"));
        System.out.println("Имя: " + json.getString("firstName"));
        System.out.println("Отчество: " + json.getString("middleName"));
        System.out.println("Курс: " + json.getInt("course"));
        System.out.println("Группа: " + json.getString("group"));
        JSONArray sessions = json.getJSONArray("sessions");
        for (int i = 0; i < sessions.length(); i++) {
            JSONObject s = sessions.getJSONObject(i);
            System.out.println(" Сессия №" + s.getInt("sessionNumber") + ":");
            JSONArray items = s.getJSONArray("items");
            for (int j = 0; j < items.length(); ++j) {
                JSONObject item = items.getJSONObject(j);
                String type = item.getBoolean("exam") ? "Экзамен" : "Зачет";
                System.out.println("Предмет: " + item.getString("subject") + " Тип: " + type + " Оценка: " + item.getInt("grade"));
            }
        }
    }
}
