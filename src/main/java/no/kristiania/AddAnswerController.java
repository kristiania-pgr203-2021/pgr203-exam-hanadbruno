package no.kristiania;

import java.sql.SQLException;
import java.util.Map;

public class AddAnswerController implements HttpController {
        private AnswerDao answerDao;

        public AddAnswerController(AnswerDao answerDao) {
            this.answerDao = answerDao;
        }

        @Override
        public HttpMessage handle(HttpMessage request) throws SQLException {
            Map<String, String> parameters = HttpMessage.parseQueryParameters(request.getMessageBody());

            Answer answers = new Answer();

            answers.setId(parameters.get(0));
            answers.setUserName(parameters.get("user_name"));
            answers.setAnswerText(parameters.get("text"));
            answers.setAnswerAlternative(parameters.get("answer_alternative"));
            answerDao.save(answers);
            return new HttpMessage("HTTP/1.1 200 OK", "We are good" );
        }
}
