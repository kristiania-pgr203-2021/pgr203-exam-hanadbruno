package no.kristiania;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {
    private final HttpServer httpServer = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldCreateNewQuestion() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        httpServer.addController("/api/newQuestion", new AddQuestionController(questionDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",httpServer.getPort(),
                "/api/newQuestion",
                "title=football&text=doyoulikefootball&lowlabel=fromone&highlabel=tofive"
        );
        assertEquals(200,postClient.getStatusCode());
        Question question = questionDao.listAll().get(0);
        assertEquals("doyoulikefootball", question.getQuestionText());
    }

    @Test
    void shouldListQuestionsFromDatabase() throws SQLException, IOException {
            QuestionDao questionDao = new QuestionDao(TestData.testDataSource());

            Question question1 = QuestionDaoTest.exampleDataTwo();
            questionDao.save(question1);
            Question question2 = QuestionDaoTest.exampleDataTwo();
            questionDao.save(question2);

            httpServer.addController("/api/questions", new ListQuestionController(questionDao));

            HttpClient client = new HttpClient("localhost", httpServer.getPort(), "/api/questions");
            assertThat(client.getMessageBody())
                        .contains(question1.getQuestionText() +   question1.getQuestionTitle())
                    .contains(question2.getQuestionText()  + question2.getQuestionTitle())
             ;
        }

    @Test
    void name() throws IOException, SQLException {
        AnswerDao answerDao = new AnswerDao(TestData.testDataSource());
        httpServer.addController("/api/answersone", new AddAnswerController(answerDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",httpServer.getPort(),
                "/api/answersone",
                "title=football&text=doyoulikefootball&lowlabel=fromone&highlabel=tofive"
        );
        assertEquals(200,postClient.getStatusCode());
        Answer answer = answerDao.listAll().get(0);
        assertEquals("doyoulikefootball", answer.getAnswerText());
    }
}


