package com.iongroup.views.detail;

import com.iongroup.service.IssueService;
import com.iongroup.service.IssueTypeService;
import com.iongroup.service.UserService;
import com.iongroup.service.mapper.IssueTypeMapper;
import com.iongroup.util.TransactionHandler;
import com.iongroup.views.components.StatusMessage;
import com.iongroup.views.forms.add.SaveIssueForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("/issues/detail")
@PageTitle("Issue details")
@PermitAll
public class DetailIssueView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final IssueService issueService;
    private final IssueTypeMapper issueTypeMapper;
    private final UserService userService;
    private final TransactionHandler transactionHandler;
    private final IssueTypeService issueTypeService;

    public DetailIssueView(IssueService issueService,
                           IssueTypeMapper issueTypeMapper,
                           IssueTypeService issueTypeService,
                           TransactionHandler transactionHandler,
                           UserService userService) {
        this.issueService = issueService;
        this.issueTypeMapper = issueTypeMapper;
        this.issueTypeService = issueTypeService;
        this.transactionHandler = transactionHandler;
        this.userService = userService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer issueId) {
        H1 header = new H1("Issue details");

        SaveIssueForm form = new SaveIssueForm(issueService, issueTypeMapper, issueTypeService, transactionHandler, userService, issueId);
        Button addButton = new Button("Save changes");

        StatusMessage successMessage = new StatusMessage(form, addButton, statusMessage ->
                statusMessage.setText("Issue has been successfully updated")
        );

        add(header, form, successMessage, addButton);
    }

}
