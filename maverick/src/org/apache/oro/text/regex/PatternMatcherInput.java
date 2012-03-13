package org.apache.oro.text.regex;

public final class PatternMatcherInput
{
  String c;
  char[] e;
  char[] i;
  char[] f;
  int h;
  int g;
  int b;
  int d = -1;
  int j = -1;

  public String preMatch()
  {
    return new String(this.i, this.h, this.d - this.h);
  }

  public int getEndOffset()
  {
    return this.g;
  }

  public void setEndOffset(int paramInt)
  {
    this.g = paramInt;
  }

  public String toString()
  {
    return new String(this.i, this.h, length());
  }

  public PatternMatcherInput(String paramString, int paramInt1, int paramInt2)
  {
    setInput(paramString, paramInt1, paramInt2);
  }

  public PatternMatcherInput(String paramString)
  {
    this(paramString, 0, paramString.length());
  }

  public PatternMatcherInput(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    setInput(paramArrayOfChar, paramInt1, paramInt2);
  }

  public PatternMatcherInput(char[] paramArrayOfChar)
  {
    this(paramArrayOfChar, 0, paramArrayOfChar.length);
  }

  public int getMatchEndOffset()
  {
    return this.j;
  }

  public char[] getBuffer()
  {
    return this.i;
  }

  public void setMatchOffsets(int paramInt1, int paramInt2)
  {
    this.d = paramInt1;
    this.j = paramInt2;
  }

  public String match()
  {
    return new String(this.i, this.d, this.j - this.d);
  }

  public char charAt(int paramInt)
  {
    return this.i[(this.h + paramInt)];
  }

  public int getBeginOffset()
  {
    return this.h;
  }

  public int getCurrentOffset()
  {
    return this.b;
  }

  public void setBeginOffset(int paramInt)
  {
    this.h = paramInt;
  }

  public void setCurrentOffset(int paramInt)
  {
    this.b = paramInt;
    setMatchOffsets(-1, -1);
  }

  public String postMatch()
  {
    return new String(this.i, this.j, this.g - this.j);
  }

  public void setInput(String paramString, int paramInt1, int paramInt2)
  {
    this.c = paramString;
    this.e = null;
    this.f = null;
    this.i = paramString.toCharArray();
    setCurrentOffset(paramInt1);
    setBeginOffset(paramInt1);
    setEndOffset(this.h + paramInt2);
  }

  public void setInput(String paramString)
  {
    setInput(paramString, 0, paramString.length());
  }

  public void setInput(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    this.c = null;
    this.f = null;
    this.i = (this.e = paramArrayOfChar);
    setCurrentOffset(paramInt1);
    setBeginOffset(paramInt1);
    setEndOffset(this.h + paramInt2);
  }

  public void setInput(char[] paramArrayOfChar)
  {
    setInput(paramArrayOfChar, 0, paramArrayOfChar.length);
  }

  public int length()
  {
    return this.g - this.h;
  }

  public String substring(int paramInt1, int paramInt2)
  {
    return new String(this.i, this.h + paramInt1, paramInt2 - paramInt1);
  }

  public String substring(int paramInt)
  {
    paramInt += this.h;
    return new String(this.i, paramInt, this.g - paramInt);
  }

  public Object getInput()
  {
    if (this.c == null)
      return this.e;
    return this.c;
  }

  public boolean endOfInput()
  {
    return this.b >= this.g;
  }

  public int getMatchBeginOffset()
  {
    return this.d;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.PatternMatcherInput
 * JD-Core Version:    0.6.0
 */