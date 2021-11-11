package no.kristiania;

public class Answer {
    private String id;
    private String userName;
    private String answerText;
    private String answerAlternative;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getAnswerAlternative() {
        return answerAlternative;
    }

    public void setAnswerAlternative(String answerAlternative) {
        this.answerAlternative = answerAlternative;
    }
    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", answerText='" + answerText + '\'' +
                ", answerAlternative='" + answerAlternative + '\'' +
                '}';
    }
}
