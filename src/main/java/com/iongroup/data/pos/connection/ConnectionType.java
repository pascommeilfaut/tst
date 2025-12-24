package com.iongroup.data.pos.connection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConnectionType {
    REMOTE(1, "Remote"),
    WI_FI(2, "Wi-Fi");

    private final int id;
    private final String displayName;
}
