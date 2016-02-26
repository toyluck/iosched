package com.atrue.hyc.searchview.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.util.SparseArray;

/**
 * Created by HYC on 2016/2/24.
 * 提供方法 帮助 {@link android.net.Uri} 匹配{@link ScheduleUriEnum}.
 * <p>
 * 所有的方法都是线程安全的,除了 {@link #buildUriMatcher()} 和 {@link #buildEnumsMap()}.所以他们放在了 构造方法中执行
 */
public class ScheduleProviderUriMatcher {

    private final UriMatcher _uriMatcher;

    private SparseArray<ScheduleUriEnum> _EnumMap = new SparseArray<>();

    public ScheduleProviderUriMatcher() {
        _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        buildUriMatcher();
    }

    private void buildUriMatcher() {
        String authority = ScheduleConract.CONTENT_AUTHORITY;
        ScheduleUriEnum[] uris = ScheduleUriEnum.values();
        for (int i = 0; i < uris.length; i++) {
            _uriMatcher.addURI(authority, uris[i]._path, uris[i]._code);
        }
        buildEnumsMap();
    }

    private void buildEnumsMap() {
        ScheduleUriEnum[] uris = ScheduleUriEnum.values();
        for (ScheduleUriEnum uri : uris
                ) {
            _EnumMap.put(uri._code, uri);
        }
    }

    public ScheduleUriEnum matchUri(Uri uri) {
        int code = _uriMatcher.match(uri);
        try {
            return matchCode(code);
        } catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    public ScheduleUriEnum matchCode(int code) {
        ScheduleUriEnum uriEnum = _EnumMap.get(code);
        if (uriEnum != null) {
            return uriEnum;
        } else {
            throw new UnsupportedOperationException("Unknow uri with code " + code);
        }
    }
}
