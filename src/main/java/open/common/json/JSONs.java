package open.common.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Hubert Lu <goldbergrvd@gmail.com>
 */
public class JSONs {
    private final static Type json2DArrayType = new TypeToken<List<List<String>>>(){}.getType();
    
    public static <E> List<E> transform(String json2DArray, Class<E> clazz) {
        Gson gson = new Gson();
        List<E> result = new ArrayList<E>();
        List<List<String>> table = gson.fromJson(json2DArray, json2DArrayType);

        Field[] allField = clazz.getFields();
        LinkedList<Field> sortedFields = new LinkedList<Field>();
        for (Field f : allField) {
            JsonProperty jp = f.getAnnotation(JsonProperty.class);
            if (jp != null) {
                sortedFields.add(jp.index(), f);
            }
        }

        try {
            for (List<String> row : table) {
                E obj = clazz.newInstance();
                int size = row.size();
                for (int i = 0; i < size; i++) {
                    Field f = sortedFields.get(i);
                    obj.getClass().getField(f.getName()).set(obj, row.get(i));
                }
                result.add(obj);
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(JSONs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JSONs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(JSONs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JSONs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
