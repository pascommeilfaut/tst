package com.iongroup.views;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
@StyleSheet("../frontend/themes/default/styles.css")
public class LoginView extends Main implements BeforeEnterObserver {

    private final LoginForm login;

    public LoginView() {
        login = new LoginForm();
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Welcome to Libra");
        i18n.getForm().setSubmit("Sign In");
        login.setI18n(i18n);

        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("login-screen");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.add(login);
        layout.setSizeFull();

        add(layout);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
