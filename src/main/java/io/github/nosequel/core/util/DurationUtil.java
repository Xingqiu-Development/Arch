package io.github.nosequel.core.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class DurationUtil {

    public static String unixToDate(long unixTime) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(unixTime));
    }


    private static int daysInMonth(int cM, int wY) {
        int ret = 0;

        switch (cM) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12: {
                ret = 31;
            }
            break;
            case 2: {
                int tY = wY % 4;
                if (tY != 0)
                    ret = 28;
                else
                    ret = 29;
            }
            break;
            case 4:
            case 6:
            case 9:
            case 11: {
                ret = 30;
            }
            break;
        }

        return ret;
    }

    public static String millisToRoundedTime(long millis) {

        StringBuilder sb = new StringBuilder(64);


        long years = 0, months = 0, weeks = 0, days = 0, hours = 0, minutes = 0;

        int curMonth = Calendar.getInstance().get(Calendar.MONTH);
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        millis /= 1000;


        if (millis >= 31556926) {
            years = millis / 31556926;
            millis -= (years * 31556926);
            sb.append(years);
            sb.append(" years, ");
        }

        while (true) {
            if (curMonth == 12) {
                curMonth = 0;
                curYear++;
            }

            curMonth += 1;
            int tDays = daysInMonth(curMonth, curYear);
            if (millis >= tDays * 86400) {
                months++;
                millis -= (tDays * 86400);
            } else
                break;
        }

        if (months > 0 || years > 0) {
            sb.append(months);
            sb.append(" months, ");
        }

        if (millis >= 604800) {
            weeks = millis / 604800;
            millis -= (weeks * 604800);
        }

        if (weeks > 0 || months > 0) {
            sb.append(weeks);
            sb.append(" weeks, ");
        }

        if (millis >= 86400) {
            days = millis / 86400;
            millis -= (days * 86400);

        }

        if (days > 0 || weeks > 0) {
            sb.append(days);
            sb.append(" days, ");
        }

        if (millis >= 3600) {
            hours = millis / 3600;
            millis -= (hours * 3600);
        }
        if (hours > 0 || days > 0) {
            sb.append(hours);
            sb.append(" hours, ");
        }

        if (millis >= 60) {
            minutes = millis / 60;
            millis -= (minutes * 60);
        }
        if (minutes > 0 || hours > 0) {
            sb.append(minutes);
            sb.append(" minutes and ");
        }

        sb.append(millis);
        sb.append(" seconds");
        return sb.toString();
    }

    public static long parseTime(String time) {
        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);

        while (matcher.find()) {
            String s = matcher.group();
            long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

            switch (type) {
                case "s":
                    totalTime += value;
                    found = true;
                    break;
                case "m":
                    totalTime += value * 60;
                    found = true;
                    break;
                case "h":
                    totalTime += value * 60 * 60;
                    found = true;
                    break;
                case "d":
                    totalTime += value * 60 * 60 * 24;
                    found = true;
                    break;
                case "w":
                    totalTime += value * 60 * 60 * 24 * 7;
                    found = true;
                    break;
                case "M":
                    totalTime += value * 60 * 60 * 24 * 30;
                    found = true;
                    break;
                case "y":
                    totalTime += value * 60 * 60 * 24 * 365;
                    found = true;
                    break;
            }
        }

        return !found ? -1 : totalTime * 1000;
    }

}
