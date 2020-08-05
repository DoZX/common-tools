package com.mask.ct.bean;

import java.io.Serializable;

/**
 * Attribute info.
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class Attribute implements Serializable {

    private static final long serialVersionUID = -6849794470754667710L;

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
        return "Attribute{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
