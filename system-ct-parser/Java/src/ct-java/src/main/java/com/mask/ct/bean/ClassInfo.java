package com.mask.ct.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class info
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;

    private String name;

    private String auth;

    private String date;

    private String description;

    private List<Attribute> attributes;


    private List<MethodInfo> methods;


    public ClassInfo() {
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }
}
