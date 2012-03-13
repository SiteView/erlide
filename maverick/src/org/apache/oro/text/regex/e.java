package org.apache.oro.text.regex;

final class e
{
  int b;
  char[] c;

  char d()
  {
    return b(this.b);
  }

  char b(int paramInt)
  {
    if ((paramInt < this.c.length) && (paramInt >= 0))
      return this.c[paramInt];
    return 65535;
  }

  String f(int paramInt)
  {
    return new String(this.c, paramInt, this.c.length - paramInt);
  }

  e(char[] paramArrayOfChar, int paramInt)
  {
    this.c = paramArrayOfChar;
    this.b = paramInt;
  }

  e(char[] paramArrayOfChar)
  {
    this(paramArrayOfChar, 0);
  }

  char c(int paramInt)
  {
    this.b += paramInt;
    if (b())
    {
      this.b = this.c.length;
      return 65535;
    }
    return this.c[this.b];
  }

  char h()
  {
    return c(1);
  }

  public String toString()
  {
    return f(0);
  }

  char g(int paramInt)
  {
    this.b -= paramInt;
    if (this.b < 0)
      this.b = 0;
    return this.c[this.b];
  }

  char g()
  {
    return g(1);
  }

  char f()
  {
    int i = d();
    h();
    return i;
  }

  int c()
  {
    return this.b;
  }

  void e(int paramInt)
  {
    this.b = paramInt;
  }

  boolean b()
  {
    return this.b >= this.c.length;
  }

  int e()
  {
    return this.c.length;
  }

  char d(int paramInt)
  {
    return b(this.b + paramInt);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.e
 * JD-Core Version:    0.6.0
 */