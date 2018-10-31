package com.example.bechitra.walleto.utility;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateManager {
    public String getCurrentDate() {
        String[] str = getCurrentDateInArray();
        return (str[2]+"/"+str[1]+"/"+str[0]);
    }

    public String getCurrentMonthWithYear() {
        String[] str = getCurrentDateInArray();
        return ("/"+str[1]+"/"+str[0]);
    }

    public String getMonthWithYear(String date) {
        String[] str = getSeparatedDateArray(date);
        return ("/"+str[1]+"/"+str[2]);
    }

    public String addDate(String current, int toAdd) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
        Date date = df.parse(current);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(GregorianCalendar.DAY_OF_MONTH, toAdd);

        return df.format(cal.getTime());
    }

    public long dateDifference(String dateOne, String dateTwo) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date1 = myFormat.parse(dateOne);
            Date date2 = myFormat.parse(dateTwo);
            long diff = date2.getTime() - date1.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String[] getCurrentDateInArray() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dateType = dateFormat.format(cal.getTime());
        StringTokenizer stk = new StringTokenizer(dateType, " ");
        String date = stk.nextToken();

        StringTokenizer token = new StringTokenizer(date, "/");
        String[] str = new String[3];
        int i = 0;
        while(token.hasMoreTokens()) {
            str[i] = token.nextToken();
            i++;
        }

        return str;
    }

    public String getCurrentDateString() {
        String [] month = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String [] str = getCurrentDateInArray();

        return (str[2]+" "+month[Integer.parseInt(str[1])-1]+" "+str[0]);
    }

    public boolean isCurrentMonth(String date) {
        StringTokenizer stk = new StringTokenizer(date, "/");
        int ii = 0;
        String []str = new String[3];
        while(stk.hasMoreTokens()) {
            str[ii] = stk.nextToken();
            ii++;
        }

        if(getCurrentMonthWithYear().equals("/"+str[1]+"/"+str[2]))
            return true;

        return false;
    }

    public String monthStringBuilder(String date) {
        String []str = getSeparatedDateArray(date);
        String name = getMonthName(Integer.parseInt(str[1]));

        return (str[0]+" "+name+" "+str[2]);
    }

    public String getMonthNameWithYear(String date) {
        String []str = getSeparatedDateArray(date);
        String name = getMonthName(Integer.parseInt(str[1]));

        return (name+" "+str[2]);
    }

    public String getMonthName(int month) {
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return str[month-1];
    }

    public String[] getAllMonthName() {
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return str;
    }

    public int getMonthID(String month) {
        String [] str = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        for(int i = 0; i < str.length; i++)
            if(str[i].equals(month))
                return i+1;

        return 0;
    }

    public  int getWeekOfYear(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public String[] getSeparatedDateArray(String date) {
        String []str = new String[3];
        StringTokenizer stk = new StringTokenizer(date, "/");
        int i = 0;
        while(stk.hasMoreTokens()) {
            str[i] = stk.nextToken();
            i++;
        }

        return str;
    }

    public String stringFormatter(String string) {
        StringTokenizer stk = new StringTokenizer(string, " ");
        String str = "";
        int i = 0;
        while(stk.hasMoreTokens()) {
            i++;
            String token = stk.nextToken();
            char [] ch = token.toCharArray();
            String bind = new String(ch, 1, ch.length-1);
            str += (ch[0]+bind.toLowerCase());
            if(i >= 1)
                str += " ";
        }

        return str;
    }

    public boolean validateDate(String date) {
        char[] ch = date.toCharArray();

        for(int i = 0; i < ch.length; i++) {
            if(((int)ch[i] >= 48 && (int)ch[i] <= 57) || ch[i] == '/')
                continue;
            else
                return false;
        }

        StringTokenizer stk = new StringTokenizer(date, "/");

        if(stk.countTokens() != 3) {
            Log.d("error", "i am creating error "+stk.countTokens());
            return false;
        }

        String dateArray[] = getSeparatedDateArray(date);

        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int year = Integer.parseInt(dateArray[2]);

        if(!(month > 0 && month < 13))
            return false;

        int currYear = Integer.parseInt(getYearFromDate(getCurrentDate()));

        if((currYear - year) < 0 || (currYear - year) > 15)
            return false;

        int dayMax = getNumberOfDayFromMonth(month, year);

        if(day > dayMax || day < 1)
            return false;

        return true;
    }

    public String getYearFromDate(String date) {
        String str = "";
        StringTokenizer stk = new StringTokenizer(date, "/");
        while(stk.hasMoreTokens())
            str = stk.nextToken();

        return str;
    }

    public List<String> getAllMonthsWithinRange(String startingDate, String endingDate) {
        List<String> dates = new ArrayList<>();

        int startYear = Integer.parseInt(getYearFromDate(startingDate));
        int endYear = Integer.parseInt(getYearFromDate(endingDate));
        int diff = endYear - startYear;
        String date = startingDate;

        StringTokenizer stk = new StringTokenizer(date, "/");

        int day = Integer.parseInt(stk.nextToken());
        int month = Integer.parseInt(stk.nextToken());
        int year = Integer.parseInt(stk.nextToken());

        if(diff == 0) {
            while (dateDifference(date, endingDate) >= 0) {

                dates.add(date);
                month++;
                char [] ch = Integer.toString(month).toCharArray();
                if(ch.length > 1)
                    date = ("01/" + month + "/" + year);
                else
                    date = ("01/0"+ month + "/" + year);
            }
        } else {
            StringTokenizer st = new StringTokenizer(endingDate, "/");

            int eday = Integer.parseInt(st.nextToken());
            int emonth = Integer.parseInt(st.nextToken());
            int eyear = Integer.parseInt(st.nextToken());

            for(int i = year; i <= eyear; i++) {
                for(int j = month; j <= 12; j++) {
                    if(i == year && j == month) {
                        dates.add(date);
                        month = 1;
                    }
                    else
                    if(!(i == endYear && j > emonth)) {
                        char [] ch = Integer.toString(j).toCharArray();
                        if(ch.length > 1)
                            dates.add("01/" + j + "/" + i);
                        else
                            dates.add("01/0" + j + "/" + i);
                    }
                }
            }
        }

        return dates;
    }

    public int getNumberOfDayFromMonth(int month, int year) {
        month = month-1;
        int days=0;
        if(month>=0 && month<12) {
            try {
                Calendar calendar = Calendar.getInstance();
                int date = 1;
                calendar.set(year, month, date);
                return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            } catch (Exception e) {
                return -1;
            }
        }
        return 0;
    }

    public List<String> allFirstDayOfWeekFromMonth(String date) {
        List<String> dates = new ArrayList<>();
        String monthYear = getMonthWithYear(date);

        StringTokenizer stk = new StringTokenizer(date, "/");

        int day = Integer.parseInt(stk.nextToken());
        int month = Integer.parseInt(stk.nextToken());
        int year = Integer.parseInt(stk.nextToken());
        int limit = getNumberOfDayFromMonth(month, year);

        while(day <= limit) {
            char[] ch = Integer.toString(day).toCharArray();
            if(ch.length > 1)
                dates.add(day+""+monthYear);
            else dates.add("0"+day+""+monthYear);
            day += 7;
        }

        return dates;
    }

    public String getDate(int day, int month, int year) {
        char[] d = Integer.toString(day).toCharArray();
        char[] m = Integer.toString(month).toCharArray();

        String da = ""+day, mo = ""+month, ya = ""+year;

        if(d.length < 2)
            da = "0"+day;
        if(m.length < 2)
            mo = ("0"+month);

        return (da+"/"+mo+"/"+ya);
    }

    public String getPreviousMonth(String date) {
        String[] dates = getSeparatedDateArray(date);
        int month = Integer.parseInt(dates[1]);
        String pattern = date;
        if(month == 1) {
            int year = Integer.parseInt(dates[2]);
            year--;
            month = 12;
            pattern = "01"+"/"+month+"/"+Integer.toString(year);
        } else {
            char[] ch = Integer.toString(month).toCharArray();
            if(ch.length > 1)
                pattern = "01/" + (month - 1) + "/" + dates[2];
            else
                pattern = "01/0" + (month - 1) + "/" + dates[2];
        }

        return pattern;
    }
}
