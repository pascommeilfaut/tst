package com.iongroup.util;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class TimeUtils {

    @NotNull
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
