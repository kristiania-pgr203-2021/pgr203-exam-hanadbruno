package no.kristiania;

public class Question {
private String id;
private String questionTitle;
private String questionText;
private String questionOptionLowLabel;
private String questionOptionHighLabel;

    public String getQuestionOptionHighLabel() {
        return questionOptionHighLabel;
    }

    public void setQuestionOptionHighLabel(String questionOptionHighLabel) {
        this.questionOptionHighLabel = questionOptionHighLabel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionOptionLowLabel() {
        return questionOptionLowLabel;
    }

    public void setQuestionOptionLowLabel(String questionOptionLowLabel) {
        this.questionOptionLowLabel = questionOptionLowLabel;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionText='" + questionText + '\'' +
                ", questionOptionLowLabel='" + questionOptionLowLabel + '\'' +
                ", questtionOptionHighLabel='" + questionOptionHighLabel + '\'' +
                '}';
    }
}
