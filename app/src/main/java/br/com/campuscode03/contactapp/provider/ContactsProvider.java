package br.com.campuscode03.contactapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class ContactsProvider extends ContentProvider {

    private ContactsDatabaseHelper mDBHelper;

    public static final int URI_CONTACT = 1;
    public static final int URI_CONTACT_ID = 2;


    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Contacts.AUTHORITY, "contact", URI_CONTACT);
        sUriMatcher.addURI(Contacts.AUTHORITY, "contact/#", URI_CONTACT_ID);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new ContactsDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch(sUriMatcher.match(uri)){
            case URI_CONTACT:
                qb.setTables(ContactModel.TABLE_NAME);
                break;
            case URI_CONTACT_ID:
                qb.setTables(ContactModel.TABLE_NAME);
                qb.appendWhere(ContactModel._ID + "=" + uri.getLastPathSegment());
                break;
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case URI_CONTACT:
                return ContactModel.CONTENT_TYPE;
            case URI_CONTACT_ID:
                return ContactModel.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch(sUriMatcher.match(uri)){
            case URI_CONTACT:
                return insertContact(contentValues);
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    private Uri insertContact(ContentValues values){
        Uri resultUri = null;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long id = db.insert(ContactModel.TABLE_NAME, null, values);

        if(id > 0){
            resultUri = ContentUris.withAppendedId(ContactModel.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(resultUri, null);
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String id;

        switch (sUriMatcher.match(uri)){
            case URI_CONTACT:
                return db.delete(ContactModel.TABLE_NAME, null, null);
            case URI_CONTACT_ID:
                id = uri.getLastPathSegment();
                return db.delete(ContactModel.TABLE_NAME, ContactModel._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}