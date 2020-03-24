package com.example.bechitra.walleto.utility;

import android.content.Context;

import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.room.entity.Transaction;
import com.example.bechitra.walleto.table.PrimeTable;

import java.text.ParseException;
import java.util.*;

import butterknife.Optional;

public class DataProcessor {
    //private DatabaseHelper db;
    private DateManager dateManager;
    private List<Transaction> transactions;

    public DataProcessor() {
        this.dateManager = new DateManager();
    }

    public DataProcessor(Context context) {
        this.dateManager = new DateManager();
    }



    public DataProcessor(List<Transaction> transactions) {
        this.transactions = transactions;
        this.dateManager = new DateManager();
    }

    public List<DataOrganizer> getProcessedTransactionOfCurrentMonth(List<Transaction> transactions,  String tag) {
        String today = dateManager.getCurrentDate();
        String[] date = dateManager.getSeparatedDateArray(today);

        String upperBound = date[0]+"/"+date[1]+"/"+date[2];
        String lowerBound = "01/"+date[1]+"/"+date[2];

        List<DataOrganizer> data = new ArrayList<>();
        List<Transaction> row = getTransactionsByRange(transactions, lowerBound, upperBound);

        Map<String, List<Transaction>> map = getDailyData(row);

        for(Map.Entry<String, List<Transaction>> m : map.entrySet()) {
            data.add(new DataOrganizer(new DataLabel(m.getKey(), getBalanceCalculation(m.getValue())), true));
            for(Transaction p : m.getValue())
                data.add(new DataOrganizer(p, tag,false));
        }

        return data;
    }

    public double getAmountByTag(List<Transaction> transactions, String tag) {
        double balance = 0;

        for (Transaction transaction : transactions)
            if (transaction.getTag().equals(tag))
                balance += transaction.getAmount();

        return balance;
    }

    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public List<Transaction> getTransactionsByRange(List<Transaction> transactions, String lowerBound, String upperBound) {
        List<Transaction> filteredTransaction = new ArrayList<>();

        for (Transaction transaction : transactions)
            if (new DateManager().isDateWithinRange(transaction.getDate(), lowerBound, upperBound))
                filteredTransaction.add(transaction);

        return filteredTransaction;
    }

    public List<Transaction> getTransactionsByTag(List<Transaction> transactions, String tag) {
        List<Transaction> filteredTransaction = new ArrayList<>();
        for (Transaction transaction : transactions)
            if (transaction.getTag().equals(tag))
                filteredTransaction.add(transaction);

        return filteredTransaction;
    }

    public List<DataOrganizer> getProcessedDailyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        List<DataOrganizer> organizedData = new ArrayList<>();
        List<Transaction> row = getTransactionsByRange(transactions, lowerBound, upperBound);
        Map<String, List<Transaction>> mapList = getDailyData(row);
        String tag = transactions.get(0).getTag();

        for(Map.Entry<String, List<Transaction>> entry : mapList.entrySet()) {
            organizedData.add(new DataOrganizer(new DataLabel(entry.getKey(), getBalanceCalculation(entry.getValue())), true));
            List<MapHelper> map = getCategorisedData(entry.getValue());
            for(MapHelper m : map)
                organizedData.add(new DataOrganizer(new CategoryProcessor(m, tag),false));
        }

