package com.example.bechitra.walleto.utility;

import android.content.Context;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.table.PrimeTable;
import java.util.*;

public class DataProcessor {
    private DatabaseHelper db;
    private DateManager dateManager;

    public DataProcessor(Context context) {
        db = new DatabaseHelper(context);
        this.dateManager = new DateManager();
    }

    public List<DataOrganizer> getProcessedDataCurrentMonth(String tableName) {
        String today = dateManager.getCurrentDate();
        String[] date = dateManager.getSeparatedDateArray(today);

        String lower = "01/"+date[1]+"/"+date[2];
        List<DataOrganizer> data = new ArrayList<>();
        List<PrimeTable> row = db.getDataWithinARange(tableName, lower, today);

        Map<String, List<PrimeTable>> map = getDailyData(row);

        for(Map.Entry<String, List<PrimeTable>> m : map.entrySet()) {
            data.add(new DataOrganizer(new DataLabel(m.getKey(), getBalanceCalculation(m.getValue())), true));
            for(PrimeTable p : m.getValue())
                data.add(new DataOrganizer(p, tableName,false));
        }

        return data;
    }

    public List<DataOrganizer> getProcessedDailyData(String table, String lowerBound, String upperBound) {
        List<DataOrganizer> organizedData = new ArrayList<>();
        List<PrimeTable> row = db.getDataWithinARange(table, lowerBound, upperBound);
        Map<String, List<PrimeTable>> mapList = getDailyData(row);

        for(Map.Entry<String, List<PrimeTable>> entry : mapList.entrySet()) {
            organizedData.add(new DataOrganizer(new DataLabel(entry.getKey(), getBalanceCalculation(entry.getValue())), true));
            List<MapHelper> map = getCategorisedData(entry.getValue());
            for(MapHelper m : map)
                organizedData.add(new DataOrganizer(new CategoryProcessor(m, table),false));
        }

        return organizedData;
    }

    public List<DataOrganizer> getProcessedWeeklyData(String table, String lowerBound, String upperBound) {
        List<DataOrganizer> list = new ArrayList<>();
        Map<String, Map<Integer, List<PrimeTable>>> weekData = getWeeklyData(table, lowerBound, upperBound);

        for(Map.Entry<String, Map<Integer, List<PrimeTable>>> entry : weekData.entrySet()) {
            double amount = getBalanceCalculation(entry.getValue());
            list.add(new DataOrganizer(new DataLabel("YEAR "+entry.getKey(), ""+amount), true));

            for(Map.Entry<Integer, List<PrimeTable>> map : entry.getValue().entrySet()) {
                String balance = getBalanceCalculation(map.getValue());
                list.add(new DataOrganizer(new DataLabel("Week "+map.getKey(), balance), true));

                List<MapHelper> helper = getCategorisedData(map.getValue());

                for(MapHelper m : helper)
                    list.add(new DataOrganizer(new CategoryProcessor(m, table), false));

            }
        }

        return list;
    }

    public List<DataOrganizer> getProcessedMonthlyData(String table, String lowerBound, String upperBound) {
        Map<String, List<PrimeTable>> map = getMonthlyData(table, lowerBound, upperBound);
        List<DataOrganizer> organizerList = new ArrayList<>();

        for(Map.Entry<String, List<PrimeTable>> entry : map.entrySet()) {
            organizerList.add(new DataOrganizer(new DataLabel(entry.getKey(), getBalanceCalculation(entry.getValue())), true));
            List<MapHelper> lst = getCategorisedData(entry.getValue());

            for(MapHelper m : lst)
                organizerList.add(new DataOrganizer(new CategoryProcessor(m, table), false));
        }

        return organizerList;
    }

    public List<DataOrganizer> getProcessedYearlyData(String table, String lowerBound, String upperBound) {
        Map<String, List<PrimeTable>> map = getYearlyData(table, lowerBound, upperBound);
        List<DataOrganizer> organizerList = new ArrayList<>();

        for(Map.Entry<String, List<PrimeTable>> entry : map.entrySet()) {
            organizerList.add(new DataOrganizer(new DataLabel(entry.getKey(), getBalanceCalculation(entry.getValue())), true));
            List<MapHelper> categorisedData = getCategorisedData(entry.getValue());
            for(MapHelper m : categorisedData)
                organizerList.add(new DataOrganizer(new CategoryProcessor(m, table), false));
        }

        return organizerList;
    }

    public String getBalanceCalculation(List<PrimeTable> primeTables) {
        double dtr = 0;
        for(PrimeTable ptb : primeTables)
            dtr += Double.parseDouble(ptb.getAmount());

        return Double.toString(dtr);
    }


