package com.github.alexanderfefelov.bgbilling.plugin.search.server.action;

import bitel.billing.server.ActionBase;
import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import org.apache.log4j.Logger;
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

        String query = URLDecoder.decode(q, StandardCharsets.UTF_8.name())
                .replaceAll("\\\\", "")
                .replaceAll("\\s+", " ")
                .trim();

        ContractFinder dao = new ContractFinder(con, log);
        List<SearchResult> list = dao.findContracts(query);
        logger.info("query: " + query + ", found: " + list.size());
        Element xmlList = createElement(rootNode, "list");
        list.forEach(record -> createElementFromRecord(xmlList, record));

        Element xmlQuery = createElement(rootNode, "query");
        xmlQuery.setAttribute("value", query);
    }

    private Element createElementFromRecord(Element xmlList, SearchResult record) {
        Element element = createElement(xmlList, "record");
        element.setAttribute("source", record.getSource());
        element.setAttribute("contractId", record.getContractId().toString());
        element.setAttribute("contractNo", record.getContractNo());
        element.setAttribute("contractStartDate", record.getContractStartDate().toString());
        element.setAttribute("contractExpirationDate", record.getContractExpirationDate().toString());
        element.setAttribute("contractComment", record.getContractComment());
        element.setAttribute("contractPostpaidMode", record.getContractPostpaidMode().toString());
        element.setAttribute("contractBalance", record.getContractBalance().toString());
        element.setAttribute("contractBalanceChangedAt", record.getContractBalanceChangedAt());
        element.setAttribute("contractLimit", record.getContractLimit().toString());
        element.setAttribute("contractPricingPlans", record.getContractPricingPlans());
        return element;
    }

    private final Logger logger = Logger.getLogger(getClass());
}
