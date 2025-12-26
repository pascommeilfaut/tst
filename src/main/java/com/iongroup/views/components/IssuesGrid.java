package com.iongroup.views.components;

import com.iongroup.data.issue.IssueEntity;
import com.iongroup.service.IssueService;
import com.iongroup.views.detail.DetailIssueView;

public class IssuesGrid extends PaginatedGrid<IssueEntity> {

    public IssuesGrid(IssueService issueService) {
        super(IssueEntity.class);

        getGrid()
                .addColumn("id")
                .setHeader("Issue #")
                .setSortable(true);

        getGrid()
                .addColumn("pos.id")
                .setHeader("Pos ID")
                .setSortable(true);

        getGrid()
                .addColumn("pos.name")
                .setHeader("Pos Name")
                .setSortable(true);

        getGrid()
                .addColumn("createdBy.name")
                .setHeader("Created By")
                .setSortable(true);

        getGrid()
                .addColumn("createdAt")
                .setHeader("Date")
                .setSortable(true);

        getGrid()
                .addColumn("type.name")
                .setHeader("Issue Type")
                .setSortable(true);

        getGrid()
                .addColumn(issueEntity -> issueEntity.getStatus().getValue().getDisplayName())
                .setHeader("Status")
                .setKey("status")
                .setSortProperty("status")
                .setSortable(true);

        getGrid()
                .addColumn(issueEntity -> issueEntity.getAssignedTo().getValue().getDisplayName())
                .setHeader("Assigned To")
                .setKey("assignedTo")
                .setSortProperty("assignedTo")
                .setSortable(true);

        getGrid()
                .addColumn("memo")
                .setHeader("Memo")
                .setSortable(true);

        getGrid().addItemDoubleClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(DetailIssueView.class, event.getItem().getId()));
        });

        setFetchCallback(issueService::findByFilter);
    }

}
