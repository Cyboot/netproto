package net.tmt.common.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class StringFormatter {
	private static DecimalFormat	df_standart	= new DecimalFormat("#0.00",
														DecimalFormatSymbols.getInstance(Locale.US));
	private static DecimalFormat	df_extra	= new DecimalFormat("#0.00",
														DecimalFormatSymbols.getInstance(Locale.US));

	public static String format(final double value) {
		return df_standart.format(value);
	}

	public static String format(final double value, final int integerDigit, final int fractionDigit) {
		df_extra.setMaximumFractionDigits(fractionDigit);
		df_extra.setMinimumFractionDigits(fractionDigit);

		df_extra.setMinimumIntegerDigits(integerDigit);
		return df_extra.format(value);
	}
}