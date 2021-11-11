package no.kristiania;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    private final DataSource dataSource;

    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[]args) throws SQLException {

    }


    public static DataSource createDataSource() throws IOException {


        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/question_db");
        dataSource.setUser("question_dbuser");
        dataSource.setPassword("oltorino2006");
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

    public List<Question> listByLastName(String lastName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from questions where question_text = ?")) {

                statement.setString(1, lastName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<Question> questions = new ArrayList<>();


                    while (resultSet.next()) {
                        questions.add(mapFromResultSet(resultSet));
                    }        return questions;
                }
            }
        }


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
