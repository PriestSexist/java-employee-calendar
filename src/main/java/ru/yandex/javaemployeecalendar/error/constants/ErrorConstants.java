package ru.yandex.javaemployeecalendar.error.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class ErrorConstants {
    public static final String TOO_SHORT = "String too short";
    public static final String TOO_LONG = "String too long";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    public static final String ROLE_NOT_FOUND_BY_ID = "Role with id=%d was not found";
    public static final String USER_WITH_THIS_EMAIL_ALREADY_EXISTS = "User with email=%s already exists";
    public static final String USER_NOT_FOUND_BY_ID = "User with id=%d was not found";
    public static final String EVENT_NOT_FOUND_BY_ID = "Event with id=%d was not found";
    public static final String NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_USER_ID = "Not working employee with user id=%d was not found";
    public static final String NOT_WORKING_EMPLOYEE_NOT_FOUND_BY_ID = "Not working employee with id=%d was not found";
    public static final String EVENT_START_DATE_RESTRICTIONS = "Event start date should be from current time to 7 days";
    public static final String EVENT_END_DATE_RESTRICTIONS = "Event end date should be from 1 day to 14 days";
    public static final String EVENT_START_AND_END_DATE_RESTRICTIONS = "Event start date should be before event end date and time between start time and ent time should be at least 1 day";


}
