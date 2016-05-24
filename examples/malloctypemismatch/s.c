#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

void test_f() {
double *p=malloc(sizeof(double));
    // warn: result is converted to 'long *', which is
    // incompatible with operand type 'short'
  int x=9;
  int b=7;
  free(p);
}
