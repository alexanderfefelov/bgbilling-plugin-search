package com.github.alexanderfefelov.bgbilling.plugin.search.client;

import bitel.billing.module.admin.TransferManager;
import bitel.billing.module.common.Request;
import com.github.alexanderfefelov.bgbilling.plugin.search.common.model.SearchResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.bitel.bgbilling.client.common.BGControlPanelPages;
import ru.bitel.bgbilling.client.common.BGUTabPanel;
import ru.bitel.bgbilling.client.common.BGUTable;
import ru.bitel.common.XMLUtils;
import ru.bitel.common.client.table.BGTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends BGUTabPanel {

    public Main() throws Exception {
        super();
        tabTitle = "Поиск";
    }

    @Override
    protected void jbInit() throws Exception {
        JPanel controls = new JPanel(new GridBagLayout());

        input = new JTextField(20);
        controls.add(input, new GridBagConstraints(0, 0, 1, 1, 0, 0, 10, 0,
                new Insets(0, 5, 5, 0), 0, 0));

        button = new JButton("Найти");
        button.addActionListener(e -> onFindButtonPressed());
        controls.add(button, new GridBagConstraints(1, 0, 1, 1, 0, 0, 10, 1,
                new Insets(0, 5, 5, 5), 0, 0));
        controls.add(new JPanel(), new GridBagConstraints(2, 0, 1, 1, 1, 0, 10, 2,
                new Insets(0, 15, 0, 0), 0, 0));

        page = new BGControlPanelPages();
        controls.add(page, new GridBagConstraints(4, 0, 1, 1, 0, 0, 10, 0, new
                Insets(0, 15, 5, 5), 0, 0));

        setLayout(new GridBagLayout());

        add(controls, new GridBagConstraints(0, 0, 1, 1, 1, 0, 10, 1,
                new Insets(5, 0, 5, 0), 0, 0));

        model = new BGTableModel<SearchResult>("SearchResult") {
            protected void initColumns() {
                addColumn("", 0, 0, 0, "contractId", false);
                addColumn("Контракт", 100, 100, 100, "contractNo", true);
                addColumn("Дата начала", 100, 100, 100, "contractStartDate", true);
                addColumn("Дата окончания", 100, 100, 100, "contractExpirationDate", true);
                addColumn("Где найдено", 100, 100, 100, "trigger", true);
            }
        };
        table = new BGUTable(model);
        add(new JScrollPane(table), new GridBagConstraints(0, 2, 1, 1, 1, 1, 10, 1,
                new Insets(0, 5, 5, 5), 0, 0));

        page.init();
    }

    private void onFindButtonPressed() {
        button.setEnabled(false);

        Request request = new Request();
        request.setModule("com.github.alexanderfefelov.bgbilling.plugin.search");
        request.setAction("FindContracts");
        request.setAttribute("q", input.getText());
        Document document = TransferManager.getDocument(request);
        List<SearchResult> list = new ArrayList<>();
        XMLUtils.selectElements(document, "//list/record").forEach(element -> list.add(createRecordFromeLEMENT(element)));
        model.setData(list);

        button.setEnabled(true);
    }

    private SearchResult createRecordFromeLEMENT(Element element) {
        SearchResult record = new SearchResult();
        record.setTrigger(element.getAttribute("trigger"));
        // record.setContractId(element.getAttribute("contractId"));
        record.setContractNo(element.getAttribute("contractNo"));
        //record.setContractStartDate(element.getAttribute(("contractStartDate"));
        //record.setContractExpirationDate(element.getAttribute("contractExpirationDate"));
        return record;
    }

    private JTextField input;
    private JButton button;
    private BGControlPanelPages page;
    private BGUTable table;
    private BGTableModel<SearchResult> model;

}
