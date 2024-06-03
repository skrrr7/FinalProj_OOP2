package com.example.pacman.UI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HeaderRenderer extends JLabel implements TableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean hasFocus,
                                                   boolean isSelected,
                                                   int row,
                                                   int col)
    {
        setText(value.toString());
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        return this;
    }
}
