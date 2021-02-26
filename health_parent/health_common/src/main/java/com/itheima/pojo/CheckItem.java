package com.itheima.pojo;

import java.io.Serializable;

public class CheckItem implements Serializable {
    private Integer id;         //主键
    private String code;        //项目编码
    private String name;        //项目名称
    private String sex;         //适用性别
    private String age;         //适用年龄（范围）
    private Float price;        //价格

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    private String type;        //检查项类型，分为检查和检验两种
    private String remark;      //项目说明
    private String attention;   //注意事项

    @Override
    public String toString() {
        return "CheckItem{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", remark='" + remark + '\'' +
                ", attention='" + attention + '\'' +
                '}';
    }
}
