package no.kristiania;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuestionDao {
    private final DataSource dataSource;

    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[]args) throws SQLException {

    }

    static DataSource createDataSource() throws IOException {
            Properties properties = new Properties();
            try (FileReader reader = new FileReader("pgr203.properties")) {
                properties.load(reader);
            }

            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(properties.getProperty(
                    "dataSource.url",
                    "jdbc:postgresql://localhost:5432/question_db"
            ));
            dataSource.setUser(properties.getProperty("dataSource.user", "question_dbuser"));
            dataSource.setPassword(properties.getProperty("dataSource.password"));
            Flyway.configure().dataSource(dataSource).load().migrate();
            return dataSource;
        }


    public void save(Question questions) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into questions (question_title, question_text, low_label, high_label) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, questions.getQuestionTitle());
                statement.setString(2, questions.getQuestionText());
                statement.setString(3, questions.getQuestionOptionLowLabel());
                statement.setString(4, questions.getQuestionOptionHighLabel());


                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    questions.setId(resultSet.getString("id"));
                }
            }
        }catch (SQLException throwables){

        }

    }

    public Question retrieve(String id) throws SQLException {
            try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from questions where id = ?")) {

                statement.setString(1,id);

                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    return mapFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    private Question mapFromResultSet(ResultSet resultSet) throws SQLException {
        Question questions = new Question();
        questions.setId(resultSet.getString("id"));
        questions.setQuestionTitle(resultSet.getString("question_title"));
        questions.setQuestionText(resultSet.getString("question_text"));
        return questions;
    }


    public List<Question> listAll() throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            {
                try (PreparedStatement statement = connection.prepareStatement("select * from questions")) {
                    try (ResultSet rs = statement.executeQuery()) {
                        ArrayList<Question> result = new ArrayList<>();
                        while (rs.next()){
                        result.add(mapFromResultSet(rs));
                        }
                 return result;   }
                }
            }
        }


    }
}
