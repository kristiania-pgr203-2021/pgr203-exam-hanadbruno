package no.kristiania;

import java.sql.SQLException;

public interface HttpController {
    HttpMessage handle(HttpMessage request) throws SQLException;
}
