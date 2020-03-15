package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableContent
{
    private final StringProperty pCode;
    private final StringProperty pFullname;
    private final StringProperty pShortname;

    public TableContent(String pCode, String pFullname, String pShortname)
    {
        this.pFullname = new SimpleStringProperty(pFullname);
        this.pShortname = new SimpleStringProperty(pShortname);
        this.pCode = new SimpleStringProperty(pCode);
    }

    public StringProperty getpCode()
    {
        return pCode;
    }

    public StringProperty getpFullname()
    {
        return pFullname;
    }

    public StringProperty getpShortname()
    {
        return pShortname;
    }
}
