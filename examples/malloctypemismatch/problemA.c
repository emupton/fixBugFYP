#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

void test_f() {
  short *p = malloc(sizeof(double));
  free(p);
}