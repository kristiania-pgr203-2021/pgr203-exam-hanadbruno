package no.kristiania;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDao {
    private final DataSource dataSource;

    public AnswerDao(DataSource dataSource) {
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

    public void save(Answer answer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into answers (user_name, answer_text, answer_alternative) values (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, answer.getUserName());
                statement.setString(2, answer.getAnswerText());
                statement.setString(3, answer.getAnswerAlternative());



                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    answer.setId(resultSet.getString("id"));
                }
            }
        }catch (SQLException throwables){

        }

    }

    public Answer retrieve(String id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from answers where id = ?")) {

                statement.setString(1,id);

                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    return mapFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    private Answer mapFromResultSet(ResultSet resultSet) throws SQLException {
        Answer answer = new Answer();
        answer.setId(resultSet.getString("id"));
        answer.setUserName(resultSet.getString("user_name"));
        answer.setAnswerText(resultSet.getString("answer_text"));
        answer.setAnswerAlternative(resultSet.getString("answer_alternative"));
        return answer;
    }

    public List<Answer> listByUserName(String userName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from answers where user_name = ?")) {

                statement.setString(1, userName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<Answer> answers = new ArrayList<>();


                    while (resultSet.next()) {
                        answers.add(mapFromResultSet(resultSet));
                    }        return answers;
                }
            }
        }


    }

    public List<Answer> listAll() throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            {
                try (PreparedStatement statement = connection.prepareStatement("select * from answers")) {
                    try (ResultSet rs = statement.executeQuery()) {
                        ArrayList<Answer> result = new ArrayList<>();
                        while (rs.next()){
                            result.add(mapFromResultSet(rs));
                        }
                        return result;   }
                }
            }
        }


    }
}

