package com.iongroup.data.issue.status;

import com.iongroup.util.EnumUtils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum IssueStatus {
    NEW(1),
    ASSIGNED(2),
    IN_PROGRESS(3),
    PENDING(4);

    public final int id;

    public String getDisplayName() {
        return EnumUtils.getEnumValueDisplayName(this.name());
    }

    public static IssueStatus valueOfSafe(String value) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

}
