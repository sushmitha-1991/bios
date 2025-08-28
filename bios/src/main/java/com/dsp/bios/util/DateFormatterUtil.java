package com.dsp.bios.util;

import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class DateFormatterUtil {

	private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
	
	private static final String inputPattern = "yyyy-MM-dd";
	
	 public static String formatDate(String inputDate) {
    	 if (inputDate == null || inputDate.isBlank()) {
             return null;
         }
    	 DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputPattern, Locale.ENGLISH);
    	 LocalDate date = LocalDate.parse(inputDate, inputFormatter);
         return date.format(FORMATTER);
    }
	 
	 public static LocalDate addDatePlusOne(LocalDate date)
		{
			return date.plusDays(1); //Add 1 Day to toDate filter
		}
}
