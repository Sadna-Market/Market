package com.example.demo.configuration;

public class JsonInit {
    public JsonUser admin;
    public boolean initState;
    public String  statePath;
    public String url;

    public String spring_datasource_url;
    public String      spring_datasource_username;
    public String       spring_datasource_password;
    public String      spring_jpa_hibernate_ddl_auto;
    public String      spring_jpa_show_sql;
    public String       spring_jpa_properties_hibernate_dialect;
    public String       spring_jpa_properties_hibernate_format_sql;

    public JsonUser getAdmin()
    {
        return admin;
    }
}
