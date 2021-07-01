package com.github.alexanderfefelov.bgbilling.plugin.search.server.action;

import bitel.billing.server.ActionBase;
import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import com.github.alexanderfefelov.bgbilling.plugin.search.server.dao.SearchResultDAO;
import org.w3c.dom.Element;
import ru.bitel.bgbilling.common.BGIllegalArgumentException;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class ActionFindContracts extends ActionBase {

    @Override
    public void doAction() throws BGIllegalArgumentException, IOException, SQLException {
        String q = this.getParameter("q");
        if (q == null) {
            throw new BGIllegalArgumentException();
        }

        String query = URLDecoder.decode(q, StandardCharsets.UTF_8.name());

        SearchResultDAO dao = new SearchResultDAO(con, log);
        List<SearchResult> list = dao.findContracts(query);
        Element xmlList = createElement(rootNode, "list");
        list.forEach(record -> createElementFromRecord(xmlList, record));

        Element xmlQuery = createElement(rootNode, "query");
        xmlQuery.setAttribute("value", query);
    }

    private Element createElementFromRecord(Element xmlList, SearchResult record) {
        Element element = createElement(xmlList, "record");
        element.setAttribute("trigger", record.getTrigger());
        element.setAttribute("contractId", record.getContractId().toString());
        element.setAttribute("contractNo", record.getContractNo());
        element.setAttribute("contractStartDate", record.getContractStartDate().toString());
        element.setAttribute("contractExpirationDate", record.getContractExpirationDate().toString());
        return element;
    }

}
