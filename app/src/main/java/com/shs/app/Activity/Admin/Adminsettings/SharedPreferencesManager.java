package com.shs.app.Activity.Admin.Adminsettings;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "TableViewPrefs";
    private static final String KEY_TABLE_DATA = "tableData";
    private static final String KEY_TABLE_STATE = "tableState";

    public static void saveTableData(Context context, String[][] data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        StringBuilder dataString = new StringBuilder();
        for (String[] rowData : data) {
            for (String cellData : rowData) {
                dataString.append(cellData).append(",");
            }
            dataString.append(";");
        }
        editor.putString(KEY_TABLE_DATA, dataString.toString());
        editor.apply();
    }

    public static String[][] getTableData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String dataString = prefs.getString(KEY_TABLE_DATA, "");
        String[] rows = dataString.split(";");
        String[][] data = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cells = rows[i].split(",");
            data[i] = cells;
        }
        return data;
    }

    public static void saveTableState(Context context, String tableState) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_TABLE_STATE, tableState);
        editor.apply();
    }

    public static String getTableState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TABLE_STATE, "");
    }
}
