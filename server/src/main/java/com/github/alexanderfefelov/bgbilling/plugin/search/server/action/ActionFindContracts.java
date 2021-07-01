package com.github.alexanderfefelov.bgbilling.plugin.search.server.action;

import bitel.billing.server.ActionBase;
import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import com.github.alexanderfefelov.bgbilling.plugin.search.server.dao.SearchResultDAO;
import org.w3c.dom.Element;

import java.util.List;

public class ActionFindContracts extends ActionBase {

    @Override
    public void doAction() throws Exception {
        SearchResultDAO dao = new SearchResultDAO(con, log);
        List<SearchResult> list = dao.findContracts();
        Element xml = createElement(rootNode, "list");
        list.forEach(record -> {
            Element element = createElement(xml, "record");
            element.setAttribute("contractId", record.getContractId().toString());
        });
    }

}
