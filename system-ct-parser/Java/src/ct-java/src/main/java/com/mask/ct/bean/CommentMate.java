package com.mask.ct.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class comment mate. eg. class name, auth...
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class CommentMate implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;

    private String name;

    private String description;

    private String keyword;

    private List<ClassInfo> classs = new ArrayList<>();

    public CommentMate() {

    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<ClassInfo> getClasss() {
        return classs;
    }

    public void setClasss(List<ClassInfo> classs) {
        this.classs = classs;
    }

    @Override
    public String toString() {
        return "CommentMate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", keyword='" + keyword + '\'' +
                ", classs=" + classs +
                '}';
    }
}
