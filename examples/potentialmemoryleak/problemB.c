#include <stdio.h>
#include <stdlib.h>

int main(){
	int *p = (int *)malloc(sizeof(int));
	int *p2 = (int *)malloc(sizeof(int));
	*p = 10;
	*p2 = 20;
	free(p);
}