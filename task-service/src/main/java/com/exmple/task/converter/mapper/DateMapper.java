package com.exmple.task.converter.mapper;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {
    // TODO (vm): try to use LocalDate
    public Date longAsDate(final long timestamp) {
        return new Date(timestamp * 1000);
    }

    public long dateAsLong(final Date date) {
        return date.getTime();
    }
}
