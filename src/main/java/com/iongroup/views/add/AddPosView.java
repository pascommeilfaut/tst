package com.iongroup.views.add;

import com.iongroup.data.user.UserRole;
import com.iongroup.service.CityService;
import com.iongroup.service.PosService;
import com.iongroup.views.components.StatusMessage;
import com.iongroup.views.forms.add.SavePosForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("/pos/add")
@PageTitle("Add POS")
@RolesAllowed(UserRole.ADMIN)
public class AddPosView extends VerticalLayout {

    public AddPosView(PosService posService, CityService cityService) {
        H1 header = new H1("New POS");

        SavePosForm form = new SavePosForm(posService, cityService);

        Button addButton = new Button("Add POS");

        StatusMessage successMessage = new StatusMessage(form, addButton, statusMessage -> {
            statusMessage.setText(String.format("POS '%s' has been successfully added",
                    form.getFormDataObject().orElseThrow().getName()));
        });

        add(header, form, successMessage, addButton);
    }

}
