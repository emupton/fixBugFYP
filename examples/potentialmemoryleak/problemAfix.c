#include <stdio.h>
#include <stdlib.h>

int main(){
	int *p = (int *)malloc(sizeof(int));
	realloc(p, sizeof(long));
	free(p);
}
