package com.iongroup.views;

import com.iongroup.data.user.UserRole;
import com.iongroup.views.add.AddIssueView;
import com.iongroup.views.add.AddPosView;
import com.iongroup.views.add.AddUserView;
import com.iongroup.views.browse.BrowseIssuesView;
import com.iongroup.views.browse.BrowsePosView;
import com.iongroup.views.browse.BrowseUsersView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;

@Layout
@PermitAll
public class MainLayout extends AppLayout {

    public MainLayout(AuthenticationContext authenticationContext) {
        SideNav nav = new SideNav();

        SideNavItem dashboard = new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create());

        SideNavItem posSection = new SideNavItem("POS");
        posSection.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());

        SideNavItem issuesSection = new SideNavItem("Issues");
        issuesSection.setPrefixComponent(VaadinIcon.WRENCH.create());
        issuesSection.addItem(new SideNavItem("Add Issue", AddIssueView.class));
        issuesSection.addItem(new SideNavItem("Browse Issues", BrowseIssuesView.class));

        nav.addItem(dashboard, posSection, issuesSection);

        if (authenticationContext.hasRole(UserRole.ADMIN)) {
            posSection.addItem(new SideNavItem("Add POS", AddPosView.class));

            SideNavItem usersSection = new SideNavItem("Users");
            usersSection.setPrefixComponent(VaadinIcon.GROUP.create());
            usersSection.addItem(new SideNavItem("Add User", AddUserView.class));
            usersSection.addItem(new SideNavItem("Browse Users", BrowseUsersView.class));

            nav.addItem(usersSection);
        }

        posSection.addItem(new SideNavItem("Browse POS", BrowsePosView.class));

        H3 title = new H3("Libra Dashboard");
        title.getStyle().set("padding-left", "20px");

        Span username = new Span(SecurityContextHolder.getContext().getAuthentication().getName());

        Button logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(event -> authenticationContext.logout());

        HorizontalLayout rightHeader = new HorizontalLayout(username, logoutButton);
        rightHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        rightHeader.setSpacing(true);
        rightHeader.getStyle().set("margin-left", "auto");
        rightHeader.getStyle().set("padding-right", "20px");

        addToNavbar(title, rightHeader);
        addToDrawer(nav);
    }

}
