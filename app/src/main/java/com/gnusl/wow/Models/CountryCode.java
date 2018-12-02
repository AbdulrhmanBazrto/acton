package com.gnusl.wow.Models;

public class CountryCode {

    private String name;
    private String code;
    private String dialCode;

    public CountryCode(String name, String code, String dialCode) {
        this.name = name;
        this.code = code;
        this.dialCode = dialCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDialCode() {
        return dialCode;
    }
}
