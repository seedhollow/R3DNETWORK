package com.android.support.entity;

import java.util.List;

public class FeatureEntity {
    public int id;
    public String type;
    public String name;
    public String description;
    public boolean enabled;
    public Integer min;
    public Integer max;
    public String[] options;
    public List<FeatureEntity> children;
}

