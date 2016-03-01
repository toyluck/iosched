package com.atrue.hyc.searchview.provider;

/**
 * Created by HYC on 2016/2/24.
 * {@code uri} 能被 {@code ContentProvider } 认可的 集合
 * {@code * }表示匹配任意长度的任意字符.
 *
 * <p/>
 * 所有 table 表中的 标准内容Uri 的枚举
 */
public enum ScheduleUriEnum {
    CONTACT(100, "contacts", ScheduleConract.ContactColmns.CONTACT_ID, false, MyDataBase.Tables.CONTACTS),
    CONTACT_ID(101, "contacts/*", ScheduleConract.ContactColmns.CONTACT_ID, true, null);
    public int _code;
    public String _path;
    public String _contentType;
    public String _table;

    ScheduleUriEnum(int code, String path, String contentType, boolean item, String table) {
        this._code = code;
        this._path = path;
        this._contentType = contentType;
        this._contentType = item ? ScheduleConract.makeContentItemType(contentType) : ScheduleConract.makeContentItemType(contentType);
        this._table = table;
    }

}
