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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main extends BGUTabPanel {

    public Main() {
        super();
        tabTitle = "Поиск";
    }

    @Override
    protected void jbInit() throws Exception {
        input = new JTextField(20);

        button = new JButton("Найти");
        button.addActionListener(e -> onFindButtonPressed());

        label = new JLabel("", SwingConstants.RIGHT);

        model = new BGTableModel<SearchResult>("SearchResult") {
            protected void initColumns() {
                addColumn("", 0, 0, 0, "contractId", false);
                addColumn("Контракт", "contractNo", true);
                addColumn("Дата начала", "contractStartDate", true);
                addColumn("Дата окончания", "contractExpirationDate", true);
                addColumn("Комментарий", "contractComment", true);
                addColumn("Где найдено", "trigger", true);
            }
        };
        table = new BGUTable(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openContract(model.getSelectedRow().getContractId());
                }

            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 0, 0);

        JPanel controls = new JPanel(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(input, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(new JPanel(), constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(button, constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(label, constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controls.add(new JPanel(), constraints);

        setLayout(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 5;
        constraints.ipady = 5;
        add(controls, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 5;
        constraints.ipady = 5;
        add(new JScrollPane(table), constraints);

        getRootPane().setDefaultButton(button);
        input.requestFocusInWindow();
    }

    private void onFindButtonPressed() {
        String q = input.getText();
        Request request = new Request();
        request.setModule("com.github.alexanderfefelov.bgbilling.plugin.search");
        request.setAction("FindContracts");
        request.setAttribute("q", q);
        Document document = TransferManager.getDocument(request);
        List<SearchResult> list = new ArrayList<>();
        XMLUtils.selectElements(document, "//list/record").forEach(element -> list.add(createRecordFromElement(element)));
        model.setData(list);
        label.setText("<html>Найдено <font size=\"5\">" + list.size() + "</font> по запросу <font size=\"5\">" + q + "</font>");
    }

    private SearchResult createRecordFromElement(Element element) {
        SearchResult record = new SearchResult();
        record.setTrigger(element.getAttribute("trigger"));
        record.setContractId(Integer.parseInt(element.getAttribute("contractId")));
        record.setContractNo(element.getAttribute("contractNo"));
        if (!element.getAttribute("contractStartDate").equals(EMPTY_DATE)) {
            try {
                record.setContractStartDate(dateFormat.parse(element.getAttribute("contractStartDate")));
            } catch (ParseException e) {
            }
        }
        if (!element.getAttribute("contractExpirationDate").equals(EMPTY_DATE)) {
            try {
                record.setContractExpirationDate(dateFormat.parse(element.getAttribute("contractExpirationDate")));
            } catch (ParseException e) {
            }
        }
        record.setContractComment(element.getAttribute("contractComment"));
        return record;
    }

    private static final String EMPTY_DATE = "2042-04-01";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    private JTextField input;
    private JButton button;
    private JLabel label;
    private BGUTable table;
    private BGTableModel<SearchResult> model;

}
