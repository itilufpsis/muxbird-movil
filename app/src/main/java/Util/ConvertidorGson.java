package Util;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by acer on 24/09/2017.
 */

public class ConvertidorGson {

    public static final <T> List<T> getList(final String json, final Class<T[]> clazz) {
        final T[] jsonToObject = new Gson().fromJson(json, clazz);
        return Arrays.asList(jsonToObject);
    }
}
