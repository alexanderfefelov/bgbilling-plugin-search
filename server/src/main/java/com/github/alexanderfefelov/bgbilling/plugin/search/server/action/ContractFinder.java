package com.github.alexanderfefelov.bgbilling.plugin.search.server.action;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContractFinder {

    public ContractFinder(Connection connection, Logger logger) throws IOException {
        this.connection = connection;
        this.logger = logger;
        loadQuery();
    }

    public List<SearchResult> findContracts(String q) throws SQLException {
        List<SearchResult> list = new ArrayList<>();

        String terms = q
                .replaceAll("\\\\", "")
                .replaceAll("\\s+", " ")
                .trim();
        if (terms.charAt(0) == '"' && terms.charAt(q.length() - 1) == '"') {
            terms = terms.substring(1, terms.length() - 2);
        } else {
            Permutator<String> permutator = new Permutator<>();
            List<List<String>> permutations = permutator.permutate(Arrays.stream(terms.split("\\s+")).limit(3).collect(Collectors.toList()));
            terms = permutations.stream()
                    .map(x -> String.join(".*", x))
                    .collect(Collectors.joining("|"));
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
        record.setContractPostpaidMode(resultSet.getBoolean("contractPostpaidMode"));
        record.setContractBalance(resultSet.getDouble("contractBalance"));
        record.setContractLimit(resultSet.getDouble("contractLimit"));
        record.setContractPricingPlans(resultSet.getString("contractPricingPlans"));
        return record;
    }

    private void loadQuery() throws IOException {
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
