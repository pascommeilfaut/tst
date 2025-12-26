package com.iongroup.views.forms.add;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class SaveForm<T> extends Composite<FormLayout> implements IForm<T> {

    protected final Binder<T> binder = new Binder<>();

    @Override
    public boolean isFormDataObjectValid() {
        return getFormDataObject().isPresent();
    }

    @Override
    public void setFormDataObject(@Nullable T formDataObject) {
        binder.setBean(formDataObject);
    }

    @Override
    public Optional<T> getFormDataObject() {
        if (binder.getBean() == null) {
            throw new IllegalStateException("No form data object");
        }

        if (binder.validate().isOk()) {
            return Optional.of(binder.getBean());
        } else {
            return Optional.empty();
        }
    }

    public abstract void persistDataObject(T formDataObject);

    @Override
    public void saveDataObject() {
        getFormDataObject().ifPresent(formDataObject -> {
            persistDataObject(formDataObject);
            setFormDataObject(formDataObject);
        });
    }

}
