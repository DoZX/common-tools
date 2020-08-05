package com.mask.ct.bean;

import java.io.Serializable;

/**
 * Class comment mate. eg. class name, auth...
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class CommentMate implements Serializable {

    private String name;

    private String description;

    private String keyword;

    private ClassInfo classs;

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

    public ClassInfo getClasss() {
        return classs;
    }

    public void setClasss(ClassInfo classs) {
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
