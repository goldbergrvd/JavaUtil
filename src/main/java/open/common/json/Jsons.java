package open.common.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import open.common.json.annotation.JsonArrayValue;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class Jsons {
    public static final Type json2DArrayType = new TypeToken<List<List<String>>>(){}.getType();

    public static <E> List<E> transform(String json2DArray, Class<E> clazz) {
        Gson gson = new Gson();
        List<E> result = new ArrayList<>();
        List<List<String>> table = gson.fromJson(json2DArray, json2DArrayType);

        Field[] allField = clazz.getDeclaredFields();
        List<Field> jsonArrayValueFields = new ArrayList<>();
        for (Field f : allField) {
            JsonArrayValue jp = f.getAnnotation(JsonArrayValue.class);
            if (jp != null) {
                f.setAccessible(Boolean.TRUE);
                jsonArrayValueFields.add(f);
            }
        }
        Collections.sort(jsonArrayValueFields,  (f1, f2) ->
                f1.getAnnotation(JsonArrayValue.class).index() - f2.getAnnotation(JsonArrayValue.class).index());

        try {
            for (List<String> row : table) {
                E obj = clazz.newInstance();

                for (Field field : jsonArrayValueFields) {
                    int index = field.getAnnotation(JsonArrayValue.class).index();
                    try {
                        switch (field.getType().getName()) {
                            case "java.lang.String":
                                field.set(obj, row.get(index));
                                break;
                            case "int":
                            case "java.lang.Integer":
                                field.setInt(obj, getInteger(row.get(index)));
                                break;
                            case "double":
                            case "java.lang.Double":
                                field.setDouble(obj, getDouble(row.get(index)));
                                break;
                            default:
                                throw new IllegalArgumentException("Wrong type " + field.getType().getName() +
                                        "!! Property type must be String, int, Integer, double or Double!!");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        break; // The index user indicate over json 2D array
                    }
                }
                result.add(obj);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException ex) {
            throw new RuntimeException("Jsons.transform reflection exception");
        }
        return result;
    }

    private static int getInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}
