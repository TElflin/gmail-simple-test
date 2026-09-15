package com.epam.training.mateusz_smola.model;

import java.util.Objects;

public class Email {
    private User user;
    private String pageUrl;

    public Email(User user, String pageUrl) {
        this.user = user;
        this.pageUrl = pageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    @Override
    public String toString() {
        return "Email{" +
                "user=" + user +
                ", pageUrl='" + pageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(user, email.user) && Objects.equals(pageUrl, email.pageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, pageUrl);
    }
}
