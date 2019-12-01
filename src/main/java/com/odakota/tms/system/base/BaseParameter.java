package com.odakota.tms.system.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odakota.tms.system.annotations.ConditionString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

/**
 * Resource list acquisition request
 *
 * @author haidv
 * @version 1.0
 */
public class BaseParameter {

    private final Integer limit;

    private final Integer page;

    @Pattern(regexp = "^[a-z0-9]+:(asc|desc)$", flags = CASE_INSENSITIVE)
    private final String sort;

    /**
     * Search condition string (Base64(JSON))
     */
    @ConditionString
    private final String condition;

    public BaseParameter(Integer limit, Integer page, String sort, String condition) {
        this.limit = (limit != null && limit <= 0) ? null : limit;
        this.page = (page != null && page < 0) ? null : page;
        this.sort = (sort != null) ? sort : "id:asc";
        this.condition = condition;
    }

    public Pageable getPageable() {
        String[] part = sort.split(":", 2);
        return (page == null || limit == null) ? null : PageRequest.of(page - 1, limit,
                                                                       Sort.by(Sort.Direction.fromString(part[1]),
                                                                               part[0]));
    }

    public FindCondition getCondition() {
        return new FindCondition(condition);
    }

    /**
     * This class holds search conditions.
     *
     * @author haidv
     * @version 1.0
     */
    @Slf4j
    public static class FindCondition {
        private static final Charset CHARSET = StandardCharsets.UTF_8;

        @Nullable
        private final String value;

        /**
         * Specify a search condition character string and construct a new instance.
         *
         * @param value Search condition string Base64 (JSON (object))
         */
        FindCondition(@Nullable String value) {
            this.value = value;
        }

        /**
         * Gets the search condition by binding to the specified object.
         *
         * @param valueType Type to bind search condition (any class)
         * @param <T>       Type to bind search condition
         * @return Search condition object
         */
        public <T> T get(Class<T> valueType) {
            if (value == null) {
                return null;
            }

            String json;
            try {
                json = new String(Base64.getDecoder().decode(value), CHARSET);
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                return null;
            }

            T object;
            try {
                object = new ObjectMapper().readValue(json, valueType);
            } catch (IOException e) {
                log.error(e.getMessage());
                return null;
            }

            return object;
        }
    }
}
