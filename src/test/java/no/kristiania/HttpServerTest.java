package no.kristiania;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

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
    void shouldReturnQuestionsFromServer() {

    }
}
