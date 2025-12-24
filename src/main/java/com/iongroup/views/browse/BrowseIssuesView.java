package com.iongroup.views.browse;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import com.iongroup.views.components.IssuesGrid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("/issues/browse")
@PageTitle("Browse Issues")
@PermitAll
public class BrowseIssuesView extends VerticalLayout implements BeforeEnterObserver {

    private final IssueService issueService;
    private final IssuesGrid issuesGrid;

    public BrowseIssuesView(IssueService issueService) {
        this.issueService = issueService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("Issues manager");
        issuesGrid = new IssuesGrid(issueService);

        add(title, issuesGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getLocation()
                .getQueryParameters()
                .getSingleParameter("status")
                .ifPresent(parameter -> issuesGrid.setFetchCallback((s, pageable) ->
                        issueService.findByStatus(IssueStatus.valueOfSafe(parameter), issuesGrid.getPageable())));
    }

}
