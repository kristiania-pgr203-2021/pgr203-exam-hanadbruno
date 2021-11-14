package no.kristiania;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class ListQuestionController implements HttpController {
    private QuestionDao QuestionDao;

    public ListQuestionController(QuestionDao questionDao) {
        this.QuestionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        String response = "";


        for(Question question : QuestionDao.listAll()){
            response +=  "<div>" + question.getQuestionText() +  question.getQuestionTitle() + "<div/>";
        }

        return new HttpMessage("HTTP/1.1 200 OK", response);
    }
}
