package org.apache.oro.text.regex;

import java.util.Stack;
import java.util.Vector;

public final class Perl5Matcher
  implements PatternMatcher
{
  private boolean d = false;
  private boolean q = false;
  private boolean v = false;
  private char r;
  private char[] m;
  private char[] t;
  private d k;
  private int f;
  private int n;
  private int e;
  private int c;
  private int u;
  private char[] b;
  private int p;
  private int l;
  private int s;
  private int[] h;
  private int[] o;
  private Stack i = new Stack();
  private b j = null;
  private int g = -100;

  char[] b(char[] paramArrayOfChar)
  {
    char[] arrayOfChar = new char[paramArrayOfChar.length];
    System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
    paramArrayOfChar = arrayOfChar;
    for (int i1 = 0; i1 < paramArrayOfChar.length; i1++)
    {
      if (!Character.isUpperCase(paramArrayOfChar[i1]))
        continue;
      paramArrayOfChar[i1] = Character.toLowerCase(paramArrayOfChar[i1]);
    }
    return paramArrayOfChar;
  }

  public boolean matches(String paramString, Pattern paramPattern)
  {
    return matches(paramString.toCharArray(), paramPattern);
  }

  public boolean matches(char[] paramArrayOfChar, Pattern paramPattern)
  {
    Perl5Pattern localPerl5Pattern = (Perl5Pattern)paramPattern;
    this.t = paramArrayOfChar;
    if (localPerl5Pattern.h)
      paramArrayOfChar = b(paramArrayOfChar);
    c(localPerl5Pattern, paramArrayOfChar, 0, paramArrayOfChar.length, 0);
    this.q = ((b(0)) && (this.o[0] == paramArrayOfChar.length));
    this.j = null;
    return this.q;
  }

  public boolean matches(PatternMatcherInput paramPatternMatcherInput, Pattern paramPattern)
  {
    Perl5Pattern localPerl5Pattern = (Perl5Pattern)paramPattern;
    this.t = paramPatternMatcherInput.i;
    char[] arrayOfChar;
    if (localPerl5Pattern.h)
    {
      if (paramPatternMatcherInput.f == null)
        paramPatternMatcherInput.f = b(this.t);
      arrayOfChar = paramPatternMatcherInput.f;
    }
    else
    {
      arrayOfChar = this.t;
    }
    c(localPerl5Pattern, arrayOfChar, paramPatternMatcherInput.h, paramPatternMatcherInput.g, paramPatternMatcherInput.h);
    this.j = null;
    if ((b(paramPatternMatcherInput.h)) && ((this.o[0] == paramPatternMatcherInput.g) || (paramPatternMatcherInput.length() == 0) || (paramPatternMatcherInput.h == paramPatternMatcherInput.g)))
    {
      this.q = true;
      return true;
    }
    this.q = false;
    return false;
  }

  private void d(int paramInt)
  {
    int i1 = 3 * (this.p - paramInt);
    int[] arrayOfInt;
    if (i1 <= 0)
      arrayOfInt = new int[3];
    else
      arrayOfInt = new int[i1 + 3];
    arrayOfInt[0] = this.p;
    arrayOfInt[1] = this.s;
    arrayOfInt[2] = this.l;
    int i2 = this.p;
    while (i2 > paramInt)
    {
      arrayOfInt[i1] = this.o[i2];
      arrayOfInt[(i1 + 1)] = this.h[i2];
      arrayOfInt[(i1 + 2)] = i2;
      i2--;
      i1 -= 3;
    }
    this.i.push(arrayOfInt);
  }

  private void b()
  {
    int[] arrayOfInt = (int[])this.i.pop();
    this.p = arrayOfInt[0];
    this.s = arrayOfInt[1];
    this.l = arrayOfInt[2];
    for (int i1 = 3; i1 < arrayOfInt.length; i1 += 3)
    {
      i2 = arrayOfInt[(i1 + 2)];
      this.h[i2] = arrayOfInt[(i1 + 1)];
      if (i2 > this.s)
        continue;
      this.o[i2] = arrayOfInt[i1];
    }
    for (int i2 = this.s + 1; i2 <= this.f; i2++)
    {
      if (i2 > this.p)
        this.h[i2] = -1;
      this.o[i2] = -1;
    }
  }

  private boolean b(Perl5Pattern paramPerl5Pattern, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
  {
    int i1 = 0;
    int i2 = 0;
    c(paramPerl5Pattern, paramArrayOfChar, paramInt1, paramInt2, paramInt3);
    boolean bool1 = false;
    char[] arrayOfChar = paramPerl5Pattern.g;
    while (true)
    {
      if ((arrayOfChar != null) && (((paramPerl5Pattern.n & 0x3) == 0) || (((this.d) || ((paramPerl5Pattern.n & 0x2) != 0)) && (paramPerl5Pattern.c >= 0))))
      {
        this.c = b(this.m, this.c, paramInt2, arrayOfChar);
        if (this.c >= paramInt2)
        {
          if ((paramPerl5Pattern.m & 0x8000) == 0)
            paramPerl5Pattern.b += 1;
          bool1 = false;
          break;
        }
        if (paramPerl5Pattern.c >= 0)
        {
          this.c -= paramPerl5Pattern.c;
          if (this.c < paramInt3)
            this.c = paramInt3;
          i1 = paramPerl5Pattern.c + arrayOfChar.length;
        }
        else
        {
          if ((!paramPerl5Pattern.k) && ((paramPerl5Pattern.m & 0x8000) == 0))
            if (paramPerl5Pattern.b += -1 < 0)
            {
              arrayOfChar = paramPerl5Pattern.g = null;
              this.c = paramInt3;
              break label224;
            }
          this.c = paramInt3;
          i1 = arrayOfChar.length;
        }
      }
      label224: if ((paramPerl5Pattern.n & 0x3) != 0)
      {
        if ((this.c == paramInt1) && (b(paramInt1)))
        {
          bool1 = true;
          break;
        }
        if ((this.d) || ((paramPerl5Pattern.n & 0x2) != 0) || ((paramPerl5Pattern.n & 0x8) != 0))
        {
          if (i1 > 0)
            i2 = i1 - 1;
          paramInt2 -= i2;
          if (this.c > paramInt3)
            this.c += -1;
          while (this.c < paramInt2)
          {
            if ((this.m[(this.c++)] != '\n') || (this.c >= paramInt2) || (!b(this.c)))
              continue;
            bool1 = true;
            break;
          }
        }
        break;
      }
      int i4;
      if (paramPerl5Pattern.j != null)
      {
        arrayOfChar = paramPerl5Pattern.j;
        if ((paramPerl5Pattern.n & 0x4) != 0)
        {
          i4 = arrayOfChar[0];
          while (this.c < paramInt2)
          {
            if (i4 == this.m[this.c])
            {
              if (b(this.c))
              {
                bool1 = true;
                break;
              }
              this.c += 1;
              while ((this.c < paramInt2) && (this.m[this.c] == i4))
                this.c += 1;
            }
            this.c += 1;
          }
        }
        else
        {
          while ((this.c = b(this.m, this.c, paramInt2, arrayOfChar)) < paramInt2)
          {
            if (b(this.c))
            {
              bool1 = true;
              break;
            }
            this.c += 1;
          }
        }
        break;
      }
      int i3;
      if ((i3 = paramPerl5Pattern.l) != -1)
      {
        boolean bool2 = (paramPerl5Pattern.n & 0x4) == 0;
        if (i1 > 0)
          i2 = i1 - 1;
        paramInt2 -= i2;
        boolean bool3 = true;
        char c2;
        char c1;
        switch (c2 = this.b[i3])
        {
        case '\t':
          i3 = c.d(i3);
          while (this.c < paramInt2)
          {
            i4 = this.m[this.c];
            if ((i4 < 256) && ((this.b[(i3 + (i4 >> 4))] & 1 << (i4 & 0xF)) == 0))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '#':
        case '$':
          i3 = c.d(i3);
          while (this.c < paramInt2)
          {
            i4 = this.m[this.c];
            if (b(i4, this.b, i3, c2))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '\024':
          if (i1 > 0)
          {
            i2++;
            paramInt2--;
          }
          if (this.c != paramInt1)
          {
            c1 = this.m[(this.c - 1)];
            bool3 = c.b(c1);
          }
          else
          {
            bool3 = c.b(this.r);
          }
          while (this.c < paramInt2)
          {
            c1 = this.m[this.c];
            if (bool3 != c.b(c1))
            {
              bool3 = !bool3;
              if (b(this.c))
              {
                bool1 = true;
                break;
              }
            }
            this.c += 1;
          }
          if (((i1 > 0) || (bool3)) && (b(this.c)))
          {
            bool1 = true;
            break;
          }
          break;
        case '\025':
          if (i1 > 0)
          {
            i2++;
            paramInt2--;
          }
          if (this.c != paramInt1)
          {
            c1 = this.m[(this.c - 1)];
            bool3 = c.b(c1);
          }
          else
          {
            bool3 = c.b(this.r);
          }
          while (this.c < paramInt2)
          {
            c1 = this.m[this.c];
            if (bool3 != c.b(c1))
            {
              bool3 = !bool3;
            }
            else if (b(this.c))
            {
              bool1 = true;
              break;
            }
            this.c += 1;
          }
          if (((i1 > 0) || (!bool3)) && (b(this.c)))
          {
            bool1 = true;
            break;
          }
          break;
        case '\022':
          while (this.c < paramInt2)
          {
            c1 = this.m[this.c];
            if (c.b(c1))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '\023':
          while (this.c < paramInt2)
          {
            c1 = this.m[this.c];
            if (!c.b(c1))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '\026':
          while (this.c < paramInt2)
          {
            if (Character.isWhitespace(this.m[this.c]))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '\027':
          while (this.c < paramInt2)
          {
            if (!Character.isWhitespace(this.m[this.c]))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '\030':
          while (this.c < paramInt2)
          {
            if (Character.isDigit(this.m[this.c]))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
          }
          break;
        case '\031':
          while (true)
          {
            if (!Character.isDigit(this.m[this.c]))
            {
              if ((bool3) && (b(this.c)))
              {
                bool1 = true;
                break;
              }
              bool3 = bool2;
            }
            else
            {
              bool3 = true;
            }
            this.c += 1;
            if (this.c >= paramInt2)
              break;
          }
        }
      }
      else
      {
        if (i1 > 0)
          i2 = i1 - 1;
        paramInt2 -= i2;
        do
        {
          if (!b(this.c))
            continue;
          bool1 = true;
          break;
        }
        while (this.c++ < paramInt2);
      }
      break;
    }
    this.q = bool1;
    this.j = null;
    return bool1;
  }

  public void setMultiline(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }

  public boolean matchesPrefix(char[] paramArrayOfChar, Pattern paramPattern, int paramInt)
  {
    Perl5Pattern localPerl5Pattern = (Perl5Pattern)paramPattern;
    this.t = paramArrayOfChar;
    if (localPerl5Pattern.h)
      paramArrayOfChar = b(paramArrayOfChar);
    c(localPerl5Pattern, paramArrayOfChar, 0, paramArrayOfChar.length, paramInt);
    this.q = b(paramInt);
    this.j = null;
    return this.q;
  }

  public boolean matchesPrefix(String paramString, Pattern paramPattern)
  {
    return matchesPrefix(paramString.toCharArray(), paramPattern, 0);
  }

  public boolean matchesPrefix(char[] paramArrayOfChar, Pattern paramPattern)
  {
    return matchesPrefix(paramArrayOfChar, paramPattern, 0);
  }

  public boolean matchesPrefix(PatternMatcherInput paramPatternMatcherInput, Pattern paramPattern)
  {
    Perl5Pattern localPerl5Pattern = (Perl5Pattern)paramPattern;
    this.t = paramPatternMatcherInput.i;
    char[] arrayOfChar;
    if (localPerl5Pattern.h)
    {
      if (paramPatternMatcherInput.f == null)
        paramPatternMatcherInput.f = b(this.t);
      arrayOfChar = paramPatternMatcherInput.f;
    }
    else
    {
      arrayOfChar = this.t;
    }
    c(localPerl5Pattern, arrayOfChar, paramPatternMatcherInput.h, paramPatternMatcherInput.g, paramPatternMatcherInput.b);
    this.q = b(paramPatternMatcherInput.b);
    this.j = null;
    return this.q;
  }

  private boolean b(char paramChar1, char[] paramArrayOfChar, int paramInt, char paramChar2)
  {
    int i1 = paramChar2 != '#' ? 0 : 1;
    while (paramArrayOfChar[paramInt] != 0)
      if (paramArrayOfChar[paramInt] == '%')
      {
        paramInt++;
        if ((paramChar1 >= paramArrayOfChar[paramInt]) && (paramChar1 <= paramArrayOfChar[(paramInt + 1)]))
          return i1;
        paramInt += 2;
      }
      else if (paramArrayOfChar[paramInt] == '1')
      {
        paramInt++;
        if (paramArrayOfChar[(paramInt++)] == paramChar1)
          return i1;
      }
      else
      {
        i1 = i1 != 0 ? 0 : paramArrayOfChar[paramInt] == '/' ? i1 : 1;
        paramInt++;
        switch (paramArrayOfChar[(paramInt++)])
        {
        case '\022':
          if (c.b(paramChar1))
            return i1;
          break;
        case '\023':
          if (!c.b(paramChar1))
            return i1;
          break;
        case '\026':
          if (Character.isWhitespace(paramChar1))
            return i1;
          break;
        case '\027':
          if (!Character.isWhitespace(paramChar1))
            return i1;
          break;
        case '\030':
          if (Character.isDigit(paramChar1))
            return i1;
          break;
        case '\031':
          if (!Character.isDigit(paramChar1))
            return i1;
          break;
        case '2':
          if (Character.isLetterOrDigit(paramChar1))
            return i1;
          break;
        case '&':
          if (Character.isLetter(paramChar1))
            return i1;
          break;
        case '\'':
          if (Character.isSpaceChar(paramChar1))
            return i1;
          break;
        case '(':
          if (Character.isISOControl(paramChar1))
            return i1;
          break;
        case '*':
          if (Character.isLowerCase(paramChar1))
            return i1;
          if ((this.v) && (Character.isUpperCase(paramChar1)))
            return i1;
          break;
        case '-':
          if (Character.isUpperCase(paramChar1))
            return i1;
          if ((this.v) && (Character.isLowerCase(paramChar1)))
            return i1;
          break;
        case '+':
          if (!Character.isSpaceChar(paramChar1))
            break;
          return i1;
        case ')':
          if (Character.isLetterOrDigit(paramChar1))
            return i1;
        case ',':
          switch (Character.getType(paramChar1))
          {
          case 20:
          case 21:
          case 22:
          case 23:
          case 24:
          case 25:
          case 26:
          case 27:
            return i1;
          }
          break;
        case '.':
          if (((paramChar1 >= '0') && (paramChar1 <= '9')) || ((paramChar1 >= 'a') && (paramChar1 <= 'f')) || ((paramChar1 >= 'A') && (paramChar1 <= 'F')))
            return i1;
          break;
        case '3':
          if (paramChar1 < '')
            return i1;
        }
      }
    return i1 == 0;
  }

  public boolean contains(String paramString, Pattern paramPattern)
  {
    return contains(paramString.toCharArray(), paramPattern);
  }

  public boolean contains(char[] paramArrayOfChar, Pattern paramPattern)
  {
    Perl5Pattern localPerl5Pattern = (Perl5Pattern)paramPattern;
    this.t = paramArrayOfChar;
    if (localPerl5Pattern.h)
      paramArrayOfChar = b(paramArrayOfChar);
    return b(localPerl5Pattern, paramArrayOfChar, 0, paramArrayOfChar.length, 0);
  }

  public boolean contains(PatternMatcherInput paramPatternMatcherInput, Pattern paramPattern)
  {
    if (paramPatternMatcherInput.b > paramPatternMatcherInput.g)
      return false;
    Perl5Pattern localPerl5Pattern = (Perl5Pattern)paramPattern;
    this.t = paramPatternMatcherInput.i;
    this.t = paramPatternMatcherInput.i;
    char[] arrayOfChar;
    if (localPerl5Pattern.h)
    {
      if (paramPatternMatcherInput.f == null)
        paramPatternMatcherInput.f = b(this.t);
      arrayOfChar = paramPatternMatcherInput.f;
    }
    else
    {
      arrayOfChar = this.t;
    }
    this.g = paramPatternMatcherInput.getMatchEndOffset();
    boolean bool = b(localPerl5Pattern, arrayOfChar, paramPatternMatcherInput.h, paramPatternMatcherInput.g, paramPatternMatcherInput.b);
    if (bool)
    {
      paramPatternMatcherInput.setCurrentOffset(this.o[0]);
      paramPatternMatcherInput.setMatchOffsets(this.h[0], this.o[0]);
    }
    else
    {
      paramPatternMatcherInput.setCurrentOffset(paramPatternMatcherInput.g + 1);
    }
    this.g = -100;
    return bool;
  }

  private boolean c(int paramInt)
  {
    int i16 = 1;
    boolean bool1 = false;
    int i11 = this.l;
    i16 = i11 >= this.u ? 0 : 1;
    int i1 = i16 != 0 ? this.m[i11] : 65535;
    int i9 = paramInt;
    int i12 = this.b.length;
    while (i9 < i12)
    {
      int i10 = c.c(this.b, i9);
      char c1;
      int i13;
      int i14;
      int i2;
      int i3;
      int i4;
      int i5;
      int i6;
      int i7;
      int i8;
      int i15;
      d locald;
      switch (c1 = this.b[i9])
      {
      case '\001':
        if (((!this.d) || ((i16 == 0) && (i11 >= this.e)) || (this.m[(i11 - 1)] != '\n') ? 0 : i11 == this.n ? this.r != '\n' ? 0 : 1 : 1) != 0)
          break;
        return false;
      case '\002':
        if ((((i16 == 0) && (i11 >= this.e)) || (this.m[(i11 - 1)] != '\n') ? 0 : i11 == this.n ? this.r != '\n' ? 0 : 1 : 1) != 0)
          break;
        return false;
      case '\003':
        if ((i11 == this.n) && (this.r == '\n'))
          break;
        return false;
      case '\036':
        if (i11 == this.n)
          break;
        return true;
      case '\004':
        if (((i16 != 0) || (i11 < this.e)) && (i1 != 10))
          return false;
        if ((!this.d) && (this.e - i11 > 1))
          return false;
        break;
      case '\005':
        if (((i16 != 0) || (i11 < this.e)) && (i1 != 10))
          return false;
        break;
      case '\006':
        if (((i16 != 0) || (i11 < this.e)) && (i1 != 10))
          return false;
        if (this.e - i11 > 1)
          return false;
        break;
      case '\b':
        if ((i16 == 0) && (i11 >= this.e))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i1 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\007':
        if (((i16 == 0) && (i11 >= this.e)) || (i1 == 10))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i1 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\016':
        i13 = c.d(i9);
        i14 = this.b[(i13++)];
        if (this.b[i13] != i1)
          return false;
        if (this.e - i11 < i14)
          return false;
        if ((i14 > 1) && (!b(this.b, i13, this.m, i11, i14)))
          return false;
        i11 += i14;
        i16 = i11 >= this.u ? 0 : 1;
        i1 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\t':
        i13 = c.d(i9);
        if ((i1 == 65535) && (i16 != 0))
          i1 = this.m[i11];
        if ((i1 >= 256) || ((this.b[(i13 + (i1 >> 4))] & 1 << (i1 & 0xF)) != 0))
          return false;
        if ((i16 == 0) && (i11 >= this.e))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i1 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '#':
      case '$':
        i13 = c.d(i9);
        if ((i1 == 65535) && (i16 != 0))
          i1 = this.m[i11];
        if (!b(i1, this.b, i13, c1))
          return false;
        if ((i16 == 0) && (i11 >= this.e))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i2 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\022':
        if (i16 == 0)
          return false;
        if (!c.b(i2))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i3 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\023':
        if ((i16 == 0) && (i11 >= this.e))
          return false;
        if (c.b(i3))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i4 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\024':
      case '\025':
        boolean bool2;
        if (i11 == this.n)
          bool2 = c.b(this.r);
        else
          bool2 = c.b(this.m[(i11 - 1)]);
        boolean bool3 = c.b(i4);
        if ((bool2 != bool3 ? 0 : 1) == (this.b[i9] != '\024' ? 0 : 1))
          return false;
        break;
      case '\026':
        if ((i16 == 0) && (i11 >= this.e))
          return false;
        if (!Character.isWhitespace(i4))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i5 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\027':
        if (i16 == 0)
          return false;
        if (Character.isWhitespace(i5))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i6 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\030':
        if (!Character.isDigit(i6))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i7 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\031':
        if ((i16 == 0) && (i11 >= this.e))
          return false;
        if (Character.isDigit(i7))
          return false;
        i11++;
        i16 = i11 >= this.u ? 0 : 1;
        i8 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\032':
        i15 = c.e(this.b, i9);
        i13 = this.h[i15];
        if (i13 == -1)
          return false;
        if (this.o[i15] == -1)
          return false;
        if (i13 == this.o[i15])
          break;
        if (this.m[i13] != i8)
          return false;
        i14 = this.o[i15] - i13;
        if (i11 + i14 > this.e)
          return false;
        if ((i14 > 1) && (!b(this.m, i13, this.m, i11, i14)))
          return false;
        i11 += i14;
        i16 = i11 >= this.u ? 0 : 1;
        i8 = i16 != 0 ? this.m[i11] : 65535;
        break;
      case '\017':
        break;
      case '\r':
        break;
      case '\033':
        i15 = c.e(this.b, i9);
        this.h[i15] = i11;
        if (i15 > this.p)
          this.p = i15;
        break;
      case '\034':
        i15 = c.e(this.b, i9);
        this.o[i15] = i11;
        if (i15 > this.s)
          this.s = i15;
        break;
      case '\013':
        locald = new d();
        locald.h = this.k;
        this.k = locald;
        locald.d = this.s;
        locald.f = -1;
        locald.e = c.e(this.b, i9);
        locald.j = c.b(this.b, i9);
        locald.g = (c.checkVersion(i9) + 2);
        locald.b = i10;
        locald.i = bool1;
        locald.c = -1;
        this.l = i11;
        bool1 = checkVersion(c.b(i10));
        this.k = locald.h;
        return bool1;
      case '"':
        locald = this.k;
        i15 = locald.f + 1;
        this.l = i11;
        if (i11 == locald.c)
        {
          this.k = locald.h;
          i14 = this.k.f;
          if (c(locald.b))
            return true;
          this.k.f = i14;
          this.k = locald;
          return false;
        }
        if (i15 < locald.e)
        {
          locald.f = i15;
          locald.c = i11;
          if (c(locald.g))
            return true;
          locald.f = (i15 - 1);
          return false;
        }
        if (locald.i)
        {
          this.k = locald.h;
          i14 = this.k.f;
          if (c(locald.b))
            return true;
          this.k.f = i14;
          this.k = locald;
          if (i15 >= locald.j)
            return false;
          this.l = i11;
          locald.f = i15;
          locald.c = i11;
          if (c(locald.g))
            return true;
          locald.f = (i15 - 1);
          return false;
        }
        if (i15 < locald.j)
        {
          d(locald.d);
          locald.f = i15;
          locald.c = i11;
          if (c(locald.g))
            return true;
          b();
          this.l = i11;
        }
        this.k = locald.h;
        i14 = this.k.f;
        if (c(locald.b))
          return true;
        locald.f = i14;
        this.k = locald;
        locald.f = (i15 - 1);
        return false;
      case '\f':
        if (this.b[i10] != '\f')
        {
          i10 = c.checkVersion(i9);
        }
        else
        {
          int i17 = this.s;
          do
          {
            this.l = i11;
            if (checkVersion(c.checkVersion(i9)))
              return true;
            for (i15 = this.s; i15 > i17; i15--)
              this.o[i15] = -1;
            this.s = i15;
            i9 = c.c(this.b, i9);
          }
          while ((i9 != -1) && (this.b[i9] == '\f'));
          return false;
        }
        break;
      case '\035':
        bool1 = true;
        break;
      case '\n':
      case '\020':
      case '\021':
        if (c1 == '\n')
        {
          i14 = c.e(this.b, i9);
          i15 = c.b(this.b, i9);
          i9 = c.checkVersion(i9) + 2;
        }
        else if (c1 == '\020')
        {
          i14 = 0;
          i15 = 65535;
          i9 = c.checkVersion(i9);
        }
        else
        {
          i14 = 1;
          i15 = 65535;
          i9 = c.checkVersion(i9);
        }
        if (this.b[i10] == '\016')
        {
          i8 = this.b[(c.d(i10) + 1)];
          i13 = 0;
        }
        else
        {
          i8 = 65535;
          i13 = -1000;
        }
        this.l = i11;
        if (bool1)
        {
          bool1 = false;
          if ((i14 > 0) && (b(i9, i14) < i14))
            return false;
          while ((i15 >= i14) || ((i15 == 65535) && (i14 > 0)))
          {
            if (((i13 == -1000) || (this.l >= this.u) || (this.m[this.l] == i8)) && (c(i10)))
              return true;
            this.l = (i11 + i14);
            if (b(i9, 1) != 0)
            {
              i14++;
              this.l = (i11 + i14);
            }
            else
            {
              return false;
            }
          }
        }
        else
        {
          i15 = b(i9, i15);
          if ((i14 < i15) && (c.log[this.b[i10]] == '\004') && (((!this.d) && (this.b[i10] != '\005')) || (this.b[i10] == '\006')))
            i14 = i15;
          while (i15 >= i14)
          {
            if (((i13 == -1000) || (this.l >= this.u) || (this.m[this.l] == i8)) && (c(i10)))
              return true;
            i15--;
            this.l = (i11 + i15);
          }
        }
        return false;
      case '\000':
      case '!':
        this.l = i11;
        return this.l != this.g;
      case '\037':
        this.l = i11;
        i9 = c.checkVersion(i9);
        if (!c(i9))
          return false;
        break;
      case ' ':
        this.l = i11;
        i9 = c.checkVersion(i9);
        if (c(i9))
          return false;
        break;
      }
      i9 = i10;
    }
    return false;
  }

  private static int b(char[] paramArrayOfChar1, int paramInt1, int paramInt2, char[] paramArrayOfChar2)
  {
    if (paramArrayOfChar1.length == 0)
      return paramInt2;
    int i3 = paramArrayOfChar2[0];
    while (paramInt1 < paramInt2)
    {
      if (i3 == paramArrayOfChar1[paramInt1])
      {
        int i2 = paramInt1;
        int i1 = 0;
        while ((paramInt1 < paramInt2) && (i1 < paramArrayOfChar2.length))
        {
          if (paramArrayOfChar2[i1] != paramArrayOfChar1[paramInt1])
            break;
          i1++;
          paramInt1++;
        }
        paramInt1 = i2;
        if (i1 >= paramArrayOfChar2.length)
          break;
      }
      paramInt1++;
    }
    return paramInt1;
  }

  private int b(int paramInt1, int paramInt2)
  {
    int i1 = this.l;
    int i2 = this.e;
    if ((paramInt2 != 65535) && (paramInt2 < i2 - i1))
      i2 = i1 + paramInt2;
    int i3 = c.d(paramInt1);
    char c1;
    int i5;
    switch (c1 = this.b[paramInt1])
    {
    case '\007':
      while ((i1 < i2) && (this.m[i1] != '\n'))
        i1++;
      break;
    case '\b':
      i1 = i2;
      break;
    case '\016':
      i3++;
      while ((i1 < i2) && (this.b[i3] == this.m[i1]))
        i1++;
      break;
    case '\t':
      if ((i1 < i2) && ((i5 = this.m[i1]) < 'Ā'))
        while ((i5 < 256) && ((this.b[(i3 + (i5 >> 4))] & 1 << (i5 & 0xF)) == 0))
        {
          i1++;
          if (i1 < i2)
            i5 = this.m[i1];
          else
            break;
        }
      break;
    case '#':
    case '$':
      if (i1 < i2)
      {
        i5 = this.m[i1];
        while (b(i5, this.b, i3, c1))
        {
          i1++;
          if (i1 < i2)
            i5 = this.m[i1];
          else
            break;
        }
      }
      break;
    case '\022':
      while ((i1 < i2) && (c.b(this.m[i1])))
        i1++;
      break;
    case '\023':
      while ((i1 < i2) && (!c.b(this.m[i1])))
        i1++;
      break;
    case '\026':
      while ((i1 < i2) && (Character.isWhitespace(this.m[i1])))
        i1++;
      break;
    case '\027':
      while ((i1 < i2) && (!Character.isWhitespace(this.m[i1])))
        i1++;
      break;
    case '\030':
      while ((i1 < i2) && (Character.isDigit(this.m[i1])))
        i1++;
      break;
    case '\031':
      while ((i1 < i2) && (!Character.isDigit(this.m[i1])))
        i1++;
      break;
    default:
      break;
    }
    int i4 = i1 - this.l;
    this.l = i1;
    return i4;
  }

  public boolean isMultiline()
  {
    return this.d;
  }

  private void c(Perl5Pattern paramPerl5Pattern, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
  {
    this.v = paramPerl5Pattern.h;
    this.m = paramArrayOfChar;
    this.u = paramInt2;
    this.k = new d();
    this.k.f = 0;
    this.k.h = null;
    this.b = paramPerl5Pattern.d;
    this.i.setSize(0);
    if ((paramInt3 == paramInt1) || (paramInt3 <= 0))
    {
      this.r = '\n';
    }
    else
    {
      this.r = paramArrayOfChar[(paramInt3 - 1)];
      if ((!this.d) && (this.r == '\n'))
        this.r = '\000';
    }
    this.f = paramPerl5Pattern.e;
    this.c = paramInt3;
    this.n = paramInt1;
    this.e = paramInt2;
    paramInt2 = this.f + 1;
    if ((this.h == null) || (paramInt2 > this.h.length))
    {
      if (paramInt2 < 20)
        paramInt2 = 20;
      this.h = new int[paramInt2];
      this.o = new int[paramInt2];
    }
  }

  private static boolean b(char[] paramArrayOfChar1, int paramInt1, char[] paramArrayOfChar2, int paramInt2, int paramInt3)
  {
    int i1 = 0;
    while (i1 < paramInt3)
    {
      if (paramInt1 >= paramArrayOfChar1.length)
        return false;
      if (paramInt2 >= paramArrayOfChar2.length)
        return false;
      if (paramArrayOfChar1[paramInt1] != paramArrayOfChar2[paramInt2])
        return false;
      i1++;
      paramInt1++;
      paramInt2++;
    }
    return true;
  }

  private boolean b(int paramInt)
  {
    this.l = paramInt;
    this.s = 0;
    this.p = 0;
    if (this.f > 0)
      for (int i1 = 0; i1 <= this.f; i1++)
      {
        this.h[i1] = -1;
        this.o[i1] = -1;
      }
    if (c(1))
    {
      this.h[0] = paramInt;
      this.o[0] = this.l;
      return true;
    }
    return false;
  }

  public MatchResult getMatch()
  {
    if (!this.q)
      return null;
    if (this.j == null)
      c();
    return this.j;
  }

  private void c()
  {
    int i2 = 0;
    this.j = new b(this.f + 1);
    if (this.o[0] > this.t.length)
      throw new ArrayIndexOutOfBoundsException();
    this.j.c = this.h[0];
    while (this.f >= 0)
    {
      int i1 = this.h[this.f];
      if (i1 >= 0)
        this.j.b[this.f] = (i1 - this.j.c);
      else
        this.j.b[this.f] = -1;
      i1 = this.o[this.f];
      if (i1 >= 0)
      {
        this.j.d[this.f] = (i1 - this.j.c);
        if ((i1 > i2) && (i1 <= this.t.length))
          i2 = i1;
      }
      else
      {
        this.j.d[this.f] = -1;
      }
      this.f += -1;
    }
    this.j.e = new String(this.t, this.h[0], i2 - this.h[0]);
    this.t = null;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.Perl5Matcher
 * JD-Core Version:    0.6.0
 */