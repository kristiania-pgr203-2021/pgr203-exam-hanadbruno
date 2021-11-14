package no.kristiania;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListAnswerController  implements HttpController {
        private QuestionDao QuestionDao;
        private AnswerDao answerDao;


        public ListAnswerController(AnswerDao answerDao) {
            this.answerDao = answerDao;
        }

        @Override
        public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
            String response = "";


            for(Answer answer : answerDao.listAll()){
                response += "<div>" + answer.getAnswerText() + "," + answer.getAnswerAlternative() + "</div>";
            }

            return new HttpMessage("HTTP/1.1 200 OK", response );
        }
}
