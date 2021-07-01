package com.github.alexanderfefelov.bgbilling.plugin.search.server.action;

import bitel.billing.server.ActionBase;
import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import com.github.alexanderfefelov.bgbilling.plugin.search.server.dao.SearchResultDAO;
import org.w3c.dom.Element;
import ru.bitel.bgbilling.common.BGIllegalArgumentException;

import java.sql.SQLException;
import java.util.List;

public class ActionFindContracts extends ActionBase {

    @Override
    public void doAction() throws BGIllegalArgumentException, SQLException {
        String query = this.getParameter("q");
        if (query == null) {
            throw new BGIllegalArgumentException();
        }

        SearchResultDAO dao = new SearchResultDAO(con, log);
        List<SearchResult> list = dao.findContracts(query);
        Element xmlList = createElement(rootNode, "list");
        list.forEach(record -> {
            Element element = createElement(xmlList, "record");
            element.setAttribute("contractId", record.getContractId().toString());
            element.setAttribute("contractNo", record.getContractNo());
            element.setAttribute("contractStartDate", record.getContractStartDate().toString());
            element.setAttribute("contractExpirationDate", record.getContractExpirationDate().toString());
        });

        Element xmlQuery = createElement(rootNode, "query");
        xmlQuery.setAttribute("value", query);
    }

}
