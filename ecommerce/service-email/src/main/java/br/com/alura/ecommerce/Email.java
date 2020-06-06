package br.com.alura.ecommerce;

public class Email {

    private final String subject;
    private final String body;

    private Email(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public static Email of(String subject, String body) {
        return new Email(subject, body);
    }

    public String getSubject() {
        return this.subject;
    }

    public String getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        return "{" +
            " subject='" + getSubject() + "'" +
            ", body='" + getBody() + "'" +
            "}";
    }


}