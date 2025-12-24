package com.iongroup.data.issue;

import com.iongroup.util.EnumUtils;

public enum IssuePriority {
    MINOR,
    NORMAL,
    CRITICAL;

    public String getDisplayName() {
        return EnumUtils.getEnumValueDisplayName(this.name());
    }
}
