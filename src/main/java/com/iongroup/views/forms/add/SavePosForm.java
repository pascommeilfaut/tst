package com.iongroup.views.forms.add;

import com.iongroup.data.city.CityEntity;
import com.iongroup.data.pos.connection.ConnectionType;
import com.iongroup.service.CityService;
import com.iongroup.service.PosService;
import com.iongroup.service.dto.SavePosDto;
import com.iongroup.util.EntityUtils;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SavePosForm extends SaveForm<SavePosDto> {

    private final PosService posService;
    private final @Nullable Integer formDataObjectId;

    public SavePosForm(PosService posService, CityService cityService) {
        this(posService, cityService, null);
    }

    public SavePosForm(PosService posService, CityService cityService, @Nullable Integer posId) {
        this.posService = posService;

        if (posId == null) {
            setFormDataObject(new SavePosDto());
            this.formDataObjectId = null;
        } else {
            setFormDataObject(posService.findSavePosDtoById(posId).orElseThrow());
            this.formDataObjectId = posId;
        }

        TextField nameField = new TextField("Name");
        nameField.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());
        binder.forField(nameField)
                .asRequired("Please, enter POS name")
                .withValidator(new StringLengthValidator("Name must be between 1 and 100 characters", 1, 100))
                .bind(SavePosDto::getName, SavePosDto::setName);

        TextField telephoneField = new TextField("Telephone");
        telephoneField.setPrefixComponent(VaadinIcon.PHONE_LANDLINE.create());
        binder.forField(telephoneField)
                .withValidator(new StringLengthValidator("Telephone number must be not longer than 50 characters", null, 50))
                .bind(SavePosDto::getTelephone, SavePosDto::setTelephone);

        TextField cellphoneField = new TextField("Cellphone");
        cellphoneField.setPrefixComponent(VaadinIcon.PHONE.create());
        binder.forField(cellphoneField)
                .withValidator(new StringLengthValidator("Cellphone number be not longer than 50 characters", null, 50))
                .bind(SavePosDto::getCellphone, SavePosDto::setCellphone);

        TextField addressField = new TextField("Address");
        addressField.setPrefixComponent(VaadinIcon.MAP_MARKER.create());
        binder.forField(addressField)
                .asRequired("Please, enter POS address")
                .withValidator(new StringLengthValidator("Address must be between 1 and 255 characters", 1, 255))
                .bind(SavePosDto::getAddress, SavePosDto::setAddress);

        List<CityEntity> allCities = cityService.findAll(Pageable.unpaged()).toList();
        Map<Integer, CityEntity> cityIdMap = EntityUtils.toIdMap(allCities);
        List<Integer> orderedCityIds = EntityUtils.toOrderedIdList(allCities, CityEntity::getName);
        ComboBox<Integer> cityComboBox = new ComboBox<>("City");
        cityComboBox.setItems(orderedCityIds);
        cityComboBox.setItemLabelGenerator(i -> cityIdMap.get(i).getName());
        binder.forField(cityComboBox)
                .asRequired("Please, choose POS city")
                .bind(SavePosDto::getCityId, SavePosDto::setCityId);

        TextField modelField = new TextField("Model");
        modelField.setPrefixComponent(VaadinIcon.BARCODE.create());
        binder.forField(modelField)
                .asRequired("Please, enter POS model")
                .withValidator(new StringLengthValidator("Model must be between 1 and 100 characters", 1, 100))
                .bind(SavePosDto::getModel, SavePosDto::setModel);

        TextField brandField = new TextField("Brand");
        brandField.setPrefixComponent(VaadinIcon.BARCODE.create());
        binder.forField(brandField)
                .asRequired("Please, enter POS brand")
                .withValidator(new StringLengthValidator("Brand must be between 1 and 100 characters", 1, 100))
                .bind(SavePosDto::getBrand, SavePosDto::setBrand);

        ComboBox<ConnectionType> connectionTypeComboBox = new ComboBox<>("Connection type");
        connectionTypeComboBox.setItems(ConnectionType.values());
        connectionTypeComboBox.setItemLabelGenerator(ConnectionType::getDisplayName);
        binder.forField(connectionTypeComboBox)
                .asRequired("Please, choose connection type")
                .bind(SavePosDto::getConnectionType, SavePosDto::setConnectionType);

        TimePicker morningOpeningTimePicker = new TimePicker("Morning opening");
        morningOpeningTimePicker.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(morningOpeningTimePicker)
                .bind(SavePosDto::getMorningOpening, SavePosDto::setMorningOpening);

        TimePicker morningClosingTimePicker = new TimePicker("Morning closing");
        morningClosingTimePicker.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(morningClosingTimePicker)
                .bind(SavePosDto::getMorningClosing, SavePosDto::setMorningClosing);

        TimePicker afternoonOpeningTimePicker = new TimePicker("Afternoon opening");
        afternoonOpeningTimePicker.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(afternoonOpeningTimePicker)
                .bind(SavePosDto::getAfternoonOpening, SavePosDto::setAfternoonOpening);

        TimePicker afternoonClosingTimePicker = new TimePicker("Afternoon closing");
        afternoonClosingTimePicker.setPrefixComponent(VaadinIcon.CLOCK.create());
        binder.forField(afternoonClosingTimePicker)
                .bind(SavePosDto::getAfternoonClosing, SavePosDto::setAfternoonClosing);

        CheckboxGroup<String> daysClosedCheckboxGroup = new CheckboxGroup<>();
        daysClosedCheckboxGroup.setLabel("Closing days");
        daysClosedCheckboxGroup.setItems(IntStream.range(1, 8).mapToObj(String::valueOf).toList());
        binder.forField(daysClosedCheckboxGroup)
                .withConverter(
                        items -> String.join(",", items),
                        str -> Arrays.stream(str.split(",")).collect(Collectors.toSet())
                )
                .bind(SavePosDto::getDaysClosed, SavePosDto::setDaysClosed);

        getContent().add(
                nameField, telephoneField,
                cellphoneField, addressField,
                cityComboBox, modelField,
                brandField, connectionTypeComboBox,
                morningOpeningTimePicker, morningClosingTimePicker,
                afternoonOpeningTimePicker, afternoonClosingTimePicker,
                daysClosedCheckboxGroup
        );
    }

    @Override
    public void persistDataObject(SavePosDto formDataObject) {
        if (formDataObjectId == null) {
            posService.create(formDataObject);
        } else {
            posService.update(formDataObject, formDataObjectId);
        }
    }
}
