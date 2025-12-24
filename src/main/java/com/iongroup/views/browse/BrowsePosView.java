package com.iongroup.views.browse;

import com.iongroup.data.pos.PosEntity;
import com.iongroup.service.IssueService;
import com.iongroup.service.PosService;
import com.iongroup.views.components.PaginatedGrid;
import com.iongroup.views.detail.DetailPosView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@Route("/pos/browse")
@PageTitle("Browse POS")
@PermitAll
public class BrowsePosView extends VerticalLayout {

    public BrowsePosView(PosService posService, AuthenticationContext authenticationContext, IssueService issueService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("POS manager");

        PaginatedGrid<PosEntity> paginatedGrid = new PaginatedGrid<>(PosEntity.class);

        paginatedGrid.getGrid().addColumn(PosEntity::getName)
                .setHeader("Name")
                .setSortProperty("name")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(PosEntity::getTelephone)
                .setHeader("Telephone")
                .setSortProperty("telephone")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(PosEntity::getCellphone)
                .setHeader("Cellphone")
                .setSortProperty("cellphone")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(PosEntity::getCellphone)
                .setHeader("Cellphone")
                .setSortProperty("cellphone")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(PosEntity::getAddress)
                .setHeader("Address")
                .setSortProperty("address")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(p -> p.getCity().getName())
                .setHeader("City")
                .setSortProperty("city.name")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(PosEntity::getModel)
                .setHeader("Model")
                .setSortProperty("model")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(PosEntity::getBrand)
                .setHeader("Brand")
                .setSortProperty("brand")
                .setSortable(true);

        paginatedGrid.getGrid().addColumn(p -> p.getConnectionType().getValue().getDisplayName())
                .setHeader("Connection")
                .setSortable(false);

        paginatedGrid.getGrid()
                .addColumn(posEntity -> {
                    long issueCount = issueService.countIssuesByPosId(posEntity.getId());
                    return issueCount > 0 ?
                            issueCount + (issueCount != 1 ? " issues" : " issue") :
                            "No issues";
                })
                .setHeader("Status")
                .setSortable(true);

        paginatedGrid.getGrid().addComponentColumn(p -> {
            Icon icon = DetailPosView.canEdit(authenticationContext)
                    ? VaadinIcon.EDIT.create()
                    : VaadinIcon.EYE.create();
            Button detailButton = new Button(icon);
            detailButton.addClickListener(e ->
                    getUI().orElseThrow().navigate(DetailPosView.class, p.getId()));
            return detailButton;
        });

        paginatedGrid.setFetchCallback(posService::findByFilter);

        add(title, paginatedGrid);
    }

}
