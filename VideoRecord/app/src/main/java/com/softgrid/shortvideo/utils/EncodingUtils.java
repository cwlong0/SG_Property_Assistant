package com.softgrid.shortvideo.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public final class EncodingUtils
{

    public static final int CR = 13;
    public static final int LF = 10;
    public static final int SP = 32;
    public static final int HT = 9;
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static String getString(byte[] data, int offset, int length, String charset)
    {
        try
        {
            return new String(data, offset, length, charset);
        }
        catch (UnsupportedEncodingException e) {}
        return new String(data, offset, length);
    }

    public static String getString(byte[] data, String charset)
    {
        return getString(data, 0, data.length, charset);
    }

    public static byte[] getBytes(String data, String charset)
    {
        try
        {
            return data.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {}
        return data.getBytes();
    }

    public static byte[] getAsciiBytes(String data)
    {
        return data.getBytes(ASCII);
    }

    public static String getAsciiString(byte[] data, int offset, int length)
    {
        return new String(data, offset, length, ASCII);
    }

    public static String getAsciiString(byte[] data)
    {
        return getAsciiString(data, 0, data.length);
    }
}
