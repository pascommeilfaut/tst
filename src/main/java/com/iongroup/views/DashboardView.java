package com.iongroup.views;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import com.iongroup.views.browse.BrowseIssuesView;
import com.iongroup.views.components.IssuesGrid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.NonNull;

import java.util.Map;

@Route("")
@PageTitle("Dashboard")
@PermitAll
public class DashboardView extends VerticalLayout {

    public DashboardView(IssueService issueService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 header = new H1("Dashboard");
        add(header);

        HorizontalLayout dashboardRow = new HorizontalLayout();
        dashboardRow.setWidthFull();
        dashboardRow.setPadding(false);
        dashboardRow.setSpacing(true);

        Map<IssueStatus, Long> issueStatusesCount = issueService.countIssuesByStatus();

        dashboardRow.add(issueStatusStatCard(
                "New issues",
                issueStatusesCount,
                VaadinIcon.FILE_TEXT_O,
                "red",
                IssueStatus.NEW
        ));
        dashboardRow.add(issueStatusStatCard(
                "Pending issues",
                issueStatusesCount,
                VaadinIcon.ELLIPSIS_CIRCLE_O,
                "orange",
                IssueStatus.PENDING
        ));
        dashboardRow.add(issueStatusStatCard(
                "Assigned issues",
                issueStatusesCount,
                VaadinIcon.ARROW_RIGHT,
                "blue",
                IssueStatus.ASSIGNED
        ));
        dashboardRow.add(issueStatusStatCard(
                "In progress issues",
                issueStatusesCount,
                VaadinIcon.REFRESH,
                "green",
                IssueStatus.IN_PROGRESS
        ));
        dashboardRow.getChildren().forEach(c -> dashboardRow.setFlexGrow(1, c));

        IssuesGrid issuesGrid = new IssuesGrid(issueService);

        add(dashboardRow, issuesGrid);
    }

    @NonNull
    private Div issueStatusStatCard(
            @NonNull String title,
            @NonNull Map<IssueStatus, Long> issueStatusesCount,
            @NonNull VaadinIcon icon,
            @NonNull String color,
            @NonNull IssueStatus issueStatus
    ) {
        Div card = new Div();
        card.addClassName("stat-card");
        card.addClassName(color);

        Div top = new Div();
        top.addClassName("stat-top");

        Icon i = icon.create();
        i.addClassName("stat-icon");

        Span number = new Span(String.valueOf(issueStatusesCount.getOrDefault(issueStatus, 0L)));
        number.addClassName("stat-number");

        top.add(i, number);

        Span caption = new Span(title);
        caption.addClassName("stat-caption");

        Div bottom = new Div();
        bottom.addClassName("stat-link");

        Span link = new Span("View details");
        bottom.add(link);

        Icon viewDetailsIcon = VaadinIcon.ARROW_CIRCLE_RIGHT_O.create();
        bottom.add(viewDetailsIcon);

        card.add(top, caption, bottom);

        card.addSingleClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(BrowseIssuesView.class, QueryParameters.of("status", issueStatus.name())));
        });

        return card;
    }
}
