package com.example.bechitra.walleto;

import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class StringPatternCreator {
    public String getCurrentDate() {
        String[] str = getCurrentDateInArray();
        return (Integer.toString(Integer.parseInt(str[2]))+"/"+Integer.toString(Integer.parseInt(str[1]))+"/"+str[0]);
    }

    public String getCurrentMonthWithYear() {
        String[] str = getCurrentDateInArray();
        return ("/"+Integer.toString(Integer.parseInt(str[1]))+"/"+str[0]);
    }

    public String getMonthWithYear(String date) {
        String[] str = getSeparatedDateArray(date);
        return ("/"+Integer.toString(Integer.parseInt(str[1]))+"/"+str[2]);
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

    public String getYearFromDate(String date) {
        String str = "";
        StringTokenizer stk = new StringTokenizer(date, "/");
        while(stk.hasMoreTokens())
            str = stk.nextToken();

        return str;
    }

    public String getPreviousMonth(String date) {
        String[] dates = getSeparatedDateArray(date);
        int month = Integer.parseInt(dates[1]);
        String pattern = date;
        if(month == 1) {
            int year = Integer.parseInt(dates[2]);
            year--;
            month = 12;
            pattern = "1"+"/"+Integer.toString(month)+"/"+Integer.toString(year);
        } else
            pattern = "1"+"/"+Integer.toString(month-1)+"/"+dates[2];


        return pattern;
    }
}