        return organizedData;
    }

    public List<DataOrganizer> getProcessedWeeklyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        List<DataOrganizer> list = new ArrayList<>();
        Map<String, Map<Integer, List<Transaction>>> weekData = getWeeklyData(transactions, lowerBound, upperBound);
        String tag = transactions.get(0).getTag();

        for(Map.Entry<String, Map<Integer, List<Transaction>>> entry : weekData.entrySet()) {
            double amount = getBalanceCalculation(entry.getValue());
            list.add(new DataOrganizer(new DataLabel("YEAR "+entry.getKey(), ""+amount), true));

            for(Map.Entry<Integer, List<Transaction>> map : entry.getValue().entrySet()) {
                String balance = getBalanceCalculation(map.getValue());
                list.add(new DataOrganizer(new DataLabel("Week "+map.getKey(), balance), true));

                List<MapHelper> helper = getCategorisedData(map.getValue());

                for(MapHelper m : helper)
                    list.add(new DataOrganizer(new CategoryProcessor(m, tag), false));

            }
        }

        return list;
    }

    public List<DataOrganizer> getProcessedMonthlyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        Map<String, List<Transaction>> map = getMonthlyData(transactions, lowerBound, upperBound);
        List<DataOrganizer> organizerList = new ArrayList<>();
        String tag = null;
        if(!transactions.isEmpty())
            tag = transactions.get(0).getTag();

        for(Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            organizerList.add(new DataOrganizer(new DataLabel(entry.getKey(), getBalanceCalculation(entry.getValue())), true));
            List<MapHelper> lst = getCategorisedData(entry.getValue());

            for(MapHelper m : lst)
                organizerList.add(new DataOrganizer(new CategoryProcessor(m, tag), false));
        }

        return organizerList;
    }

    public List<DataOrganizer> getProcessedYearlyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        Map<String, List<Transaction>> map = getYearlyData(transactions, lowerBound, upperBound);
        List<DataOrganizer> organizerList = new ArrayList<>();
        String tag = transactions.get(0).getTag();

        for(Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            organizerList.add(new DataOrganizer(new DataLabel(entry.getKey(), getBalanceCalculation(entry.getValue())), true));
            List<MapHelper> categorisedData = getCategorisedData(entry.getValue());
            for(MapHelper m : categorisedData)
                organizerList.add(new DataOrganizer(new CategoryProcessor(m, tag), false));
        }

        return organizerList;
    }

    public double getBalanceOfTransaction(List<Transaction> transactions) {
        double balance = 0;

        for(Transaction transaction : transactions)
            balance += transaction.getAmount();

        return balance;
    }

    public String getBalanceCalculation(List<Transaction> primeTables) {
        double dtr = 0;
        for(Transaction ptb : primeTables)
            dtr += ptb.getAmount();

        return Double.toString(dtr);
    }


    private Map<String, List<Transaction>> getDailyData(List<Transaction> data) {
        Map<String, List<Transaction>> map = new LinkedHashMap<>();
        List<Transaction> sortedList = sortList(data);
        for(int i = sortedList.size()-1 ; i >= 0; i--) {
            if(map.containsKey(sortedList.get(i).getDate()))
                map.get(sortedList.get(i).getDate()).add(sortedList.get(i));
            else {
                map.put(sortedList.get(i).getDate(), new ArrayList<Transaction>());
                map.get(sortedList.get(i).getDate()).add(sortedList.get(i));
            }
        }


        return map;
    }

    private List<Transaction> sortList(List<Transaction> data) {
        Collections.sort(data, (o1, o2) ->{
                if(dateManager.dateDifference(o1.getDate(), o2.getDate()) > 0)
                    return -1;
                else if (dateManager.dateDifference(o1.getDate(), o2.getDate()) == 0)
                    return 0;
                else
                    return 1;
        });

        return data;
    }


    public Map<String, List<Transaction>> getMonthlyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        List<Transaction> rowData = getTransactionsByRange(transactions, lowerBound, upperBound);
        Map<String, List<Transaction>> map = new LinkedHashMap<>();

        List<String> month = dateManager.getAllMonthsWithinRange(lowerBound, upperBound);

        for (int i = month.size()-1 ; i >= 0; i--) {
            String pattern = dateManager.getMonthWithYear(month.get(i));

            for(Transaction p : rowData) {
                if(p.getDate().contains(pattern)) {
                    String date = dateManager.getMonthNameWithYear(p.getDate());
                    if(map.containsKey(date))
                        map.get(date).add(p);
                    else {
                        map.put(date, new ArrayList<Transaction>());
                        map.get(date).add(p);
                    }
                }
            }
        }

        return map;
    }

    public Map<String, List<Transaction>> getYearlyTransaction(List<Transaction> transactions, String lowerBound, String upperBound) {
        List<Transaction> rowData = getTransactionsByRange(transactions, lowerBound, upperBound);

        int sYear = Integer.parseInt(dateManager.getYearFromDate(lowerBound));
        int eYear = Integer.parseInt(dateManager.getYearFromDate(upperBound));

        Map<String, List<Transaction>> map = new LinkedHashMap<>();

        for(int i = eYear; i >= sYear; i--) {
            for(Transaction p : rowData) {
                if(p.getDate().contains("/"+i)) {
                    if(map.containsKey(""+i))
                        map.get(""+i).add(p);
                    else {
                        map.put(""+i, new ArrayList<Transaction>());
                        map.get(""+i).add(p);
                    }
                }
            }
        }

        return map;
    }

    public Map<String, List<Transaction>> getYearlyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        List<Transaction> rowData = getTransactionsByRange(transactions, lowerBound, upperBound);

        int sYear = Integer.parseInt(dateManager.getYearFromDate(lowerBound));
        int eYear = Integer.parseInt(dateManager.getYearFromDate(upperBound));

        Map<String, List<Transaction>> map = new LinkedHashMap<>();

        for(int i = eYear; i >= sYear; i--) {
            for(Transaction p : rowData) {
                if(p.getDate().contains("/"+i)) {
                    if(map.containsKey(""+i))
                        map.get(""+i).add(p);
                    else {
                        map.put(""+i, new ArrayList<Transaction>());
                        map.get(""+i).add(p);
                    }
                }
            }
        }

        return map;
    }

    public Map<String, Map<Integer, List<Transaction>>> getWeeklyData(List<Transaction> transactions, String lowerBound, String upperBound) {
        Map<String, List<Transaction>> linkedMap = getYearlyData(transactions, lowerBound, upperBound);
        Map<String, Map<Integer, List<Transaction>>> weekData = new LinkedHashMap<>();

        for(Map.Entry<String, List<Transaction>> map : linkedMap.entrySet()) {
            weekData.put(map.getKey(), new TreeMap<Integer, List<Transaction>>());
            Map<Integer, List<Transaction>> data = weekData.get(map.getKey());

            for(Transaction p : map.getValue()) {
                int week = dateManager.getWeekOfYear(p.getDate());
                if(data.containsKey(week)) {
                    data.get(week).add(p);
                } else {
                    data.put(week, new ArrayList<Transaction>());
                    data.get(week).add(p);
                }
            }
        }

        return weekData;
    }

    public double getBalanceCalculation(Map<Integer, List<Transaction>> map) {
        double amount = 0;
        for(Map.Entry<Integer, List<Transaction>> entry : map.entrySet())
            amount += Double.parseDouble(getBalanceCalculation(entry.getValue()));

        return amount;
    }

    public List<MapHelper> getCategorisedData(List<Transaction> list) {
        Map<String, List<Transaction>> listMap = new HashMap<>();
        List<MapHelper> helperList = new ArrayList<>();

        for(Transaction p : list) {
            if(listMap.containsKey(p.getCategory())) {
                listMap.get(p.getCategory()).add(p);
            } else {
                listMap.put(p.getCategory(), new ArrayList<Transaction>());
                listMap.get(p.getCategory()).add(p);
            }
        }

        for(Map.Entry<String, List<Transaction>> entry : listMap.entrySet()) {
            Bound bound = getBound(entry.getValue());
            MapHelper helper = new MapHelper(entry.getKey(), Integer.toString(entry.getValue().size()),
                                        getBalanceCalculation(entry.getValue()), bound.getLowerBound(), bound.getUpperBound());
            helperList.add(helper);
        }

        return helperList;
    }

    private Bound getBound(List<Transaction> list) {
        list = sortList(list);
        return new Bound(list.get(0).getDate(), list.get(list.size()-1).getDate());
    }
}