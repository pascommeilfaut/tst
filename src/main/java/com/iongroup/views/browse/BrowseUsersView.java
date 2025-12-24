package com.iongroup.views.browse;

import com.iongroup.data.user.UserEntity;
import com.iongroup.service.UserService;
import com.iongroup.views.components.PaginatedGrid;
import com.iongroup.views.detail.DetailUserView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("/users/browse")
@PageTitle("Browse Users")
@RolesAllowed("ADMIN")
public class BrowseUsersView extends VerticalLayout {

    public BrowseUsersView(UserService userService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("Users manager");

        PaginatedGrid<UserEntity> paginatedGrid = new PaginatedGrid<>(UserEntity.class);

        paginatedGrid.getGrid()
                .addColumn("name")
                .setHeader("Name")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("email")
                .setHeader("Email")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("login")
                .setHeader("Login")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn("telephone")
                .setHeader("Telephone")
                .setSortable(true);

        paginatedGrid.getGrid()
                .addColumn(user -> user.getType().getValue().getDisplayName())
                .setKey("type")
                .setSortProperty("type")
                .setHeader("User Type")
                .setSortable(true);

        paginatedGrid.getGrid().addComponentColumn(p -> {
            Button detailButton = new Button(VaadinIcon.EDIT.create());
            detailButton.addClickListener(e ->
                    getUI().orElseThrow().navigate(DetailUserView.class, p.getId()));
            return detailButton;
        });

        paginatedGrid.setFetchCallback(userService::findByFilter);

        add(title, paginatedGrid);
    }

}