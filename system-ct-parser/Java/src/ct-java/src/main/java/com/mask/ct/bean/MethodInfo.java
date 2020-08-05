package com.mask.ct.bean;

import java.io.Serializable;

/**
 * Method info.
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class MethodInfo implements Serializable {

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

