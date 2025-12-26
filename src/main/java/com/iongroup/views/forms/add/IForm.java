package com.iongroup.views.forms.add;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IForm<T> {

    boolean isFormDataObjectValid();
    void setFormDataObject(@Nullable T formDataObject);
    Optional<T> getFormDataObject();
    void saveDataObject();

}
