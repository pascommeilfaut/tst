package com.iongroup.views.add;

import com.iongroup.data.pos.PosEntity;
import com.iongroup.service.IssueService;
import com.iongroup.service.IssueTypeService;
import com.iongroup.service.PosService;
import com.iongroup.service.UserService;
import com.iongroup.service.mapper.IssueTypeMapper;
import com.iongroup.util.TransactionHandler;
import com.iongroup.views.components.PaginatedGrid;
import com.iongroup.views.components.StatusMessage;
import com.iongroup.views.forms.add.SaveIssueForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("/issues/add")
@PageTitle("Add Issue")
@PermitAll
public class AddIssueView extends VerticalLayout {

    public AddIssueView(
            IssueService issueService,
            IssueTypeMapper issueTypeMapper,
            IssueTypeService issueTypeService,
            TransactionHandler transactionHandler,
            UserService userService,
            PosService posService
    ) {
        H1 h1 = new H1("Add Issue");
        SaveIssueForm saveIssueForm = new SaveIssueForm(issueService, issueTypeMapper, issueTypeService, transactionHandler, userService, null);
        saveIssueForm.setVisible(false);
        Button addIssueButton = new Button("Add Issue");
        addIssueButton.setVisible(false);

        StatusMessage successMessage = new StatusMessage(saveIssueForm, addIssueButton, statusMessage ->
                statusMessage.setText("Issue has been successfully added")
        );

        PaginatedGrid<PosEntity> paginatedGrid = new PaginatedGrid<>(PosEntity.class);
        paginatedGrid.getGrid().addItemDoubleClickListener(e -> {
            Integer posId = e.getItem().getId();
            saveIssueForm.setPosId(posId);
            saveIssueForm.setVisible(true);
            addIssueButton.setVisible(true);
        });

        paginatedGrid.getGrid().setHeight("300px");

        paginatedGrid.getGrid()
                .addColumn("id")
                .setHeader("Pos ID")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("name")
                .setHeader("Name")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("telephone")
                .setHeader("Telephone")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("cellphone")
                .setHeader("Cellphone")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("address")
                .setHeader("Address")
                .setSortable(true);

        paginatedGrid.setFetchCallback(posService::findByFilter);

        add(
                h1,
                paginatedGrid,
                new Hr(),
                saveIssueForm,
                successMessage,
                addIssueButton
        );
    }

}
