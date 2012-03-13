package com.maverick.util;

public class Arrays
{
  private static int b(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    return paramArrayOfInt[paramInt1] > paramArrayOfInt[paramInt3] ? paramInt3 : paramArrayOfInt[paramInt2] > paramArrayOfInt[paramInt3] ? paramInt2 : paramArrayOfInt[paramInt1] < paramArrayOfInt[paramInt2] ? paramInt1 : paramArrayOfInt[paramInt1] < paramArrayOfInt[paramInt3] ? paramInt3 : paramArrayOfInt[paramInt2] < paramArrayOfInt[paramInt3] ? paramInt2 : paramInt1;
  }

  private static void c(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfInt[paramInt1];
    paramArrayOfInt[paramInt1] = paramArrayOfInt[paramInt2];
    paramArrayOfInt[paramInt2] = i;
  }

  public static void sort(int[] paramArrayOfInt)
  {
    b(paramArrayOfInt, 0, paramArrayOfInt.length);
  }

  private static void b(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    if (paramInt2 < 7)
    {
      for (i = paramInt1; i < paramInt2 + paramInt1; i++)
        for (j = i; (j > paramInt1) && (paramArrayOfInt[(j - 1)] > paramArrayOfInt[j]); j--)
          c(paramArrayOfInt, j, j - 1);
      return;
    }
    int i = paramInt1 + (paramInt2 >> 1);
    if (paramInt2 > 7)
    {
      j = paramInt1;
      k = paramInt1 + paramInt2 - 1;
      if (paramInt2 > 40)
      {
        m = paramInt2 / 8;
        j = b(paramArrayOfInt, j, j + m, j + 2 * m);
        i = b(paramArrayOfInt, i - m, i, i + m);
        k = b(paramArrayOfInt, k - 2 * m, k - m, k);
      }
      i = b(paramArrayOfInt, j, i, k);
    }
    int j = paramArrayOfInt[i];
    int k = paramInt1;
    int m = k;
    int n = paramInt1 + paramInt2 - 1;
    int i1 = n;
    while (true)
    {
      if ((m <= n) && (paramArrayOfInt[m] <= j))
      {
        if (paramArrayOfInt[m] == j)
          c(paramArrayOfInt, k++, m);
        m++;
        continue;
      }
      while ((n >= m) && (paramArrayOfInt[n] >= j))
      {
        if (paramArrayOfInt[n] == j)
          c(paramArrayOfInt, n, i1--);
        n--;
      }
      if (m > n)
        break;
      c(paramArrayOfInt, m++, n--);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.Arrays
 * JD-Core Version:    0.6.0
 */