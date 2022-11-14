package com.chixing.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "myuser")
public class User {
    @Id
    private Integer id;
    //自动检测类型
    @Field(type = FieldType.Auto)
    private Integer age;
    //手动设置为keyword  但同时也就不能分词
    @Field(type=FieldType.Keyword)
    private String name;
    //设置为text  可以分词
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_max_word")
    private String info;

    public User() {
    }

    public User(Integer id, Integer age, String name, String info) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.info = info;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
