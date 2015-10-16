/*
*/
package org.yy.framework.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
*/
@SuppressWarnings("serial")
@XStreamAlias("user")
public class User implements Serializable {
    
    private String name = "周亮";
    
    private int age = 33;
    
    private Integer hight = 175;
    
    private Long money = 12312312l;
    
    private List<String> contacts = new ArrayList<String>();
    
    private List<Address> addresses = new ArrayList<Address>();
    
    private School school = new School();
    
    /** 
    <默认构造函数>
    */
    public User() {
    }
    
    /**
    * @return 返回 name
    */
    public String getName() {
        return name;
    }
    
    /**
    * @param 对name进行赋值
    */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
    * @return 返回 age
    */
    public int getAge() {
        return age;
    }
    
    /**
    * @param 对age进行赋值
    */
    public void setAge(int age) {
        this.age = age;
    }
    
    /**
    * @return 返回 hight
    */
    public Integer getHight() {
        return hight;
    }
    
    /**
    * @param 对hight进行赋值
    */
    public void setHight(Integer hight) {
        this.hight = hight;
    }
    
    /**
    * @return 返回 money
    */
    public Long getMoney() {
        return money;
    }
    
    /**
    * @param 对money进行赋值
    */
    public void setMoney(Long money) {
        this.money = money;
    }
    
    /**
    * @return 返回 contacts
    */
    public List<String> getContacts() {
        return contacts;
    }
    
    /**
    * @param 对contacts进行赋值
    */
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }
    
    /**
    * @return 返回 addresses
    */
    public List<Address> getAddresses() {
        return addresses;
    }
    
    /**
    * @param 对addresses进行赋值
    */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    /**
    * @return 返回 school
    */
    public School getSchool() {
        return school;
    }
    
    /**
    * @param 对school进行赋值
    */
    public void setSchool(School school) {
        this.school = school;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + ", hight=" + hight + ", money=" + money + ", contacts="
            + contacts + ", addresses=" + addresses + ", school=" + school + "]";
    };
    
}
