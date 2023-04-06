package net.yousra.dore_meet;

public class Musicien {

    String name,email,password,age,specialite,localisa;


    public Musicien() {
    }

    public Musicien(String name, String email, String password,
                    String age, String specialite, String localisa) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.specialite = specialite;
        this.localisa = localisa;
    }

    public Musicien(String name, String email, String age, String specialite) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.specialite = specialite;
    }

    public Musicien(String name, String specialite) {
        this.name = name;
        this.specialite = specialite;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getLocalisa() {
        return localisa;
    }

    public void setLocalisa(String localisa) {
        this.localisa = localisa;
    }
}
