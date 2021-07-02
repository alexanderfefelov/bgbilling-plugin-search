package com.github.alexanderfefelov.bgbilling.plugin.search.server.dao;

import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultDAO {

    public SearchResultDAO(Connection connection, Logger logger) throws IOException {
        this.connection = connection;
        this.logger = logger;
        loadSQLQueries();
    }

    public List<SearchResult> findContracts(String q) throws SQLException {
        List<SearchResult> list = new ArrayList<>();

        String terms = q;

        if (q.charAt(0) == '"' && q.charAt(q.length() - 1) == '"') {
            terms = q.substring(1, q.length() - 2);
        } else {
            terms = String.join(".*",
                    q.split("\\s+"));
        }

        try (PreparedStatement statement = connection.prepareStatement(findContractsQuery)) {
            statement.setString(1, q);
            statement.setString(2, terms);
            statement.setString(3, terms);
            statement.setString(4, terms);
            statement.setString(5, terms);
            statement.setString(6, terms);
            statement.setString(7, terms);
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
        record.setSource(resultSet.getString("source"));
        record.setContractId(resultSet.getInt("contractId"));
        record.setContractNo(resultSet.getString("contractNo"));
        record.setContractStartDate(resultSet.getDate("contractStartDate"));
        record.setContractExpirationDate(resultSet.getDate("contractExpirationDate"));
        record.setContractComment(resultSet.getString("contractComment"));
        return record;
    }

    private void loadSQLQueries() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("find-contracts.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            findContractsQuery = builder.toString();
        }
    }

    private String findContractsQuery;
    private final Connection connection;
    private final Logger logger;

}
