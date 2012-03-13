package org.apache.oro.text.regex;

import java.util.Hashtable;

public final class Perl5Compiler
  implements PatternCompiler
{
  private e f;
  private boolean e;
  private char[] i = new char[1];
  private int j;
  private int h;
  private int c;
  private char[] g;
  private static final Hashtable d = new Hashtable();
  public static final int DEFAULT_MASK = 0;
  public static final int CASE_INSENSITIVE_MASK = 1;
  public static final int MULTILINE_MASK = 8;
  public static final int SINGLELINE_MASK = 16;
  public static final int EXTENDED_MASK = 32;
  public static final int READ_ONLY_MASK = 32768;

  private char b(boolean[] paramArrayOfBoolean)
    throws MalformedPatternException
  {
    int k = this.f.c();
    int m = this.f.e();
    int n = k;
    char c1 = this.f.b(n++);
    if (c1 != ':')
      return '\000';
    if (this.f.b(n) == '^')
    {
      paramArrayOfBoolean[0] = true;
      n++;
    }
    else
    {
      paramArrayOfBoolean[0] = false;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      do
      {
        localStringBuffer.append(c1);
        if ((c1 = this.f.b(n++)) == ':')
          break;
      }
      while (n < m);
    }
    catch (Exception localException)
    {
      return '\000';
    }
    if (this.f.b(n++) != ']')
      return '\000';
    Object localObject = d.get(localStringBuffer.toString());
    if (localObject == null)
      return '\000';
    this.f.e(n);
    return ((Character)localObject).charValue();
  }

  private static boolean f(char paramChar)
  {
    return (paramChar == '*') || (paramChar == '+') || (paramChar == '?');
  }

  private int c()
    throws MalformedPatternException
  {
    int k = 0;
    int i2 = 65535;
    int[] arrayOfInt = new int[1];
    boolean[] arrayOfBoolean = new boolean[1];
    int i4;
    if (this.f.d() == '^')
    {
      i4 = e('$');
      this.f.h();
    }
    else
    {
      i4 = e('#');
    }
    int n = this.f.d();
    int m;
    if ((n == 93) || (n == 45))
      m = 1;
    else
      m = 0;
    int i1;
    while (((!this.f.b()) && ((i1 = this.f.d()) != ']')) || (m != 0))
    {
      m = 0;
      int i5 = 0;
      this.f.h();
      if ((n == 92) || (n == 91))
      {
        if (n == 92)
        {
          n = this.f.f();
        }
        else
        {
          int i6 = b(arrayOfBoolean);
          if (i6 != 0)
          {
            i5 = 1;
            n = i6;
          }
        }
        if (i5 != 1)
          switch (n)
          {
          case 119:
            i5 = 1;
            n = 18;
            i2 = 65535;
            break;
          case 87:
            i5 = 1;
            n = 19;
            i2 = 65535;
            break;
          case 115:
            i5 = 1;
            n = 22;
            i2 = 65535;
            break;
          case 83:
            i5 = 1;
            n = 23;
            i2 = 65535;
            break;
          case 100:
            i5 = 1;
            n = 24;
            i2 = 65535;
            break;
          case 68:
            i5 = 1;
            n = 25;
            i2 = 65535;
            break;
          case 110:
            n = 10;
            break;
          case 114:
            n = 13;
            break;
          case 116:
            n = 9;
            break;
          case 102:
            n = 12;
            break;
          case 98:
            n = 8;
            break;
          case 101:
            n = 27;
            break;
          case 97:
            n = 7;
            break;
          case 120:
            n = (char)c(this.f.c, this.f.c(), 2, arrayOfInt);
            this.f.c(arrayOfInt[0]);
            break;
          case 99:
            n = this.f.f();
            if (Character.isLowerCase(n))
              n = Character.toUpperCase(n);
            n = (char)(n ^ 0x40);
            break;
          case 48:
          case 49:
          case 50:
          case 51:
          case 52:
          case 53:
          case 54:
          case 55:
          case 56:
          case 57:
            n = (char)b(this.f.c, this.f.c() - 1, 3, arrayOfInt);
            this.f.c(arrayOfInt[0] - 1);
            break;
          default:
            break;
          }
      }
      if (k != 0)
      {
        if (i2 > n)
          throw new MalformedPatternException("Invalid [] range in expression.");
        k = 0;
      }
      else
      {
        i2 = n;
        if ((i5 == 0) && (this.f.d() == '-') && (this.f.c() + 1 < this.f.e()) && (this.f.d(1) != ']'))
        {
          this.f.h();
          k = 1;
          continue;
        }
      }
      if (i2 == n)
      {
        if (i5 == 1)
        {
          if (arrayOfBoolean[0] == 0)
            d('/');
          else
            d('0');
        }
        else
          d('1');
        d(n);
        if (((this.i[0] & 0x1) != 0) && (Character.isUpperCase(n)) && (Character.isUpperCase(i2)))
        {
          this.h += -1;
          d(Character.toLowerCase(n));
        }
      }
      if (i2 < n)
      {
        d('%');
        d(i2);
        d(n);
        if (((this.i[0] & 0x1) != 0) && (Character.isUpperCase(n)) && (Character.isUpperCase(i2)))
        {
          this.h -= 2;
          d(Character.toLowerCase(i2));
          d(Character.toLowerCase(n));
        }
        i3 = 65535;
        k = 0;
      }
      int i3 = n;
    }
    if (this.f.d() != ']')
      throw new MalformedPatternException("Unmatched [] in expression.");
    b();
    d('\000');
    return i4;
  }

  private int d(int[] paramArrayOfInt)
    throws MalformedPatternException
  {
    int k = 0;
    int m = 0;
    int[] arrayOfInt = new int[1];
    int i2 = 0;
    int i3 = 65535;
    int n = c(arrayOfInt);
    if (n == -1)
    {
      if ((arrayOfInt[0] & 0x8) != 0)
        paramArrayOfInt[0] |= 8;
      return -1;
    }
    int i4 = this.f.d();
    if ((i4 == 40) && (this.f.d(1) == '?') && (this.f.d(2) == '#'))
    {
      while ((i4 != 65535) && (i4 != 41))
        i4 = this.f.h();
      if (i4 != 65535)
      {
        b();
        i4 = this.f.d();
      }
    }
    if ((i4 == 123) && (b(this.f.c, this.f.c())))
    {
      int i1 = this.f.c() + 1;
      int i5;
      int i6 = i5 = this.f.e();
      for (char c1 = this.f.b(i1); (Character.isDigit(c1)) || (c1 == ','); c1 = this.f.b(i1))
      {
        if (c1 == ',')
        {
          if (i6 != i5)
            break;
          i6 = i1;
        }
        i1++;
      }
      if (c1 == '}')
      {
        StringBuffer localStringBuffer = new StringBuffer(10);
        if (i6 == i5)
          i6 = i1;
        this.f.h();
        int i7 = this.f.c();
        for (c1 = this.f.b(i7); Character.isDigit(c1); c1 = this.f.b(i7))
        {
          localStringBuffer.append(c1);
          i7++;
        }
        try
        {
          i2 = Integer.parseInt(localStringBuffer.toString());
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          throw new MalformedPatternException("Unexpected number format exception.  Please report this bug.NumberFormatException message: " + localNumberFormatException1.getMessage());
        }
        c1 = this.f.b(i6);
        if (c1 == ',')
          i6++;
        else
          i6 = this.f.c();
        i7 = i6;
        localStringBuffer = new StringBuffer(10);
        for (c1 = this.f.b(i7); Character.isDigit(c1); c1 = this.f.b(i7))
        {
          localStringBuffer.append(c1);
          i7++;
        }
        try
        {
          if (i7 != i6)
            i3 = Integer.parseInt(localStringBuffer.toString());
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          throw new MalformedPatternException("Unexpected number format exception.  Please report this bug.NumberFormatException message: " + localNumberFormatException2.getMessage());
        }
        if ((i3 == 0) && (this.f.b(i6) != '0'))
          i3 = 65535;
        this.f.e(i1);
        b();
        k = 1;
        m = 1;
      }
    }
    if (k == 0)
    {
      m = 0;
      if (!f(i4))
      {
        paramArrayOfInt[0] = arrayOfInt[0];
        return n;
      }
      b();
      paramArrayOfInt[0] = (i4 != 43 ? 4 : 1);
      if ((i4 == 42) && ((arrayOfInt[0] & 0x2) != 0))
      {
        b('\020', n);
        this.c += 4;
      }
      else if (i4 == 42)
      {
        i2 = 0;
        m = 1;
      }
      else if ((i4 == 43) && ((arrayOfInt[0] & 0x2) != 0))
      {
        b('\021', n);
        this.c += 3;
      }
      else if (i4 == 43)
      {
        i2 = 1;
        m = 1;
      }
      else if (i4 == 63)
      {
        i2 = 0;
        i3 = 1;
        m = 1;
      }
    }
    if (m != 0)
    {
      if ((arrayOfInt[0] & 0x2) != 0)
      {
        this.c += (2 + this.c) / 2;
        b('\n', n);
      }
      else
      {
        this.c += 4 + this.c;
        c(n, e('"'));
        b('\013', n);
        c(n, e('\017'));
      }
      if (i2 > 0)
        paramArrayOfInt[0] = 1;
      if ((i3 != 0) && (i3 < i2))
        throw new MalformedPatternException("Invalid interval {" + i2 + "," + i3 + "}");
      if (this.g != null)
      {
        this.g[(n + 2)] = (char)i2;
        this.g[(n + 3)] = (char)i3;
      }
    }
    if (this.f.d() == '?')
    {
      b();
      b('\035', n);
      c(n, n + 2);
    }
    if (c(this.f.c, this.f.c()))
      throw new MalformedPatternException("Nested repetitions *?+ in expression");
    return n;
  }

  private void d(char paramChar)
  {
    if (this.g != null)
      this.g[this.h] = paramChar;
    this.h += 1;
  }

  private int b(char paramChar1, char paramChar2)
  {
    int k = this.h;
    if (this.g == null)
    {
      this.h += 3;
    }
    else
    {
      this.g[(this.h++)] = paramChar1;
      this.g[(this.h++)] = '\000';
      this.g[(this.h++)] = paramChar2;
    }
    return k;
  }

  private static int c(char[] paramArrayOfChar, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    int k = 0;
    paramArrayOfInt[0] = 0;
    int m;
    while ((paramInt1 < paramArrayOfChar.length) && (paramInt2-- > 0) && ((m = "0123456789abcdef0123456789ABCDEFx".indexOf(paramArrayOfChar[paramInt1])) != -1))
    {
      k <<= 4;
      k |= m & 0xF;
      paramInt1++;
      paramArrayOfInt[0] += 1;
    }
    return k;
  }

  private static void b(char[] paramArrayOfChar, char paramChar)
  {
    switch (paramChar)
    {
    case 'i':
      int tmp62_61 = 0;
      paramArrayOfChar[tmp62_61] = (char)(paramArrayOfChar[tmp62_61] | 0x1);
      return;
    case 'g':
      int tmp71_70 = 0;
      paramArrayOfChar[tmp71_70] = (char)(paramArrayOfChar[tmp71_70] | 0x2);
      return;
    case 'o':
      int tmp80_79 = 0;
      paramArrayOfChar[tmp80_79] = (char)(paramArrayOfChar[tmp80_79] | 0x4);
      return;
    case 'm':
      int tmp89_88 = 0;
      paramArrayOfChar[tmp89_88] = (char)(paramArrayOfChar[tmp89_88] | 0x8);
      return;
    case 's':
      int tmp99_98 = 0;
      paramArrayOfChar[tmp99_98] = (char)(paramArrayOfChar[tmp99_98] | 0x10);
      return;
    case 'x':
      int tmp109_108 = 0;
      paramArrayOfChar[tmp109_108] = (char)(paramArrayOfChar[tmp109_108] | 0x20);
      return;
    }
  }

  public Pattern compile(String paramString)
    throws MalformedPatternException
  {
    return compile(paramString.toCharArray(), 0);
  }

  public Pattern compile(String paramString, int paramInt)
    throws MalformedPatternException
  {
    return compile(paramString.toCharArray(), paramInt);
  }

  public Pattern compile(char[] paramArrayOfChar)
    throws MalformedPatternException
  {
    return compile(paramArrayOfChar, 0);
  }

  public Pattern compile(char[] paramArrayOfChar, int paramInt)
    throws MalformedPatternException
  {
    int[] arrayOfInt = new int[1];
    int i1 = 0;
    int i2 = 0;
    int i4 = 0;
    this.f = new e(paramArrayOfChar);
    int k = paramInt & 0x1;
    this.i[0] = (char)paramInt;
    this.e = false;
    this.j = 1;
    this.h = 0;
    this.c = 0;
    this.g = null;
    d('\000');
    if (b(false, arrayOfInt) == -1)
      throw new MalformedPatternException("Unknown compilation error.");
    if (this.h >= 65534)
      throw new MalformedPatternException("Expression is too large.");
    this.g = new char[this.h];
    Perl5Pattern localPerl5Pattern = new Perl5Pattern();
    localPerl5Pattern.d = this.g;
    localPerl5Pattern.i = new String(paramArrayOfChar);
    this.f.e(0);
    this.j = 1;
    this.h = 0;
    this.c = 0;
    d('\000');
    if (b(false, arrayOfInt) == -1)
      throw new MalformedPatternException("Unknown compilation error.");
    k = this.i[0] & 0x1;
    localPerl5Pattern.k = (this.c >= 10);
    localPerl5Pattern.l = -1;
    localPerl5Pattern.n = 0;
    localPerl5Pattern.c = -1;
    localPerl5Pattern.m = paramInt;
    localPerl5Pattern.j = null;
    localPerl5Pattern.g = null;
    String str1 = null;
    String str2 = null;
    int m = 1;
    if (this.g[c.c(this.g, m)] == 0)
    {
      int n = m = c.c(m);
      for (char c1 = this.g[n]; ((c1 == '\033') && ((i1 = 1) != 0)) || ((c1 == '\f') && (this.g[c.c(this.g, n)] != '\f')) || (c1 == '\021') || (c1 == '\035') || ((c.d[c1] == '\n') && (c.e(this.g, n) > 0)); c1 = this.g[n])
      {
        if (c1 == '\021')
          i2 = 1;
        else
          n += c.b[c1];
        n = c.c(n);
      }
      int i8 = 1;
      while (i8 != 0)
      {
        i8 = 0;
        c1 = this.g[n];
        if (c1 == '\016')
        {
          str2 = new String(this.g, c.d(n + 1), this.g[c.d(n)]);
        }
        else if (c.b(c1, c.c, 2))
        {
          localPerl5Pattern.l = n;
        }
        else if ((c1 == '\024') || (c1 == '\025'))
        {
          localPerl5Pattern.l = n;
        }
        else if (c.d[c1] == '\001')
        {
          if (c1 == '\001')
            localPerl5Pattern.n = 1;
          else if (c1 == '\002')
            localPerl5Pattern.n = 2;
          else
            localPerl5Pattern.n = 3;
          n = c.c(n);
          i8 = 1;
        }
        else
        {
          if ((c1 != '\020') || (c.d[this.g[c.c(n)]] != '\007') || ((localPerl5Pattern.n & 0x3) == 0))
            continue;
          localPerl5Pattern.n = 11;
          n = c.c(n);
          i8 = 1;
        }
      }
      if ((i2 != 0) && ((i1 == 0) || (!this.e)))
        localPerl5Pattern.n |= 4;
      StringBuffer localStringBuffer1 = new StringBuffer();
      StringBuffer localStringBuffer2 = new StringBuffer();
      int i3 = 0;
      i4 = 0;
      int i5 = 0;
      int i6 = 0;
      int i7 = 0;
      while ((m > 0) && ((c1 = this.g[m]) != 0))
        if (c1 == '\f')
        {
          if (this.g[c.c(this.g, m)] == '\f')
          {
            i5 = -30000;
            while (this.g[m] == '\f')
              m = c.c(this.g, m);
          }
          else
          {
            m = c.c(m);
          }
        }
        else if (c1 == ' ')
        {
          i5 = -30000;
          m = c.c(this.g, m);
        }
        else
        {
          if (c1 == '\016')
          {
            n = m;
            while (this.g[(i9 = c.c(this.g, m))] == '\034')
              m = i9;
            i4 += this.g[c.d(n)];
            int i9 = this.g[c.d(n)];
            if (i5 - i6 == i3)
            {
              localStringBuffer1.append(new String(this.g, c.d(n) + 1, i9));
              i3 += i9;
              i5 += i9;
              n = c.c(this.g, m);
            }
            else if (i9 >= i3 + (i5 >= 0 ? 1 : 0))
            {
              i3 = i9;
              localStringBuffer1 = new StringBuffer(new String(this.g, c.d(n) + 1, i9));
              i6 = i5;
              i5 += i3;
              n = c.c(this.g, m);
            }
            else
            {
              i5 += i9;
            }
          }
          else if (c.b(c1, c.e, 0))
          {
            i5 = -30000;
            i3 = 0;
            if (localStringBuffer1.length() > localStringBuffer2.length())
            {
              localStringBuffer2 = localStringBuffer1;
              i7 = i6;
            }
            localStringBuffer1 = new StringBuffer();
            if ((c1 == '\021') && (c.b(this.g[c.c(m)], c.c, 0)))
              i4++;
            else if ((c.d[c1] == '\n') && (c.b(this.g[(c.c(m) + 2)], c.c, 0)))
              i4 += c.e(this.g, m);
          }
          else if (c.b(c1, c.c, 0))
          {
            i5++;
            i4++;
            i3 = 0;
            if (localStringBuffer1.length() > localStringBuffer2.length())
            {
              localStringBuffer2 = localStringBuffer1;
              i7 = i6;
            }
            localStringBuffer1 = new StringBuffer();
          }
          m = c.c(this.g, m);
        }
      if (localStringBuffer1.length() + (c.d[this.g[n]] == '\004' ? 1 : 0) > localStringBuffer2.length())
      {
        localStringBuffer2 = localStringBuffer1;
        i7 = i6;
      }
      else
      {
        localStringBuffer1 = new StringBuffer();
      }
      if ((localStringBuffer2.length() > 0) && (str2 == null))
      {
        str1 = localStringBuffer2.toString();
        if (i7 < 0)
          i7 = -1;
        localPerl5Pattern.c = i7;
      }
      else
      {
        localStringBuffer2 = null;
      }
    }
    localPerl5Pattern.h = ((k & 0x1) != 0);
    localPerl5Pattern.e = (this.j - 1);
    localPerl5Pattern.f = i4;
    if (str1 != null)
    {
      localPerl5Pattern.g = str1.toCharArray();
      localPerl5Pattern.b = 100;
    }
    if (str2 != null)
      localPerl5Pattern.j = str2.toCharArray();
    return localPerl5Pattern;
  }

  private static boolean c(char[] paramArrayOfChar, int paramInt)
  {
    if ((paramInt < paramArrayOfChar.length) && (paramInt >= 0))
      return (paramArrayOfChar[paramInt] == '*') || (paramArrayOfChar[paramInt] == '+') || (paramArrayOfChar[paramInt] == '?') || ((paramArrayOfChar[paramInt] == '{') && (b(paramArrayOfChar, paramInt)));
    return false;
  }

  private int b(boolean paramBoolean, int[] paramArrayOfInt)
    throws MalformedPatternException
  {
    char[] arrayOfChar2 = new char[1];
    char[] arrayOfChar3 = new char[1];
    int i1 = -1;
    int i2 = 0;
    int[] arrayOfInt = new int[1];
    String str = "iogmsx-";
    char[] arrayOfChar1 = arrayOfChar2;
    paramArrayOfInt[0] = 1;
    int n;
    if (paramBoolean)
    {
      n = 1;
      if (this.f.d() == '?')
      {
        this.f.h();
        int k;
        n = k = this.f.f();
        switch (k)
        {
        case 33:
        case 58:
        case 61:
          break;
        case 35:
          for (k = this.f.d(); (k != 65535) && (k != 41); k = this.f.h());
          if (k != 41)
            throw new MalformedPatternException("Sequence (?#... not terminated");
          b();
          paramArrayOfInt[0] = 8;
          return -1;
        default:
          this.f.g();
          int m;
          for (k = this.f.d(); (m != 65535) && (str.indexOf(m) != -1); m = this.f.h())
            if (k == 45)
              arrayOfChar1 = arrayOfChar3;
            else
              b(arrayOfChar1, k);
          int tmp246_245 = 0;
          char[] tmp246_242 = this.i;
          tmp246_242[tmp246_245] = (char)(tmp246_242[tmp246_245] | arrayOfChar2[0]);
          int tmp260_259 = 0;
          char[] tmp260_256 = this.i;
          tmp260_256[tmp260_259] = (char)(tmp260_256[tmp260_259] & (arrayOfChar3[0] ^ 0xFFFFFFFF));
          if (m != 41)
            throw new MalformedPatternException("Sequence (?" + m + "...) not recognized");
          b();
          paramArrayOfInt[0] = 8;
          return -1;
        }
      }
      else
      {
        i2 = this.j;
        this.j += 1;
        i1 = b('\033', (char)i2);
      }
    }
    else
    {
      n = 0;
    }
    int i3 = b(arrayOfInt);
    if (i3 == -1)
      return -1;
    if (i1 != -1)
      c(i1, i3);
    else
      i1 = i3;
    if ((arrayOfInt[0] & 0x1) == 0)
      paramArrayOfInt[0] &= -2;
    paramArrayOfInt[0] |= arrayOfInt[0] & 0x4;
    while (this.f.d() == '|')
    {
      b();
      i3 = b(arrayOfInt);
      if (i3 == -1)
        return -1;
      c(i1, i3);
      if ((arrayOfInt[0] & 0x1) == 0)
        paramArrayOfInt[0] &= -2;
      paramArrayOfInt[0] |= arrayOfInt[0] & 0x4;
    }
    int i4;
    switch (n)
    {
    case 58:
      i4 = e('\017');
      break;
    case 1:
      i4 = b('\034', (char)i2);
      break;
    case 33:
    case 61:
      i4 = e('!');
      paramArrayOfInt[0] &= -2;
      break;
    case 0:
    default:
      i4 = e('\000');
      break;
    }
    c(i1, i4);
    for (i3 = i1; i3 != -1; i3 = c.c(this.g, i3))
      b(i3, i4);
    if (n == 61)
    {
      b('\037', i1);
      c(i1, e('\017'));
    }
    else if (n == 33)
    {
      b(' ', i1);
      c(i1, e('\017'));
    }
    if ((n != 0) && ((this.f.b()) || (b() != ')')))
      throw new MalformedPatternException("Unmatched parentheses.");
    if ((n == 0) && (!this.f.b()))
    {
      if (this.f.d() == ')')
        throw new MalformedPatternException("Unmatched parentheses.");
      throw new MalformedPatternException("Unreached characters at end of expression.  Please report this bug!");
    }
    return i1;
  }

  private void c(int paramInt1, int paramInt2)
  {
    if ((this.g == null) || (paramInt1 == -1))
      return;
    int m;
    for (int k = paramInt1; ; k = m)
    {
      m = c.c(this.g, k);
      if (m == -1)
        break;
    }
    int n;
    if (this.g[k] == '\r')
      n = k - paramInt2;
    else
      n = paramInt2 - k;
    this.g[(k + 1)] = (char)n;
  }

  static
  {
    d.put("alnum", new Character('2'));
    d.put("word", new Character('\022'));
    d.put("alpha", new Character('&'));
    d.put("blank", new Character('\''));
    d.put("cntrl", new Character('('));
    d.put("digit", new Character('\030'));
    d.put("graph", new Character(')'));
    d.put("lower", new Character('*'));
    d.put("print", new Character('+'));
    d.put("punct", new Character(','));
    d.put("space", new Character('\026'));
    d.put("upper", new Character('-'));
    d.put("xdigit", new Character('.'));
    d.put("ascii", new Character('3'));
  }

  private int e(char paramChar)
  {
    int k = this.h;
    if (this.g == null)
    {
      this.h += 2;
    }
    else
    {
      this.g[(this.h++)] = paramChar;
      this.g[(this.h++)] = '\000';
    }
    return k;
  }

  private int b(int[] paramArrayOfInt)
    throws MalformedPatternException
  {
    int i1 = 0;
    paramArrayOfInt[0] = 0;
    int m = e('\f');
    int k = -1;
    if (this.f.c() == 0)
    {
      this.f.e(-1);
      b();
    }
    else
    {
      this.f.g();
      b();
    }
    int i2 = this.f.d();
    while ((i2 != 65535) && (i2 != 124) && (i2 != 41))
    {
      i1 &= -9;
      int n = d(paramArrayOfInt);
      if (n == -1)
      {
        if ((i1 & 0x8) != 0)
          i2 = this.f.d();
        else
          return -1;
      }
      else
      {
        paramArrayOfInt[0] |= i1 & 0x1;
        if (k == -1)
        {
          paramArrayOfInt[0] |= i1 & 0x4;
        }
        else
        {
          this.c += 1;
          c(k, n);
        }
        k = n;
        i2 = this.f.d();
      }
    }
    if (k == -1)
      e('\017');
    return m;
  }

  public static final String quotemeta(char[] paramArrayOfChar)
  {
    StringBuffer localStringBuffer = new StringBuffer(2 * paramArrayOfChar.length);
    for (int k = 0; k < paramArrayOfChar.length; k++)
    {
      if (!c.b(paramArrayOfChar[k]))
        localStringBuffer.append('\\');
      localStringBuffer.append(paramArrayOfChar[k]);
    }
    return localStringBuffer.toString();
  }

  public static final String quotemeta(String paramString)
  {
    return quotemeta(paramString.toCharArray());
  }

  private char b()
  {
    int k = this.f.f();
    label154: 
    while (true)
    {
      int m = this.f.d();
      if ((m == 40) && (this.f.d(1) == '?') && (this.f.d(2) == '#'))
      {
        while ((m != 65535) && (m != 41))
          m = this.f.h();
        this.f.h();
      }
      else
      {
        if ((this.i[0] & 0x20) != 0)
        {
          if (Character.isWhitespace(m))
          {
            this.f.h();
            break label154;
          }
          if (m == 35)
          {
            int n;
            while ((n != 65535) && (n != 10))
              n = this.f.h();
            this.f.h();
            break label154;
          }
        }
        return k;
      }
    }
  }

  private int c(int[] paramArrayOfInt)
    throws MalformedPatternException
  {
    int[] arrayOfInt1 = new int[1];
    paramArrayOfInt[0] = 0;
    int k = 0;
    int m = -1;
    char c1;
    int n;
    label1297: 
    while (true)
    {
      c1 = this.f.d();
      switch (c1)
      {
      case '^':
        b();
        if ((this.i[0] & 0x8) != 0)
          m = e('\002');
        else if ((this.i[0] & 0x10) != 0)
          m = e('\003');
        else
          m = e('\001');
        break;
      case '$':
        b();
        if ((this.i[0] & 0x8) != 0)
          m = e('\005');
        else if ((this.i[0] & 0x10) != 0)
          m = e('\006');
        else
          m = e('\004');
        break;
      case '.':
        b();
        if ((this.i[0] & 0x10) != 0)
          m = e('\b');
        else
          m = e('\007');
        this.c += 1;
        paramArrayOfInt[0] |= 3;
        break;
      case '[':
        this.f.h();
        m = c();
        paramArrayOfInt[0] |= 3;
        break;
      case '(':
        b();
        m = b(true, arrayOfInt1);
        if (m == -1)
        {
          if ((arrayOfInt1[0] & 0x8) != 0)
            break label1297;
          return -1;
        }
        paramArrayOfInt[0] |= arrayOfInt1[0] & 0x5;
        break;
      case ')':
      case '|':
        if ((arrayOfInt1[0] & 0x8) != 0)
        {
          paramArrayOfInt[0] |= 8;
          return -1;
        }
        throw new MalformedPatternException("Error in expression at " + this.f.f(this.f.c()));
      case '*':
      case '+':
      case '?':
        throw new MalformedPatternException("?+* follows nothing in expression");
      case '\\':
        c1 = this.f.h();
        switch (c1)
        {
        case 'A':
          m = e('\003');
          paramArrayOfInt[0] |= 2;
          b();
          break;
        case 'G':
          m = e('\036');
          paramArrayOfInt[0] |= 2;
          b();
          break;
        case 'Z':
          m = e('\006');
          paramArrayOfInt[0] |= 2;
          b();
          break;
        case 'w':
          m = e('\022');
          paramArrayOfInt[0] |= 3;
          b();
          break;
        case 'W':
          m = e('\023');
          paramArrayOfInt[0] |= 3;
          b();
          break;
        case 'b':
          m = e('\024');
          paramArrayOfInt[0] |= 2;
          b();
          break;
        case 'B':
          m = e('\025');
          paramArrayOfInt[0] |= 2;
          b();
          break;
        case 's':
          m = e('\026');
          paramArrayOfInt[0] |= 3;
          b();
          break;
        case 'S':
          m = e('\027');
          paramArrayOfInt[0] |= 3;
          b();
          break;
        case 'd':
          m = e('\030');
          paramArrayOfInt[0] |= 3;
          b();
          break;
        case 'D':
          m = e('\031');
          paramArrayOfInt[0] |= 3;
          b();
          break;
        case '0':
        case 'a':
        case 'c':
        case 'e':
        case 'f':
        case 'n':
        case 'r':
        case 't':
        case 'x':
          k = 1;
          break;
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          StringBuffer localStringBuffer1 = new StringBuffer(10);
          n = 0;
          for (c1 = this.f.d(n); Character.isDigit(c1); c1 = this.f.d(n))
          {
            localStringBuffer1.append(c1);
            n++;
          }
          try
          {
            n = Integer.parseInt(localStringBuffer1.toString());
          }
          catch (NumberFormatException localNumberFormatException1)
          {
            throw new MalformedPatternException("Unexpected number format exception.  Please report this bug.NumberFormatException message: " + localNumberFormatException1.getMessage());
          }
          if ((n > 9) && (n >= this.j))
          {
            k = 1;
            break label1300;
          }
          if (n >= this.j)
            throw new MalformedPatternException("Invalid backreference: \\" + n);
          this.e = true;
          m = b('\032', (char)n);
          paramArrayOfInt[0] |= 1;
          for (c1 = this.f.d(); Character.isDigit(c1); c1 = this.f.h());
          this.f.g();
          b();
          break;
        case '\000':
        case '￿':
          if (!this.f.b())
            break;
          throw new MalformedPatternException("Trailing \\ in expression.");
        }
        k = 1;
        break label1300;
        break;
      case '#':
        if ((this.i[0] & 0x20) == 0)
          break;
        while ((!this.f.b()) && (this.f.d() != '\n'))
          this.f.h();
        if (!this.f.b())
          break label1297;
      }
      this.f.h();
      k = 1;
      break;
    }
    label1300: if (k != 0)
    {
      m = e('\016');
      d(65535);
      int i1 = 0;
      int i2 = this.f.c() - 1;
      int i3 = this.f.e();
      while ((i1 < 127) && (i2 < i3))
      {
        int i4 = i2;
        c1 = this.f.b(i2);
        switch (c1)
        {
        case '$':
        case '(':
        case ')':
        case '.':
        case '[':
        case '^':
        case '|':
          break;
        case '\\':
          i2++;
          c1 = this.f.b(i2);
          int[] arrayOfInt2;
          switch (c1)
          {
          case 'A':
          case 'B':
          case 'D':
          case 'G':
          case 'S':
          case 'W':
          case 'Z':
          case 'b':
          case 'd':
          case 's':
          case 'w':
            i2--;
            break;
          case 'n':
            n = 10;
            i2++;
            break;
          case 'r':
            n = 13;
            i2++;
            break;
          case 't':
            n = 9;
            i2++;
            break;
          case 'f':
            n = 12;
            i2++;
            break;
          case 'e':
            n = 27;
            i2++;
            break;
          case 'a':
            n = 7;
            i2++;
            break;
          case 'x':
            arrayOfInt2 = new int[1];
            i2++;
            n = (char)c(this.f.c, i2, 2, arrayOfInt2);
            i2 += arrayOfInt2[0];
            break;
          case 'c':
            i2++;
            n = this.f.b(i2++);
            if (Character.isLowerCase(n))
              c2 = Character.toUpperCase(n);
            c2 = (char)(c2 ^ 0x40);
            break;
          case '0':
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
          case '8':
          case '9':
            int i5 = 0;
            c1 = this.f.b(i2);
            if (c1 == '0')
              i5 = 1;
            c1 = this.f.b(i2 + 1);
            if (Character.isDigit(c1))
            {
              StringBuffer localStringBuffer2 = new StringBuffer(10);
              int i6 = i2;
              for (c1 = this.f.b(i6); Character.isDigit(c1); c1 = this.f.b(i6))
              {
                localStringBuffer2.append(c1);
                i6++;
              }
              try
              {
                i6 = Integer.parseInt(localStringBuffer2.toString());
              }
              catch (NumberFormatException localNumberFormatException2)
              {
                throw new MalformedPatternException("Unexpected number format exception.  Please report this bug.NumberFormatException message: " + localNumberFormatException2.getMessage());
              }
              if (i5 == 0)
                i5 = i6 < this.j ? 0 : 1;
            }
            if (i5 != 0)
            {
              arrayOfInt2 = new int[1];
              c2 = (char)b(this.f.c, i2, 3, arrayOfInt2);
              i2 += arrayOfInt2[0];
            }
            else
            {
              i2--;
              break label2333;
            }
            break;
          case '\000':
          case '￿':
            if (i2 < i3)
              break;
            throw new MalformedPatternException("Trailing \\ in expression.");
          }
          c2 = this.f.b(i2++);
          break;
        case '#':
          if ((this.i[0] & 0x20) == 0)
            break;
          while ((i2 < i3) && (this.f.b(i2) != '\n'))
            i2++;
        case '\t':
        case '\n':
        case '\013':
        case '\f':
        case '\r':
        case ' ':
          if ((this.i[0] & 0x20) != 0)
          {
            i2++;
            i1--;
          }
          break;
        }
        char c2 = this.f.b(i2++);
        if (((this.i[0] & 0x1) != 0) && (Character.isUpperCase(c2)))
          c2 = Character.toLowerCase(c2);
        if ((i2 < i3) && (c(this.f.c, i2)))
        {
          if (i1 > 0)
          {
            i2 = i4;
          }
          else
          {
            i1++;
            d(c2);
          }
          break;
        }
        d(c2);
        i1++;
      }
      label2333: this.f.e(i2 - 1);
      b();
      if (i1 < 0)
        throw new MalformedPatternException("Unexpected compilation failure.  Please report this bug!");
      if (i1 > 0)
        paramArrayOfInt[0] |= 1;
      if (i1 == 1)
        paramArrayOfInt[0] |= 2;
      if (this.g != null)
        this.g[c.d(m)] = (char)i1;
      d(65535);
    }
    return m;
  }

  private static int b(char[] paramArrayOfChar, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    int k = 0;
    paramArrayOfInt[0] = 0;
    while ((paramInt1 < paramArrayOfChar.length) && (paramInt2 > 0) && (paramArrayOfChar[paramInt1] >= '0') && (paramArrayOfChar[paramInt1] <= '7'))
    {
      k <<= 3;
      k |= paramArrayOfChar[paramInt1] - '0';
      paramInt2--;
      paramInt1++;
      paramArrayOfInt[0] += 1;
    }
    return k;
  }

  private void b(char paramChar, int paramInt)
  {
    int n = c.d[paramChar] == '\n' ? 2 : 0;
    if (this.g == null)
    {
      this.h += 2 + n;
      return;
    }
    int k = this.h;
    this.h += 2 + n;
    int m = this.h;
    while (k > paramInt)
    {
      k--;
      m--;
      this.g[m] = this.g[k];
    }
    this.g[(paramInt++)] = paramChar;
    this.g[(paramInt++)] = '\000';
    while (n-- > 0)
      this.g[(paramInt++)] = '\000';
  }

  private void b(int paramInt1, int paramInt2)
  {
    if ((this.g == null) || (paramInt1 == -1) || (c.d[this.g[paramInt1]] != '\f'))
      return;
    c(c.c(paramInt1), paramInt2);
  }

  private static boolean b(char[] paramArrayOfChar, int paramInt)
  {
    if (paramArrayOfChar[paramInt] != '{')
      return false;
    paramInt++;
    if ((paramInt >= paramArrayOfChar.length) || (!Character.isDigit(paramArrayOfChar[paramInt])))
      return false;
    while ((paramInt < paramArrayOfChar.length) && (Character.isDigit(paramArrayOfChar[paramInt])))
      paramInt++;
    if ((paramInt < paramArrayOfChar.length) && (paramArrayOfChar[paramInt] == ','))
      paramInt++;
    while ((paramInt < paramArrayOfChar.length) && (Character.isDigit(paramArrayOfChar[paramInt])))
      paramInt++;
    return (paramInt < paramArrayOfChar.length) && (paramArrayOfChar[paramInt] == '}');
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.Perl5Compiler
 * JD-Core Version:    0.6.0
 */