    private Map<String, List<PrimeTable>> getDailyData(List<PrimeTable> data) {
        Map<String, List<PrimeTable>> map = new LinkedHashMap<>();
        List<PrimeTable> sortedList = sortList(data);
        for(int i = sortedList.size()-1 ; i >= 0; i--) {
            if(map.containsKey(sortedList.get(i).getDate()))
                map.get(sortedList.get(i).getDate()).add(sortedList.get(i));
            else {
                map.put(sortedList.get(i).getDate(), new ArrayList<PrimeTable>());
                map.get(sortedList.get(i).getDate()).add(sortedList.get(i));
            }
        }


        return map;
    }

    private List<PrimeTable> sortList(List<PrimeTable> data) {
        Collections.sort(data, new Comparator<PrimeTable>() {
            @Override
            public int compare(PrimeTable o1, PrimeTable o2) {
                if(dateManager.dateDifference(o1.getDate(), o2.getDate()) > 0)
                    return -1;
                else if (dateManager.dateDifference(o1.getDate(), o2.getDate()) == 0)
                    return 0;
                else
                    return 1;
            }
        });

        return data;
    }


    public Map<String, List<PrimeTable>> getMonthlyData(String table, String lowerBound, String upperBound) {
        List<PrimeTable> rowData = db.getDataWithinARange(table, lowerBound, upperBound);
        Map<String, List<PrimeTable>> map = new LinkedHashMap<>();

        List<String> month = dateManager.getAllMonthsWithinRange(lowerBound, upperBound);

        for (int i = month.size()-1 ; i >= 0; i--) {
            String pattern = dateManager.getMonthWithYear(month.get(i));

            for(PrimeTable p : rowData) {
                if(p.getDate().contains(pattern)) {
                    String date = dateManager.getMonthNameWithYear(p.getDate());
                    if(map.containsKey(date))
                        map.get(date).add(p);
                    else {
                        map.put(date, new ArrayList<PrimeTable>());
                        map.get(date).add(p);
                    }
                }
            }
        }

        return map;
    }

    public Map<String, List<PrimeTable>> getYearlyData(String table, String lowerBound, String upperBound) {
        List<PrimeTable> rowData = db.getDataWithinARange(table, lowerBound, upperBound);

        int sYear = Integer.parseInt(dateManager.getYearFromDate(lowerBound));
        int eYear = Integer.parseInt(dateManager.getYearFromDate(upperBound));

        Map<String, List<PrimeTable>> map = new LinkedHashMap<>();

        for(int i = eYear; i >= sYear; i--) {
            for(PrimeTable p : rowData) {
                if(p.getDate().contains("/"+i)) {
                    if(map.containsKey(""+i))
                        map.get(""+i).add(p);
                    else {
                        map.put(""+i, new ArrayList<PrimeTable>());
                        map.get(""+i).add(p);
                    }
                }
            }
        }

        return map;
    }

    public Map<String, Map<Integer, List<PrimeTable>>> getWeeklyData(String table, String lowerBound, String upperBound) {
        Map<String, List<PrimeTable>> linkedMap = getYearlyData(table, lowerBound, upperBound);
        Map<String, Map<Integer, List<PrimeTable>>> weekData = new LinkedHashMap<>();

        for(Map.Entry<String, List<PrimeTable>> map : linkedMap.entrySet()) {
            weekData.put(map.getKey(), new TreeMap<Integer, List<PrimeTable>>());
            Map<Integer, List<PrimeTable>> data = weekData.get(map.getKey());

            for(PrimeTable p : map.getValue()) {
                int week = dateManager.getWeekOfYear(p.getDate());
                if(data.containsKey(week)) {
                    data.get(week).add(p);
                } else {
                    data.put(week, new ArrayList<PrimeTable>());
                    data.get(week).add(p);
                }
            }
        }

        return weekData;
    }

    public double getBalanceCalculation(Map<Integer, List<PrimeTable>> map) {
        double amount = 0;
        for(Map.Entry<Integer, List<PrimeTable>> entry : map.entrySet())
            amount += Double.parseDouble(getBalanceCalculation(entry.getValue()));

        return amount;
    }

    public List<MapHelper> getCategorisedData(List<PrimeTable> list) {
        Map<String, List<PrimeTable>> listMap = new HashMap<>();
        List<MapHelper> helperList = new ArrayList<>();

        for(PrimeTable p : list) {
            if(listMap.containsKey(p.getCategory())) {
                listMap.get(p.getCategory()).add(p);
            } else {
                listMap.put(p.getCategory(), new ArrayList<PrimeTable>());
                listMap.get(p.getCategory()).add(p);
            }
        }

        for(Map.Entry<String, List<PrimeTable>> entry : listMap.entrySet()) {
            Bound bound = getBound(entry.getValue());
            MapHelper helper = new MapHelper(entry.getKey(), Integer.toString(entry.getValue().size()),
                                        getBalanceCalculation(entry.getValue()), bound.getLowerBound(), bound.getUpperBound());
            helperList.add(helper);
        }

        return helperList;
    }

    private Bound getBound(List<PrimeTable> list) {
        list = sortList(list);
        return new Bound(list.get(0).getDate(), list.get(list.size()-1).getDate());
    }
}