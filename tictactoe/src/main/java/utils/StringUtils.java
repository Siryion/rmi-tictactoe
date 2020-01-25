package utils;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Contains string utility methods
 * 
 * @author rkm
 *
 */
public class StringUtils {

	/**
	 * Default constructor
	 */
	public StringUtils() {

	}

	/**
	 * Checks whether a string is numeric or not
	 * 
	 * @param strNum string to check
	 * @return true if the given string strNum is numeric, false otherwise
	 */
	public static boolean isNumeric(String strNum) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}

	/**
	 * Generates a random string 7 letters long
	 * 
	 * @return randomly generated string
	 */
	public static String generateRandomString(int length) {
		final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
		final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
		final String NUMBER = "0123456789";
		final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
		SecureRandom random = new SecureRandom();

		if (length < 1)
			throw new IllegalArgumentException();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {

			// 0-62 (exclusive), random returns 0-61
			int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
			char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

			// debug
			// System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);

			sb.append(rndChar);

		}

		return sb.toString();

	}
}
