package com.iongroup.util;

import com.iongroup.data.BaseEntity;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class EntityUtils {

    @NonNull
    public <E extends BaseEntity> Map<Integer, E> toIdMap(
            @NonNull Collection<E> coll
    ) {
        return coll.stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }

    @NonNull
    public <E extends BaseEntity, U extends Comparable<? super U>> List<Integer> toOrderedIdList(
            @NonNull Collection<E> coll,
            @NonNull Function<E, U> getter
    ) {
        return toOrderedIdList(coll, Comparator.comparing(getter));
    }

    @NonNull
    public <E extends BaseEntity> List<Integer> toOrderedIdList(
            @NonNull Collection<E> coll,
            @NonNull Comparator<E> comp
    ) {
        return coll.stream().sorted(comp).map(BaseEntity::getId).toList();
    }
}
