package com.android.support;

import com.android.support.entity.FeatureEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeatureParser {

    public static List<FeatureEntity> parse(String json) {
        List<FeatureEntity> features = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                features.add(parseObject(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return features;
    }

    private static FeatureEntity parseObject(JSONObject obj) throws JSONException {
        FeatureEntity feature = new FeatureEntity();

        feature.id              = obj.optInt("id", 0);
        feature.type            = obj.optString("type", "");
        feature.name            = obj.optString("name", "");
        feature.description     = obj.optString("description", "");
        feature.enabled         = obj.optBoolean("enabled", false);
        feature.min             = obj.has("min") ? obj.getInt("min") : null;
        feature.max             = obj.has("max") ? obj.getInt("max") : null;

        if (obj.has("options")) {
            JSONArray opts = obj.getJSONArray("options");
            feature.options = new String[opts.length()];
            for (int j = 0; j < opts.length(); j++) {
                feature.options[j] = opts.getString(j);
            }
        }

        // Recursive children parsing
        // this used for nested features like ICollapse
        if (obj.has("children")) {
            JSONArray childrenArray = obj.getJSONArray("children");
            List<FeatureEntity> childList = new ArrayList<>();
            for (int j = 0; j < childrenArray.length(); j++) {
                childList.add(parseObject(childrenArray.getJSONObject(j)));
            }
            feature.children = childList;
        }

        return feature;
    }
}