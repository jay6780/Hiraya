package com.shs.app.Adapter.tableAdapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;

public class SeparationLineTableDecoration extends TableView<String[]> {

    private final Paint paint = new Paint();
    private final int separatorColor;
    private final float separatorHeight;
    private final TableDataAdapter<String[]> tableDataAdapter;

    public SeparationLineTableDecoration(Context context, int separatorColor, float separatorHeight, TableDataAdapter<String[]> tableDataAdapter) {
        super(context);
        this.separatorColor = separatorColor;
        this.separatorHeight = separatorHeight;
        this.tableDataAdapter = tableDataAdapter;
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(separatorColor);
        paint.setStrokeWidth(separatorHeight);

        TableColumnModel tableColumnModel = getColumnModel();

        int columns = tableColumnModel.getColumnCount();
        int rows = tableDataAdapter.getCount(); // Use getCount() directly

        int width = getWidth();
        int height = getHeight();

        // Draw horizontal lines between rows
        for (int i = 1; i < rows; i++) {
            float y = i * height / rows;
            canvas.drawLine(0, y, width, y, paint);
        }

        // Draw vertical lines between columns
        float x = 0;
        for (int i = 0; i < columns; i++) {
            if (tableColumnModel instanceof TableColumnWeightModel) {
                x += ((TableColumnWeightModel) tableColumnModel).getColumnWeight(i) * width / 100;
            } else {
                x += tableColumnModel.getColumnWidth(i, width);
            }
            canvas.drawLine(x, 0, x, height, paint);
        }

    }

}
