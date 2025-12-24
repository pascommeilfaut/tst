package com.iongroup.views.add;

import com.iongroup.data.user.UserRole;
import com.iongroup.service.UserService;
import com.iongroup.views.forms.add.SaveUserForm;
import com.iongroup.views.components.StatusMessage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("/users/add")
@PageTitle("Add User")
@RolesAllowed(UserRole.ADMIN)
public class AddUserView extends VerticalLayout {

    public AddUserView(UserService userService) {
        H1 h1 = new H1("Add User");
        SaveUserForm saveUserForm = new SaveUserForm(userService);
        Button addUserButton = new Button("Add User");

        StatusMessage successMessage = new StatusMessage(saveUserForm, addUserButton, statusMessage -> {
            statusMessage.setText(String.format("User %s has been successfully added",
                    saveUserForm.getFormDataObject().orElseThrow().getLogin()));
        });

        add(h1);
        add(saveUserForm);
        add(successMessage);
        add(addUserButton);
    }

}
