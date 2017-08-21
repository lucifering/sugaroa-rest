package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "oa_user")
public class User {
    @Id
    @GeneratedValue
    private int id;

    private String account;
    @JsonIgnore
    private String password;
    private String realname;

    @JsonIgnore
    private String salt;

    @Column(name = "purview_array")
    private String purview_array;

    @Column(name = "purview_object")
    private String purview;
    private int deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return (account);
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return (password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return (salt);
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
