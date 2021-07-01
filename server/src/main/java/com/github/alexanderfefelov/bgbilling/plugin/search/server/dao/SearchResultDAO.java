package com.github.alexanderfefelov.bgbilling.plugin.search.server.dao;

import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SearchResultDAO {

    public SearchResultDAO(Connection connection, Logger logger) throws IOException {
        this.connection = connection;
        this.logger = logger;
        loadSQLQueries();
    }

    public List<SearchResult> findContracts(String query) throws SQLException {
        List<SearchResult> list = new ArrayList<>();

        try {
            long id = Long.parseLong(query);
            try (PreparedStatement statement = connection.prepareStatement(sqlQueries.getProperty("find_contracts_by_id"))) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    list.add(createRecordFromResultSet(resultSet));
                }
            } catch (SQLException sqle) {
                logger.error(sqle);
                throw sqle;
            }
        } catch (NumberFormatException nfe) {
        }

        String regex = String.join(".*",
                query.split("\\s+"));

        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.getProperty("find_contracts_by_text_parameter"))) {
            statement.setString(1, regex);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(createRecordFromResultSet(resultSet));
            }
            return list;
        } catch (SQLException sqle) {
            logger.error(sqle);
            throw sqle;
        }
    }

    private SearchResult createRecordFromResultSet(ResultSet resultSet) throws SQLException {
        SearchResult record = new SearchResult();
        record.setTrigger(resultSet.getString("trigger"));
        record.setContractId(resultSet.getInt("contractId"));
        record.setContractNo(resultSet.getString("contractNo"));
        record.setContractStartDate(resultSet.getDate("contractStartDate"));
        record.setContractExpirationDate(resultSet.getDate("contractExpirationDate"));
        return record;
    }

    private void loadSQLQueries() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("sql-queries.properties")) {
            sqlQueries.load(stream);
        }
    }

    private final Properties sqlQueries = new Properties();
    private final Connection connection;
    private final Logger logger;

}
