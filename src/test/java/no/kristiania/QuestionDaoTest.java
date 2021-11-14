package no.kristiania;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    private  QuestionDao dao = new QuestionDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedQuestion() throws SQLException {
        Question question = new Question();
        question.setQuestionText("doyoulikefootball");
        question.setQuestionTitle("he title");
        dao.save(question);
        assertThat(dao.retrieve(question.getId()))
                .hasNoNullFieldsOrPropertiesExcept("questionOptionLowLabel", "questionOptionHighLabel")
                .usingRecursiveComparison()
                .isEqualTo(question);
    }


    private Question exampleData() {
        Question questions = new Question();
        questions.setId(TestData.pickOne("0","2","3", "4", "5"));
        return questions;
    }

    @Test
    void shouldListAllQuestions() throws SQLException, IOException {
     Question questions = exampleData();
     dao.save(questions);
     Question questions1 = exampleData();
     dao.save(questions1);
     assertThat(dao.listAll())
             .extracting(Question::getId)
             .contains(questions.getId(), questions1.getId());
    }

    @Test
    void shouldCreateNewQuestion() {

    }

    public static Question exampleDataTwo() {
        Question questions = new Question();
        questions.setQuestionText(TestData.pickOne("letsgo"));
        questions.setQuestionTitle(TestData.pickOne("Basketball" ));
        return questions;
    }


}
