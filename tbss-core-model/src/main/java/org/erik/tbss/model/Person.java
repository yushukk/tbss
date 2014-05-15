package org.erik.tbss.model;

/**
 * Write class comments here.
 * User: caiwd
 * Date: 14-5-15 下午3:18
 * version $Id: Person.java, v0.1 Exp $.
 */
public class Person {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
