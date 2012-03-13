package org.apache.oro.text.regex;

final class b
  implements MatchResult
{
  int c;
  int[] b;
  int[] d;
  String e;

  public String group(int paramInt)
  {
    if (paramInt < this.b.length)
    {
      int i = this.b[paramInt];
      int j = this.d[paramInt];
      int k = this.e.length();
      if ((i >= 0) && (j >= 0))
      {
        if ((i < k) && (j <= k) && (j > i))
          return this.e.substring(i, j);
        if (i <= j)
          return "";
      }
    }
    return null;
  }

  public int begin(int paramInt)
  {
    if (paramInt < this.b.length)
    {
      int i = this.b[paramInt];
      int j = this.d[paramInt];
      if ((i >= 0) && (j >= 0))
        return i;
    }
    return -1;
  }

  public int end(int paramInt)
  {
    if (paramInt < this.b.length)
    {
      int i = this.b[paramInt];
      int j = this.d[paramInt];
      if ((i >= 0) && (j >= 0))
        return j;
    }
    return -1;
  }

  public int endOffset(int paramInt)
  {
    if (paramInt < this.d.length)
    {
      int i = this.b[paramInt];
      int j = this.d[paramInt];
      if ((i >= 0) && (j >= 0))
        return this.c + j;
    }
    return -1;
  }

  public String toString()
  {
    return group(0);
  }

  b(int paramInt)
  {
    this.b = new int[paramInt];
    this.d = new int[paramInt];
  }

  public int groups()
  {
    return this.b.length;
  }

  public int beginOffset(int paramInt)
  {
    if (paramInt < this.b.length)
    {
      int i = this.b[paramInt];
      int j = this.d[paramInt];
      if ((i >= 0) && (j >= 0))
        return this.c + i;
    }
    return -1;
  }

  public int length()
  {
    int i = this.d[0] - this.b[0];
    if (i > 0)
      return i;
    return 0;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     org.apache.oro.text.regex.b
 * JD-Core Version:    0.6.0
 */