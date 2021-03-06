package com.epam.esm.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Audited
public class User extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "user must have login")
    @NotEmpty(message = "login must be not empty")
    @Column(name = "login")
    private String login;

    @NotNull(message = "user must have password")
    @NotEmpty(message = "password must be not empty")
    @Column(name = "password")
    private String password;

    @NotNull(message = "user must have name")
    @NotEmpty(message = "name must be not empty")
    @Column(name = "name")
    private String name;

    @NotNull(message = "user must have age")
    @Min(value = 1, message = "age must be greater than 0")
    @Column(name = "age")
    private int age;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Order> orders = new HashSet<>();

    public User() {
    }

    public User(int id, String login, String password, String name, int age) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && age == user.age && login.equals(user.login) && password.equals(user.password) && name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, name, age);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
