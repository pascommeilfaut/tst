package com.iongroup.views.detail;

import com.iongroup.data.user.UserRole;
import com.iongroup.service.UserService;
import com.iongroup.views.components.StatusMessage;
import com.iongroup.views.forms.add.SaveUserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("/users/detail")
@PageTitle("User details")
@RolesAllowed(UserRole.ADMIN)
public class DetailUserView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final UserService userService;

    public DetailUserView(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer userId) {
        H1 header = new H1("User details");

        SaveUserForm form = new SaveUserForm(userService, userId);

        Button addButton = new Button("Save changes");

        StatusMessage successMessage = new StatusMessage(form, addButton, statusMessage -> {
            statusMessage.setText(String.format("User '%s' has been successfully saved",
                    form.getFormDataObject().orElseThrow().getName()));
        });

        add(header, form, successMessage, addButton);
    }
}
