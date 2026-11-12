package com.epam.training.mateusz_smola.model;

import java.util.Objects;

public class Email {
    private String emailAddress;
    private String subject;
    private String content;

    public Email(String emailAddress, String subject, String content) {
        this.emailAddress = emailAddress;
        this.subject = subject;
        this.content = content;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Email{" +
                "emailAddress='" + emailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(emailAddress, email.emailAddress) && Objects.equals(subject, email.subject) && Objects.equals(content, email.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, subject, content);
    }
}
