import java.util.ArrayList;
import java.util.List;

public class bonus {
    public static void main(String[] args) {

        printBonusDatesBetween(1, 2500);
    }

    public static void printBonusDatesBetween(int fromYear, int toYear) {
        List<String> dates = Date.getBonusDatesBetween(fromYear, toYear);
        System.out.println(dates.size() + " dates between " + fromYear + " and " + toYear);
        for (int i = 0; i < dates.size(); i++) {
            System.out.println(dates.get(i));
        }
    }

    public static class Date {
        public static Boolean checkMonthAndDay(String date) {
            int year = Integer.parseInt(date.substring(0, date.length() - 4));
            int month = Integer.parseInt(date.substring(date.length() - 3, date.length() - 2));
            int day = Integer.parseInt(date.substring(date.length() - 2));
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return day <= 31;
                case 4:
                case 6:
                case 9:
                case 11:
                    return day <= 30;
                case 2:
                    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
                        return day <= 29;
                    return day <= 28;
                default:
                    return false;
            }
        }

        public static List<String> getBonusDatesBetween(int fromYear, int toYear) {
            List<String> dates = new ArrayList<String>();

            dates.addAll(getBonusDatesSingleDigit(Math.max(1, fromYear), Math.min(9, toYear)));
            dates.addAll(getBonusDatesDoubleDigit(Math.max(10, fromYear), Math.min(99, toYear)));
            dates.addAll(getBonusDatesThreeDigit(Math.max(100, fromYear), Math.min(999, toYear)));
            dates.addAll(getBonusDatesFourDigit(Math.max(1000, fromYear), Math.min(9999, toYear)));

            String startYear = Integer.toString(fromYear);
            String endYear = Integer.toString(toYear);

            for (int i = Math.max(5, startYear.length()); i <= endYear.length(); i++) {
                dates.addAll(getBonusDatesMoreThanFourDigit(Math.max((int) Math.pow(10, i - 1), fromYear),
                        Math.min((int) Math.pow(10, i) - 1, toYear), i - 4));
            }

            return dates;
        }

        private static List<String> getBonusDatesSingleDigit(int fromYear, int toYear) {
            List<String> datesBetween = new ArrayList<String>();

            if (fromYear > toYear)
                return datesBetween;

            for (int i = fromYear; i <= toYear; i++) {
                for (int j = 1; j <= 12; j++) {
                    int date = i * 10000 + j * 100 + (j / 10 * 10) + i;
                    if (checkMonthAndDay(Integer.toString(date)))
                        datesBetween.add(format(Integer.toString(date)));
                }
            }

            return datesBetween;
        }

        private static List<String> getBonusDatesDoubleDigit(int fromYear, int toYear) {
            List<String> datesBetween = new ArrayList<String>();

            if (fromYear > toYear)
                return datesBetween;

            for (int i = fromYear; i <= toYear; i++) {
                int date = i * 10000 + 1100 + (i % 10 * 10) + i / 10;
                if (checkMonthAndDay(Integer.toString(date)))
                    datesBetween.add(format(Integer.toString(date)));
            }

            return datesBetween;
        }

        private static List<String> getBonusDatesThreeDigit(int fromYear, int toYear) {
            List<String> datesBetween = new ArrayList<String>();

            if (fromYear > toYear)
                return datesBetween;

            for (int i = fromYear; i <= toYear; i++) {
                for (int j = 0; j <= 1; j++) {
                    int date = i * 10000 + j * 1000 + (i % 10 * 100) + (i % 100 / 10 * 10) + i / 100;
                    if (checkMonthAndDay(Integer.toString(date)))
                        datesBetween.add(format(Integer.toString(date)));

                }
            }

            return datesBetween;
        }

        private static List<String> getBonusDatesFourDigit(int fromYear, int toYear) {
            List<String> datesBetween = new ArrayList<String>();
            // date format xyab--ba-yx

            if (fromYear > toYear)
                return datesBetween;

            int[] ab = { 10, 11, 20, 21, 30, 40, 50, 60, 70, 80, 90 };
            for (int x = 1; x <= 9; x++) {
                for (int y = 0; y <= 3; y++) {
                    if (y == 3 && x == 2)
                        break;

                    for (int i = 0; i < ab.length; i++) {
                        int a = ab[i] / 10;
                        int b = ab[i] % 10;
                        int date = x * 10000000 + y * 1000000 + a * 100000 + b * 10000 + b * 1000 + a * 100 + y * 10
                                + x;
                        if (date / 10000 < fromYear || date / 10000 > toYear)
                            continue;
                        if (checkMonthAndDay(Integer.toString(date)))
                            datesBetween.add(format(Integer.toString(date)));
                    }
                }
            }

            return datesBetween;
        }

        private static List<String> getBonusDatesMoreThanFourDigit(int fromYear, int toYear,
                int numberOfUnknownDigits) {
            // date format xyab*--ba-yx ('*' means unknown number of digits)
            List<String> datesBetween = new ArrayList<String>();
            
            if (fromYear > toYear)
                return datesBetween;

            int halfDigits = (numberOfUnknownDigits + 1) / 2;
            int[] ab = { 10, 11, 20, 21, 30, 40, 50, 60, 70, 80, 90 };
            for (int x = 1; x <= 9; x++) {
                for (int y = 0; y <= 3; y++) {
                    if (y == 3 && x == 2)
                        break;
                    for (int i = 0; i < ab.length; i++) {
                        int a = ab[i] / 10;
                        int b = ab[i] % 10;
                        String dateStart = "" + x + y + a + b;
                        String dateEnd = "" + b + a + y + x;

                        int combinations = (int) Math.pow(10, halfDigits);
                        for (int j = 0; j < combinations; j++) {
                            String middle = Palindrome.reverseAndAddEndOfPalindrome(
                                    Integer.toString(j),
                                    numberOfUnknownDigits % 2 == 0 ? true : false);
                            String date = dateStart + middle + dateEnd;
                            if (Integer.parseInt(date.substring(0, 4 + numberOfUnknownDigits)) < fromYear ||
                                Integer.parseInt(date.substring(0, 4 + numberOfUnknownDigits)) > toYear)
                                continue;
                            if (checkMonthAndDay(date))
                                datesBetween.add(format(date));
                        }
                    }
                }
            }

            return datesBetween;
        }

        public static String format(String year) {
            return year.substring(0, year.length() - 4) + "-"
                    + year.substring(year.length() - 4, year.length() - 2) + "-"
                    + year.substring(year.length() - 2, year.length());
        }
    }

    public static class Palindrome {
        public static String reverseAndAddEndOfPalindrome(String firstHalf, Boolean evenNumberOfDigits) {
            return firstHalf + new StringBuilder(firstHalf).reverse().substring(evenNumberOfDigits ? 0 : 1);
        }
    }
}