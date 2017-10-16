package com.example.demo;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    //@Value("${proxy.enabled:false}")
    private boolean enabled;

    //@Value("${proxy.hostname}")
    private String hostname;

    //@Value("${proxy.port}")
    private Integer port;
    //@Value("${proxy.user}")
    private String user;
    //@Value("${proxy.password}")
    private String password;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
