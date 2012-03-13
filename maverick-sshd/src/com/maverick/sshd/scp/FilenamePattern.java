package com.maverick.sshd.scp;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.PrintStream;

public class FilenamePattern
  implements Cloneable, FileFilter, FilenameFilter
{
  public static final char PAT_ESCAPE = '\\';
  public static final char PAT_ANY = '?';
  public static final char PAT_CLOSURE = '*';
  public static final char PAT_SET_OPEN = '[';
  public static final char PAT_SET_CLOSE = ']';
  public static final char PAT_SET_THRU = '-';
  public static final char PAT_SET_EXCL = '^';
  public static final char PAT_DIR_SEP = '/';
  public static final char PAT_NEGATE = '!';
  protected static final char DIR_SEP1;
  protected static final char DIR_SEP2;
  protected static final boolean IGNORE_CASE;
  protected char[] m_pat;
  protected boolean m_ignoreCase = IGNORE_CASE;
  protected boolean m_hasDirSep;
  protected char m_escape = '\\';
  protected char m_any = '?';
  protected char m_closure = '*';
  protected char m_setOpen = '[';
  protected char m_setClose = ']';
  protected char m_setThru = '-';
  protected char m_setExcl = '^';
  protected char m_dirSep = '/';
  protected char m_negate = '!';

  public static boolean matches(String paramString1, String paramString2)
  {
    return new FilenamePattern(paramString1).matches(paramString2);
  }

  public static void main(String[] paramArrayOfString)
  {
    String str1 = "?";
    if (paramArrayOfString.length < 3)
    {
      System.out.println("usage:  java " + FilenamePattern.class.getName() + " pattern filename t|f|m");
      System.exit(255);
    }
    try
    {
      String str2 = paramArrayOfString[0];
      String str3 = paramArrayOfString[1];
      str1 = paramArrayOfString[2];
      FilenamePattern localFilenamePattern = new FilenamePattern();
      System.out.println("pattern:  \"" + str2 + "\"");
      System.out.println("filename: \"" + str3 + "\"");
      localFilenamePattern.setPattern(str2);
      boolean bool = localFilenamePattern.matches(str3);
      System.out.print("-> " + (bool ? "true, " : "false, "));
      System.out.println((!bool) && (str1.equals("f")) ? "pass" : (bool) && (str1.equals("t")) ? "pass" : "FAIL ***");
    }
    catch (Exception localException)
    {
      System.out.println("Exception: " + localException.getClass().getName());
      System.out.println(localException.getMessage());
      if ((localException instanceof IllegalArgumentException))
        System.out.println("-> " + (str1.equals("m") ? "pass" : "FAIL ***"));
      else if (!(localException instanceof IllegalArgumentException))
        localException.printStackTrace(System.out);
    }
  }

  protected static boolean compareCharIgnoreCase(char paramChar1, char paramChar2, char paramChar3)
  {
    int i = Character.toLowerCase(paramChar1);
    int j = Character.toLowerCase(paramChar2);
    int k = j;
    if (paramChar2 != paramChar3)
      k = Character.toLowerCase(paramChar3);
    if ((i >= j) && (i <= k))
      return true;
    i = Character.toUpperCase(paramChar1);
    j = Character.toUpperCase(paramChar2);
    k = j;
    if (paramChar2 != paramChar3)
      k = Character.toUpperCase(paramChar3);
    return (i >= j) && (i <= k);
  }

  public FilenamePattern()
  {
  }

  public FilenamePattern(String paramString)
  {
    setPattern(paramString);
  }

  public FilenamePattern(String paramString, boolean paramBoolean)
  {
    setIgnoreCase(paramBoolean);
    setPattern(paramString);
  }

  public String getPattern()
  {
    if (this.m_pat == null)
      return null;
    return new String(this.m_pat);
  }

  public void setPattern(String paramString)
  {
    if (paramString == null)
      throw new NullPointerException("Null pattern");
    if (!isValidPattern(paramString))
      throw new IllegalArgumentException("Malformed pattern: \"" + paramString + "\"");
    this.m_pat = paramString.toCharArray();
    this.m_hasDirSep = false;
    if (this.m_dirSep != 0)
    {
      int i = this.m_pat.length;
      for (int j = 0; j < i; j++)
      {
        if (this.m_pat[j] == 0)
          continue;
        if (this.m_pat[j] == this.m_escape)
        {
          j++;
        }
        else
        {
          if (this.m_pat[j] != this.m_dirSep)
            continue;
          this.m_hasDirSep = true;
          break;
        }
      }
    }
  }

  public char setSpecialChar(int paramInt, char paramChar)
  {
    int i;
    switch (paramInt)
    {
    case 92:
      i = this.m_escape;
      this.m_escape = paramChar;
      return i;
    case 63:
      i = this.m_any;
      this.m_any = paramChar;
      return i;
    case 42:
      i = this.m_closure;
      this.m_closure = paramChar;
      return i;
    case 91:
      i = this.m_setOpen;
      this.m_setOpen = paramChar;
      return i;
    case 93:
      i = this.m_setClose;
      this.m_setClose = paramChar;
      return i;
    case 45:
      i = this.m_setThru;
      this.m_setThru = paramChar;
      return i;
    case 94:
      i = this.m_setExcl;
      this.m_setExcl = paramChar;
      return i;
    case 47:
      i = this.m_dirSep;
      this.m_dirSep = paramChar;
      return i;
    case 33:
      i = this.m_negate;
      this.m_negate = paramChar;
      return i;
    }
    throw new IllegalArgumentException("Invalid special pattern character type: '" + (char)paramInt + "'");
  }

  public boolean setIgnoreCase(boolean paramBoolean)
  {
    boolean bool = this.m_ignoreCase;
    this.m_ignoreCase = paramBoolean;
    return bool;
  }

  public boolean matches(String paramString)
  {
    if (this.m_pat == null)
      throw new NullPointerException("Null pattern");
    if (paramString == null)
      throw new NullPointerException("Null filename");
    if (paramString.length() == 0)
      return false;
    return matchSubpattern(0, paramString, 0) >= 0;
  }

  public boolean accept(File paramFile, String paramString)
  {
    if (this.m_hasDirSep)
      return matches(paramFile.getPath() + DIR_SEP1 + paramString);
    return matches(paramString);
  }

  public boolean accept(File paramFile)
  {
    if (this.m_hasDirSep)
      return matches(paramFile.getPath());
    return matches(paramFile.getName());
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof FilenamePattern))
      return false;
    FilenamePattern localFilenamePattern = (FilenamePattern)paramObject;
    if (this.m_pat != null)
    {
      int i = this.m_pat.length;
      if (i != localFilenamePattern.m_pat.length)
        return false;
      for (int j = 0; j < i; j++)
        if (this.m_pat[j] != localFilenamePattern.m_pat[j])
          return false;
    }
    if (this.m_ignoreCase != localFilenamePattern.m_ignoreCase)
      return false;
    if (this.m_hasDirSep != localFilenamePattern.m_hasDirSep)
      return false;
    if (this.m_escape != localFilenamePattern.m_escape)
      return false;
    if (this.m_any != localFilenamePattern.m_any)
      return false;
    if (this.m_closure != localFilenamePattern.m_closure)
      return false;
    if (this.m_setOpen != localFilenamePattern.m_setOpen)
      return false;
    if (this.m_setClose != localFilenamePattern.m_setClose)
      return false;
    if (this.m_setThru != localFilenamePattern.m_setThru)
      return false;
    if (this.m_setExcl != localFilenamePattern.m_setExcl)
      return false;
    if (this.m_dirSep != localFilenamePattern.m_dirSep)
      return false;
    return this.m_negate == localFilenamePattern.m_negate;
  }

  public int hashCode()
  {
    int i = 0;
    if (this.m_pat != null)
      for (int j = this.m_pat.length - 1; j >= 0; j--)
        i = (i << 3 | i >>> 29) ^ this.m_pat[j];
    i = (i << 1 | i >>> 31) ^ (this.m_ignoreCase ? 1 : 0);
    i = (i << 1 | i >>> 31) ^ (this.m_hasDirSep ? 1 : 0);
    i = (i << 1 | i >>> 31) ^ this.m_escape;
    i = (i << 1 | i >>> 31) ^ this.m_any;
    i = (i << 1 | i >>> 31) ^ this.m_closure;
    i = (i << 1 | i >>> 31) ^ this.m_setOpen;
    i = (i << 1 | i >>> 31) ^ this.m_setClose;
    i = (i << 1 | i >>> 31) ^ this.m_setThru;
    i = (i << 1 | i >>> 31) ^ this.m_setExcl;
    i = (i << 1 | i >>> 31) ^ this.m_dirSep;
    i = (i << 1 | i >>> 31) ^ this.m_negate;
    return i;
  }

  public Object clone()
  {
    FilenamePattern localFilenamePattern = new FilenamePattern();
    localFilenamePattern.m_pat = this.m_pat;
    localFilenamePattern.m_ignoreCase = this.m_ignoreCase;
    localFilenamePattern.m_hasDirSep = this.m_hasDirSep;
    localFilenamePattern.m_escape = this.m_escape;
    localFilenamePattern.m_any = this.m_any;
    localFilenamePattern.m_closure = this.m_closure;
    localFilenamePattern.m_setOpen = this.m_setOpen;
    localFilenamePattern.m_setClose = this.m_setClose;
    localFilenamePattern.m_setThru = this.m_setThru;
    localFilenamePattern.m_setExcl = this.m_setExcl;
    localFilenamePattern.m_dirSep = this.m_dirSep;
    localFilenamePattern.m_negate = this.m_negate;
    return localFilenamePattern;
  }

  protected boolean isValidPattern(String paramString)
  {
    int i = paramString.length();
    if (i == 0)
      return false;
    int j = 0;
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      int n = paramString.charAt(m);
      if (n == 0)
        continue;
      if ((n == this.m_negate) && (j == 0))
      {
        if (m + 1 >= i)
          return false;
      }
      else if (n == this.m_escape)
      {
        m++;
        if (m >= i)
          return false;
        k = 0;
      }
      else if ((n == this.m_setOpen) && (j == 0))
      {
        j++;
      }
      else
      {
        if ((n == this.m_setExcl) && (j > 0))
          continue;
        if ((n == this.m_setThru) && (j > 0))
        {
          k = 1;
        }
        else
        {
          if ((n == this.m_setClose) && (k != 0))
            return false;
          if ((n == this.m_setClose) && (j > 0))
          {
            if (j < 2)
              return false;
            j = 0;
          }
          else
          {
            if (j <= 0)
              continue;
            j++;
            k = 0;
          }
        }
      }
    }
    return (j <= 0) && (k == 0);
  }

  protected int matchSubpattern(int paramInt1, String paramString, int paramInt2)
  {
    int i = this.m_pat.length;
    int j = paramString.length();
    int k = paramInt1;
    int m = paramInt2;
    while (k < i)
    {
      int i2 = -1;
      if (m < j)
        i2 = paramString.charAt(m);
      int n = this.m_pat[k];
      if (n == 0)
      {
        if (i2 != 0)
          return -m - 1;
        m++;
      }
      else
      {
        int i3;
        int i4;
        if (n == this.m_closure)
        {
          if (this.m_dirSep != 0)
            for (i3 = m; i3 < j; i3++)
            {
              i4 = paramString.charAt(i3);
              if ((i4 == DIR_SEP1) || (i4 == DIR_SEP2))
                break;
            }
          i3 = j;
          if (i3 == paramInt2)
            return -m - 1;
          while (i3 >= m)
          {
            if (matchSubpattern(k + 1, paramString, i3) >= 0)
              return i3;
            i3--;
          }
          return -m - 1;
        }
        if (n == this.m_any)
        {
          if (i2 < 0)
            return -m - 1;
          m++;
        }
        else if (n == this.m_escape)
        {
          if (k + 1 < i)
          {
            k++;
            n = this.m_pat[k];
          }
          if (n != i2)
            if (this.m_ignoreCase)
            {
              if (!compareCharIgnoreCase((char)i2, n, n))
                return -m - 1;
            }
            else
              return -m - 1;
          m++;
        }
        else
        {
          int i1;
          if (n == this.m_setOpen)
          {
            if (i2 < 0)
              return -m - 1;
            k++;
            if (k < i)
              i1 = this.m_pat[k];
            i3 = 1;
            if (i1 == this.m_setExcl)
            {
              i3 = 0;
              k++;
            }
            i4 = i3 == 0 ? 1 : 0;
            while (k < i)
            {
              i1 = this.m_pat[k];
              if (i1 == this.m_setClose)
                break;
              int i5 = i1;
              k++;
              if (i1 == this.m_escape)
              {
                if (k >= i)
                  return -m - 1;
                i1 = this.m_pat[(k++)];
                i5 = i1;
              }
              else if ((k < i) && (this.m_pat[k] == this.m_setThru))
              {
                k++;
                if (k >= i)
                  return -m - 1;
                i1 = this.m_pat[(k++)];
                if (i1 == this.m_escape)
                {
                  if (k >= i)
                    return -m - 1;
                  i1 = this.m_pat[(k++)];
                }
                else if (i1 == this.m_setClose)
                {
                  return -m - 1;
                }
              }
              if (this.m_ignoreCase)
              {
                if (compareCharIgnoreCase((char)i2, i5, i1))
                  i4 = i3;
              }
              else if ((i5 <= i2) && (i2 <= i1))
                i4 = i3;
            }
            if (i4 == 0)
              return -m - 1;
            m++;
          }
          else if (i1 == this.m_dirSep)
          {
            if (i2 < 0)
              return -m - 1;
            if ((i2 != DIR_SEP1) && (i2 != DIR_SEP2))
              return -m - 1;
            m++;
            if (((k + 1 < i) && (this.m_pat[(k + 1)] != this.m_dirSep)) || (k + 1 == i))
              while (m < j)
              {
                i2 = paramString.charAt(m);
                if ((i2 != DIR_SEP1) && (i2 != DIR_SEP2))
                  break;
                m++;
              }
            if (k + 1 < i)
            {
              if (matchSubpattern(k + 1, paramString, m) >= m)
                return m;
              return -m - 1;
            }
          }
          else
          {
            if (i1 == this.m_negate)
            {
              i3 = matchSubpattern(k + 1, paramString, m);
              if (i3 >= 0)
                return -m - 1;
              return -i3 - 1;
            }
            if (i2 < 0)
              return -m - 1;
            if (i1 != i2)
              if (this.m_ignoreCase)
              {
                if (!compareCharIgnoreCase((char)i2, i1, i1))
                  return -m - 1;
              }
              else
                return -m - 1;
            m++;
          }
        }
      }
      k++;
    }
    if (m == j)
      return m;
    return -m - 1;
  }

  static
  {
    char c = File.separatorChar;
    DIR_SEP1 = c;
    if (c == '\\')
    {
      DIR_SEP2 = '/';
      IGNORE_CASE = true;
    }
    else if (c == '/')
    {
      DIR_SEP2 = c;
      IGNORE_CASE = false;
    }
    else
    {
      DIR_SEP2 = c;
      IGNORE_CASE = true;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.scp.FilenamePattern
 * JD-Core Version:    0.6.0
 */