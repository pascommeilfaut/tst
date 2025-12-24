package com.iongroup.views.forms.view;

import com.iongroup.service.CityService;
import com.iongroup.service.PosService;
import com.iongroup.service.dto.SavePosDto;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewPosForm extends Composite<FormLayout> {

    protected final Binder<SavePosDto> binder = new Binder<>();
    private final CityService cityService;

    public ViewPosForm(PosService posService, CityService cityService, @NonNull Integer posId) {
        this.cityService = cityService;

        SavePosDto pos = posService.findSavePosDtoById(posId).orElseThrow();
        binder.setBean(pos);

        TextField nameField = new TextField("Name");
        nameField.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());
        binder.forField(nameField).bindReadOnly(SavePosDto::getName);

        TextField telephoneField = new TextField("Telephone");
        telephoneField.setPrefixComponent(VaadinIcon.PHONE_LANDLINE.create());
        binder.forField(telephoneField).bindReadOnly(SavePosDto::getTelephone);

        TextField cellphoneField = new TextField("Cellphone");
        cellphoneField.setPrefixComponent(VaadinIcon.PHONE.create());
        binder.forField(cellphoneField).bindReadOnly(SavePosDto::getCellphone);

        TextField addressField = new TextField("Address");
        addressField.setPrefixComponent(VaadinIcon.MAP_MARKER.create());
        binder.forField(addressField).bindReadOnly(SavePosDto::getAddress);

        TextField cityField = new TextField("City");
        cityField.setPrefixComponent(VaadinIcon.MAP_MARKER.create());
        binder.forField(cityField).bindReadOnly(this::getCityName);

        TextField modelField = new TextField("Model");
        modelField.setPrefixComponent(VaadinIcon.BARCODE.create());
        binder.forField(modelField).bindReadOnly(SavePosDto::getModel);

        TextField brandField = new TextField("Brand");
        brandField.setPrefixComponent(VaadinIcon.BARCODE.create());
        binder.forField(brandField).bindReadOnly(SavePosDto::getBrand);

        TextField connectionTypeField = new TextField("Connection type");
        connectionTypeField.setPrefixComponent(VaadinIcon.BARCODE.create());
        binder.forField(connectionTypeField).bindReadOnly(p -> p.getConnectionType().getDisplayName());

        TimePicker morningOpeningField = new TimePicker("Morning opening");
        morningOpeningField.setReadOnly(true);
        morningOpeningField.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(morningOpeningField).bindReadOnly(SavePosDto::getMorningOpening);

        TimePicker morningClosingField = new TimePicker("Morning closing");
        morningClosingField.setReadOnly(true);
        morningClosingField.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(morningClosingField).bindReadOnly(SavePosDto::getMorningClosing);

        TimePicker afternoonOpeningField = new TimePicker("Afternoon opening");
        afternoonOpeningField.setReadOnly(true);
        afternoonOpeningField.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(afternoonOpeningField).bindReadOnly(SavePosDto::getAfternoonOpening);

        TimePicker afternoonClosingField = new TimePicker("Afternoon closing");
        afternoonClosingField.setReadOnly(true);
        afternoonClosingField.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(afternoonClosingField).bindReadOnly(SavePosDto::getAfternoonClosing);

        CheckboxGroup<String> daysClosedCheckboxGroup = new CheckboxGroup<>();
        daysClosedCheckboxGroup.setLabel("Closing days");
        daysClosedCheckboxGroup.setItems(IntStream.range(1, 8).mapToObj(String::valueOf).toList());
        binder.forField(daysClosedCheckboxGroup)
                .withConverter(
                        items -> String.join(",", items),
                        str -> Arrays.stream(str.split(",")).collect(Collectors.toSet())
                )
                .bindReadOnly(SavePosDto::getDaysClosed);

        getContent().add(
                nameField, telephoneField,
                cellphoneField, addressField,
                cityField, modelField,
                brandField, connectionTypeField,
                morningOpeningField, morningClosingField,
                afternoonOpeningField, afternoonClosingField,
                daysClosedCheckboxGroup
        );
    }

    @NonNull
    private String getCityName(@NonNull SavePosDto pos) {
        return cityService.findById(pos.getCityId()).orElseThrow().getName();
    }
}
