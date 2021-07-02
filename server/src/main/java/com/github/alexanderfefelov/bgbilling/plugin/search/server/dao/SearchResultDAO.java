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

    public List<SearchResult> findContracts(String q) throws SQLException {
        List<SearchResult> list = new ArrayList<>();

        String regex = String.join(".*",
                q.split("\\s+"));

        try (PreparedStatement statement = connection.prepareStatement(sqlQueries.getProperty("find_contracts"))) {
            statement.setString(1, q);
            statement.setString(2, regex);
            statement.setString(3, regex);
            statement.setString(4, regex);
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
        record.setContractComment(resultSet.getString("contractComment"));
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
