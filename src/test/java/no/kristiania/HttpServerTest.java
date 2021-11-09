package no.kristiania;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {
    private final HttpServer httpServer = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldCreateNewQuestion() throws IOException {
        HttpPostClient postClient = new HttpPostClient("localhost",httpServer.getPort(),"/api/newQuestion",
                "title=football&text=doyoulikefootball&lowlabel=fromone&highlabel=tofive");
        assertEquals(404,postClient.getStatusCode());
        Question question = httpServer.getQuestionsList().get(0);
        assertEquals("Hamburger", question.getQuestionText());
    }

    @Test
    void shouldReturnQuestionsFromServer() {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        questionDao.save("Football");
    }
}
