package com.github.alexanderfefelov.bgbilling.plugin.search.server.dao;

import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchResultDAO {

    public SearchResultDAO(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public List<SearchResult> findContracts(String query) throws SQLException {
        List<SearchResult> list = new ArrayList<>();

        try {
            long id = Long.parseLong(query);
            try (PreparedStatement statement = connection.prepareStatement(QUERY_FIND_CONTRACT_BY_ID)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    SearchResult record = new SearchResult();
                    record.setContractId(resultSet.getLong("contractId"));
                    record.setContractNo(resultSet.getString("contractNo"));
                    record.setContractStartDate(resultSet.getDate("contractStartDate"));
                    record.setContractExpirationDate(resultSet.getDate("contractExpirationDate"));
                    list.add(record);
                }
                return list;
            } catch (SQLException sqle) {
                logger.error(sqle);
                throw sqle;
            }
        } catch (NumberFormatException nfe) {
        }

        return list;
    }

    public static final String QUERY_FIND_CONTRACT_BY_ID = String.join("\n",
            "select",
            "  c.id as 'contractId',",
            "  c.title as 'contractNo',",
            "  c.date1 as 'contractStartDate',",
            "  coalesce(c.date2, '2042-04-01') as 'contractExpirationDate'",
            "from",
            "  contract c",
            "where",
            "  c.id = ?"
    );

    private final Connection connection;
    private final Logger logger;

}
