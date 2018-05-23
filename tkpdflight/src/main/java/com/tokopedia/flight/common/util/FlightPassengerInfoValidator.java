package com.tokopedia.flight.common.util;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * @author by furqan on 20/03/18.
 */

public class FlightPassengerInfoValidator {

    private static final int MAX_PASSENGER_NAME_LENGTH = 48;
    private static final int MIN_PASSENGER_LAST_NAME = 2;
    private static final String REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z\\s]*$";

    @Inject
    public FlightPassengerInfoValidator() {
    }

    public boolean validateDateNotBetween(Date minDate, Date maxDate, Date selectedDate) {
        return selectedDate.before(minDate) || selectedDate.after(maxDate);
    }

    public boolean validateDateExceedMaxDate(Date maxDate, Date selectedDate) {
        return selectedDate.after(maxDate);
    }

    public boolean validateBirthdateNotEmpty(String birthdate) {
        return birthdate != null && !birthdate.isEmpty() && birthdate.length() > 0;
    }

    public boolean validateNameIsEmpty(String name) {
        return name.isEmpty() || name.length() == 0;
    }

    public boolean validateNameIsNotAlphabetAndSpaceOnly(String name) {
        return name.length() > 0 && !isAlphabetAndSpaceOnly(name);
    }

    public boolean validateNameIsMoreThanMaxLength(String firstName, String lastName) {
        return firstName.length() + lastName.length() > MAX_PASSENGER_NAME_LENGTH;
    }

    public boolean validateLastNameIsLessThanMinLength(String lastName) {
        return lastName.length() < MIN_PASSENGER_LAST_NAME;
    }

    public boolean validateLastNameIsNotSingleWord(String lastName) {
        return lastName.length() > 0 && !isSingleWord(lastName);
    }

    public boolean validateTitleIsEmpty(String title) {
        return title.isEmpty() && title.length() == 0;
    }

    public boolean validateDateMoreThan(String dateDefaultViewFormat, Date secondDate) {
        return FlightDateUtil.removeTime(FlightDateUtil.stringToDate(
                FlightDateUtil.DEFAULT_VIEW_FORMAT, dateDefaultViewFormat))
                .compareTo(FlightDateUtil.removeTime(secondDate)) > 0;
    }

    public boolean validateDateLessThan(String dateDefaultViewFormat, Date secondDate) {
        return FlightDateUtil.removeTime(FlightDateUtil.stringToDate(
                FlightDateUtil.DEFAULT_VIEW_FORMAT, dateDefaultViewFormat))
                .compareTo(FlightDateUtil.removeTime(secondDate)) < 0;
    }

    private boolean isAlphabetAndSpaceOnly(String expression) {
        return expression.matches(new String(REGEX_IS_ALPHABET_AND_SPACE_ONLY));
    }

    private boolean isSingleWord(String passengerLastName) {
        return passengerLastName != null && passengerLastName.split(" ").length == 1;
    }

    public boolean validateDateNotLessThan(Date indicator, String selectedDate) {
        Date inputDate = FlightDateUtil.removeTime(FlightDateUtil.stringToDate(
                FlightDateUtil.DEFAULT_VIEW_FORMAT, selectedDate));
        return inputDate.before(indicator);
    }

    public boolean validateExpiredDateOfPassportAtLeast6Month(String expiredDateString, Date lastFlightDate) {
        Date expiredDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, expiredDateString);
        Date lastFlightDateUsed = FlightDateUtil.addTimeToSpesificDate(lastFlightDate, Calendar.DATE, -2);

        return expiredDate.after(lastFlightDateUsed);
    }

    public boolean validateExpiredDateOfPassportMax20Years(String expiredDateString, Date lastFlightDate) {
        Date expiredDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, expiredDateString);

        return expiredDate.before(lastFlightDate);
    }

    public boolean validatePassportNumberNotEmpty(String passportNumber) {
        return passportNumber.length() > 0 && !passportNumber.isEmpty();
    }
}
