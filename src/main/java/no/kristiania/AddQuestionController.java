package no.kristiania;

import java.sql.SQLException;
import java.util.Map;

public class AddQuestionController implements HttpController {
    private QuestionDao questionDao;

    public AddQuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> parameters = HttpMessage.parseQueryParameters(request.getMessageBody());

        Question questions = new Question();

        questions.setId(parameters.get(0));
        questions.setQuestionTitle(parameters.get("title"));
        questions.setQuestionText(parameters.get("text"));
        questions.setQuestionOptionLowLabel(parameters.get("low_label"));
        questions.setQuestionOptionHighLabel(parameters.get("high_label"));
        questionDao.save(questions);
        return new HttpMessage("HTTP/1.1 200 OK", "We are good" );
    }
}
