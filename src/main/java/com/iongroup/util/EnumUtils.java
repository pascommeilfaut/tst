package com.iongroup.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnumUtils {

    public String getEnumValueDisplayName(String rawEnumName) {
        return (rawEnumName.charAt(0) + rawEnumName.substring(1).toLowerCase()).replace("_", " ");
    }

}
