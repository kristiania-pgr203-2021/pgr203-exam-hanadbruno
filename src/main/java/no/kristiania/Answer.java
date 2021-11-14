package no.kristiania;

public class Answer {
    private String id;
    private String answerText;
    private String answerAlternative;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id='" + id + '\'' +
                ", answerText='" + answerText + '\'' +
                ", answerAlternative='" + answerAlternative + '\'' +
                '}';
    }
}
