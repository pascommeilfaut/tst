package com.iongroup.data.user;

import com.iongroup.util.EnumUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserType {
    ADMIN(1),
    TECHNICAL_GROUP(2);

    public final int id;

    public String getDisplayName() {
        return EnumUtils.getEnumValueDisplayName(this.name());
    }

    public static UserType fromId(int id) {
        for (UserType userType : UserType.values()) {
            if (userType.id == id) {
                return userType;
            }
        }

        return null;
    }
}
