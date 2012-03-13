package org.apache.oro.text.regex;

final class c
{
  static final int[] b = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 };
  static final char[] d = { '\000', '\001', '\001', '\001', '\004', '\004', '\004', '\007', '\007', '\t', '\n', '\n', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\001', '\f', '\f', '\000', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3' };
  static final char[] e = { '\f', '\r', '\020', '\021', '\n', '\013', '\032', '"' };
  static final char[] c = { '\007', '\b', '\t', '\022', '\023', '\026', '\027', '\030', '\031', '#', '$', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3' };

  static final boolean b(char paramChar, char[] paramArrayOfChar, int paramInt)
  {
    while (paramInt < paramArrayOfChar.length)
      if (paramChar == paramArrayOfChar[(paramInt++)])
        return true;
    return false;
  }

  static final int c(char[] paramArrayOfChar, int paramInt)
  {
    if (paramArrayOfChar == null)
      return -1;
    int i = d(paramArrayOfChar, paramInt);
    if (i == 0)
      return -1;
    if (paramArrayOfChar[paramInt] == '\r')
      return paramInt - i;
    return paramInt + i;
  }

  static final char b(char[] paramArrayOfChar, int paramInt)
  {
    return paramArrayOfChar[(paramInt + 3)];
  }

  static final int d(char[] paramArrayOfChar, int paramInt)
  {
    return paramArrayOfChar[(paramInt + 1)];
  }

  static final int c(int paramInt)
  {
    return paramInt + 2;
  }

  static final char e(char[] paramArrayOfChar, int paramInt)
  {
    return paramArrayOfChar[(paramInt + 2)];
  }

  static final boolean b(char paramChar)
  {
    return (Character.isLetterOrDigit(paramChar)) || (paramChar == '_');
  }

  static final int d(int paramInt)
  {
    return paramInt + 2;
  }

  static final int b(int paramInt)
  {
    return paramInt - 2;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.c
 * JD-Core Version:    0.6.0
 */