package com.iongroup.views.forms.add;

import com.iongroup.data.user.UserType;
import com.iongroup.service.UserService;
import com.iongroup.service.dto.SaveUserDto;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class SaveUserForm extends SaveForm<SaveUserDto> {

    private final UserService userService;
    private final @Nullable Integer formDataObjectId;

    public SaveUserForm(UserService userService) {
        this(userService, null);
    }

    public SaveUserForm(UserService userService, @Nullable Integer userId) {
        this.userService = userService;
        this.formDataObjectId = userId;

        SaveUserDto formDataObject;
        if (userId == null) {
            formDataObject = new SaveUserDto();
        } else {
            formDataObject = userService.findSaveUserDtoById(userId).orElseThrow();
        }
        setFormDataObject(formDataObject);

        String oldLogin = formDataObject.getLogin();

        TextField loginField = new TextField("Login");
        PasswordField passwordField = new PasswordField("Password");
        TextField nameField = new TextField("Name");
        TextField telephoneField = new TextField("Telephone");
        EmailField emailField = new EmailField("Email");
        ComboBox<UserType> userTypeComboBox = new ComboBox<>("User Type");
        userTypeComboBox.setItems(UserType.values());
        userTypeComboBox.setItemLabelGenerator(UserType::getDisplayName);

        getContent().add(loginField, passwordField, nameField, telephoneField, emailField, userTypeComboBox);

        binder.forField(loginField)
                .asRequired("Please, enter your login")
                .withValidator(new StringLengthValidator("Login must be between 5 and 50 characters", 5, 50))
                .withValidator(login -> login.equals(oldLogin) || userService.findByLogin(login).isEmpty(), "This login is already in use")
                .bind(SaveUserDto::getLogin, SaveUserDto::setLogin);

        if (userId == null) {
            binder.forField(passwordField)
                    .asRequired("Please, enter your password")
                    .withValidator(new StringLengthValidator("Password must be between 8 and 255 characters", 8, 255))
                    .bind(SaveUserDto::getRawPassword, SaveUserDto::setRawPassword);
        } else {
            binder.forField(passwordField)
                    .withValidator(v -> v.isEmpty() || (v.length() >= 8 && v.length() <= 255), "Password must be between 8 and 255 characters")
                    .bind(SaveUserDto::getRawPassword, SaveUserDto::setRawPassword);
        }

        binder.forField(nameField)
                .asRequired("Please, enter your name")
                .withValidator(new StringLengthValidator("Name must be between 5 and 100 characters", 5, 100))
                .bind(SaveUserDto::getName, SaveUserDto::setName);

        binder.forField(telephoneField)
                .asRequired("Please, enter your telephone")
                .withValidator(new StringLengthValidator("Telephone number must be between 8 and 50 characters", 8, 50))
                .bind(SaveUserDto::getTelephone, SaveUserDto::setTelephone);

        binder.forField(emailField)
                .asRequired("Please, enter your email")
                .withValidator(new EmailValidator("Please, enter a valid email address"))
                .bind(SaveUserDto::getEmail, SaveUserDto::setEmail);

        binder.forField(userTypeComboBox)
                .asRequired("Please, choose your user type")
                .bind(SaveUserDto::getType, SaveUserDto::setType);
    }

    @Override
    public void persistDataObject(SaveUserDto formDataObject) {
        if (formDataObjectId == null) {
            userService.create(formDataObject);
        } else {
            userService.update(formDataObject, formDataObjectId);
        }
    }

}
