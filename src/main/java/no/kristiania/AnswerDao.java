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
    private Answer answers;

    public AnswerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[]args) throws SQLException {

    }


    public void save(Answer answer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into answers ( answer_text) values (?)", Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, answer.getAnswerText());



                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    answer.setId(resultSet.getString("id"));
                }
            }
        }this.answers = answer;



    }

}


