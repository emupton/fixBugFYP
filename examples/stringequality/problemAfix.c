#include <stdio.h>
#include <string.h>
#include <checkstring.h>

int main ()
{
  char str2[] = "hello";
  char str3[] = "world";

  int value = (strcmp(str2 , str3));

  return 0;
}
