package com.github.alexanderfefelov.bgbilling.plugin.search.server.dao;

import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultDAO {

    public SearchResultDAO(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public List<SearchResult> findContracts() throws SQLException {
        List<SearchResult> list = new ArrayList<>();
        return list;
    }

    private Connection connection;
    private Logger logger;

}
