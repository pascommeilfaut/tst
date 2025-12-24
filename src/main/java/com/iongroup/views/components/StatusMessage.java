package com.iongroup.views.components;

import com.iongroup.views.forms.add.IForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
public class StatusMessage extends Span {

    @Getter(AccessLevel.PRIVATE) private final IForm<?> form;
    @Getter(AccessLevel.PRIVATE) private final Button saveButton;
    private final Consumer<StatusMessage> onSave;

    private String text;

    public StatusMessage(IForm<?> form, Button saveButton, Consumer<StatusMessage> onSave) {
        this.form = form;
        this.saveButton = saveButton;
        this.onSave = onSave;
        initButton();
    }

    private void initButton() {
        saveButton.addClickListener(event -> {
            if (!form.isFormDataObjectValid()) {
                super.setVisible(false);
                return;
            }

            if (onSave != null) {
                onSave.accept(this);
                form.saveDataObject();
            }

            super.setText(text);
            super.setVisible(true);
        });
    }

}
