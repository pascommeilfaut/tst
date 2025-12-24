package com.iongroup.views.forms.add;

import com.iongroup.data.issue.IssuePriority;
import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.data.user.UserType;
import com.iongroup.service.IssueService;
import com.iongroup.service.IssueTypeService;
import com.iongroup.service.UserService;
import com.iongroup.service.dto.CreateIssueDto;
import com.iongroup.service.dto.IssueTypeDto;
import com.iongroup.service.mapper.IssueTypeMapper;
import com.iongroup.util.TransactionHandler;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SaveIssueForm extends SaveForm<CreateIssueDto> {

    private final IssueService issueService;
    private final UserService userService;

    private final @Nullable Integer formDataObjectId;

    @Setter private Integer posId;

    public SaveIssueForm(
            IssueService issueService,
            IssueTypeMapper issueTypeMapper,
            IssueTypeService issueTypeService,
            TransactionHandler transactionHandler,
            UserService userService,
            @Nullable Integer issueId
    ) {
        this.issueService = issueService;
        this.userService = userService;

        if (issueId == null) {
            setFormDataObject(new CreateIssueDto());
            this.formDataObjectId = null;
        } else {
            setFormDataObject(issueService.findSaveIssueDtoById(issueId).orElseThrow());
            this.formDataObjectId = issueId;
        }

        List<IssueTypeDto> availableIssueTypes = new ArrayList<>();
        List<IssueTypeDto> availableIssueSubTypes = new ArrayList<>();

        ComboBox<IssueTypeDto> issueTypeComboBox = new ComboBox<>("Issue Type");
        ComboBox<IssueTypeDto> issueSubTypeComboBox = new ComboBox<>("Subclass");

        transactionHandler.runInTransaction(() -> {
            availableIssueTypes.addAll(issueTypeService.findAllParents(Pageable.unpaged()).stream()
                    .map(issueTypeMapper::mapToDto)
                    .toList());

            availableIssueSubTypes.addAll(issueTypeService.findAllSubTypes(Pageable.unpaged()).stream()
                    .map(issueTypeMapper::mapToDto)
                    .toList());
        });

        issueTypeComboBox.setItems(availableIssueTypes);
        issueSubTypeComboBox.setItems(availableIssueSubTypes);

        issueTypeComboBox.setItemLabelGenerator(IssueTypeDto::getName);
        issueSubTypeComboBox.setItemLabelGenerator(IssueTypeDto::getName);

        TextField problem = new TextField("Problem");

        ComboBox<IssuePriority> priorityComboBox = new ComboBox<>("Priority");
        priorityComboBox.setItems(IssuePriority.values());
        priorityComboBox.setItemLabelGenerator(IssuePriority::getDisplayName);

        ComboBox<IssueStatus> statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems(IssueStatus.values());
        statusComboBox.setItemLabelGenerator(IssueStatus::getDisplayName);

        TextArea description = new TextArea("Problem description");
        TextArea solution = new TextArea("Solution");

        ComboBox<UserType> assignedToComboBox = new ComboBox<>("Assigned To");
        assignedToComboBox.setItems(UserType.values());
        assignedToComboBox.setItemLabelGenerator(UserType::getDisplayName);

        TextField memo = new TextField("Memo");

        getContent().add(
                issueTypeComboBox,
                issueSubTypeComboBox,
                problem,
                priorityComboBox,
                statusComboBox,
                description,
                solution,
                assignedToComboBox,
                memo
        );

        binder.forField(issueTypeComboBox)
                .asRequired("Please, choose the issue type")
                .withConverter(
                        issueTypeDto -> issueTypeDto != null ? issueTypeDto.getId() : null,
                        id -> {
                            if (id == null) return null;
                            return availableIssueTypes.stream()
                                    .filter(issueTypeDto -> issueTypeDto.getId().equals(id))
                                    .findFirst()
                                    .orElse(null);
                        }
                )
                .bind(CreateIssueDto::getIssueTypeId, CreateIssueDto::setIssueTypeId);

        binder.forField(issueSubTypeComboBox)
                .asRequired("Please, choose the issue subtype")
                .withConverter(
                        issueTypeDto -> issueTypeDto != null ? issueTypeDto.getId() : null,
                        id -> {
                            if (id == null) return null;
                            return availableIssueSubTypes.stream()
                                    .filter(issueTypeDto -> issueTypeDto.getId().equals(id))
                                    .findFirst()
                                    .orElse(null);
                        }
                )
                .bind(CreateIssueDto::getSubTypeId, CreateIssueDto::setSubTypeId);

        binder.forField(priorityComboBox)
                .asRequired("Please, choose the issue priority")
                .bind(CreateIssueDto::getPriority, CreateIssueDto::setPriority);

        binder.forField(statusComboBox)
                .asRequired("Please, choose the issue priority")
                .bind(CreateIssueDto::getStatus, CreateIssueDto::setStatus);

        binder.forField(description)
                .asRequired("Please, enter the problem description")
                .withValidator(new StringLengthValidator("Problem description must be between 5 and 10240 characters", 5, 10240))
                .bind(CreateIssueDto::getProblemDescription, CreateIssueDto::setProblemDescription);

        binder.forField(problem)
                .asRequired("Please, enter the problem")
                .withValidator(new StringLengthValidator("Problem must be between 5 and 255 characters", 5, 255))
                .bind(CreateIssueDto::getProblem, CreateIssueDto::setProblem);

        binder.forField(solution)
                .withValidator(new StringLengthValidator("Solution must be between 5 and 10240 characters", 5, 10240))
                .bind(CreateIssueDto::getProblemDescription, CreateIssueDto::setProblemDescription);

        binder.forField(assignedToComboBox)
                .asRequired("Please, choose the assigned group")
                .withConverter(
                        userType -> userType != null ? userType.id : null,
                        id -> id == null ? null : UserType.fromId(id)
                )
                .bind(CreateIssueDto::getAssignedTo, CreateIssueDto::setAssignedTo);

        binder.forField(memo)
                .withValidator(new StringLengthValidator("Memo must be between 5 and 255 characters", 5, 255))
                .bind(CreateIssueDto::getMemo, CreateIssueDto::setMemo);
    }

    @Override
    public void persistDataObject(CreateIssueDto formDataObject) {
        if (formDataObjectId == null) {
            formDataObject.setPosId(posId);
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            formDataObject.setCreatedBy(userService.findByLogin(login).orElseThrow().getId());
            issueService.create(formDataObject);
        } else {
            issueService.update(formDataObject, formDataObjectId);
        }
    }

}
