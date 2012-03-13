package org.apache.oro.text.regex;

import java.io.Serializable;

public final class Perl5Pattern
  implements Pattern, Serializable, Cloneable
{
  String i;
  char[] d;
  int b;
  int c;
  int f;
  int e;
  boolean h;
  boolean k;
  int l;
  int n;
  int m;
  char[] g;
  char[] j;

  public int getOptions()
  {
    return this.m;
  }

  public String getPattern()
  {
    return this.i;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.Perl5Pattern
 * JD-Core Version:    0.6.0
 */