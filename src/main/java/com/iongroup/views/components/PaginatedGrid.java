package com.iongroup.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiFunction;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class PaginatedGrid<T> extends VerticalLayout {

    @Getter private final Grid<T> grid;
    private final Select<Integer> pageSizeSelect = new Select<>();
    @Getter private final TextField searchField = new TextField();
    private final Button prevButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button currentPageBtn = new Button("1");
    private final Span statusText = new Span();

    private int currentPage = 0;
    private int totalPages = 0;
    private SerializableBiFunction<String, Pageable, Page<T>> fetchDataCallback;

    public PaginatedGrid(Class<T> beanType) {
        this.grid = new Grid<>(beanType, false);
        setSizeFull();
        setPadding(false);
        setSpacing(true);
        configureToolbar();
        configureGrid();
        configureFooter();
    }

    public void setFetchCallback(SerializableBiFunction<String, Pageable, Page<T>> fetchDataCallback) {
        this.fetchDataCallback = fetchDataCallback;
        refresh();
    }

    private void configureToolbar() {
        pageSizeSelect.setLabel("Show entries");
        pageSizeSelect.setItems(10, 25, 50, 100);
        pageSizeSelect.setValue(10);

        pageSizeSelect.addValueChangeListener(e -> {
            currentPage = 0;
            refresh();
        });

        searchField.setPlaceholder("Search:");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

        searchField.addValueChangeListener(e -> {
            currentPage = 0;
            refresh();
        });

        HorizontalLayout toolbar = new HorizontalLayout(pageSizeSelect, searchField);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setVerticalComponentAlignment(Alignment.END, pageSizeSelect, searchField);
        add(toolbar);
    }

    private void configureGrid() {
        grid.setHeightFull();
        grid.addSortListener(e -> refresh());
        add(grid);
    }

    private void configureFooter() {
        prevButton.addClickListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                refresh();
            }
        });

        nextButton.addClickListener(e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                refresh();
            }
        });

        currentPageBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        currentPageBtn.setEnabled(false);
        currentPageBtn.getStyle().set("opacity", "1");
        HorizontalLayout paginationControls = new HorizontalLayout(prevButton, currentPageBtn, nextButton);
        HorizontalLayout footer = new HorizontalLayout(statusText, paginationControls);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        footer.setAlignItems(Alignment.CENTER);
        add(footer);
    }

    public void refresh() {
        if (fetchDataCallback == null) return;
        String searchTerm = searchField.getValue();
        Pageable pageable = getPageable();
        Page<T> pageData = fetchDataCallback.apply(searchTerm, pageable);
        grid.setItems(pageData.getContent());
        totalPages = pageData.getTotalPages();
        long totalItems = pageData.getTotalElements();
        updatePaginationButtons();
        updateStatusText(pageData.getNumberOfElements(), totalItems);
    }

    public Pageable getPageable() {
        return PageRequest.of(currentPage, pageSizeSelect.getValue(), getSpringSort());
    }

    private Sort getSpringSort() {
        List<GridSortOrder<T>> sortOrders = grid.getSortOrder();

        if (sortOrders.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = sortOrders.stream()
                .map(sortOrder -> {
                    String property = sortOrder.getSorted().getKey();
                    Sort.Direction direction = sortOrder.getDirection() == SortDirection.ASCENDING
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC;
                    return new Sort.Order(direction, property);
                })
                .collect(Collectors.toList());

        return Sort.by(orders);
    }

    private void updatePaginationButtons() {
        prevButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < totalPages - 1);
        currentPageBtn.setText(String.valueOf(currentPage + 1));
    }

    private void updateStatusText(int currentItemsCount, long totalItems) {
        int start = (currentPage * pageSizeSelect.getValue()) + 1;
        int end = start + currentItemsCount - 1;

        if (totalItems == 0) {
            statusText.setText("Showing 0 entries");
        } else {
            statusText.setText(String.format("Showing %d to %d of %d entries", start, end, totalItems));
        }
    }

}
