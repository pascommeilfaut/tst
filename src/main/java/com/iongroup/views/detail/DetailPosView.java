package com.iongroup.views.detail;

import com.iongroup.data.user.UserRole;
import com.iongroup.service.CityService;
import com.iongroup.service.PosService;
import com.iongroup.views.components.StatusMessage;
import com.iongroup.views.forms.add.SavePosForm;
import com.iongroup.views.forms.view.ViewPosForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import lombok.NonNull;

@Route("/pos/detail")
@PageTitle("POS details")
@PermitAll
public class DetailPosView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final PosService posService;
    private final CityService cityService;
    private final AuthenticationContext authenticationContext;

    public static boolean canEdit(@NonNull AuthenticationContext authenticationContext) {
        return authenticationContext.hasRole(UserRole.ADMIN);
    }

    public DetailPosView(PosService posService, CityService cityService, AuthenticationContext authenticationContext) {
        this.posService = posService;
        this.cityService = cityService;
        this.authenticationContext = authenticationContext;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer posId) {
        add(new H1("POS details"));

        if (canEdit(authenticationContext)) {
            SavePosForm form = new SavePosForm(posService, cityService, posId);

            Button addButton = new Button("Save changes");

            StatusMessage successMessage = new StatusMessage(form, addButton, statusMessage -> {
                statusMessage.setText(String.format("POS '%s' has been successfully saved",
                        form.getFormDataObject().orElseThrow().getName()));
            });

            add(form, addButton, successMessage);
        } else {
            add(new ViewPosForm(posService, cityService, posId));
        }
    }
}
