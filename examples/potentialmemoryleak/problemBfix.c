#include <stdio.h>
#include <stdlib.h>

int main(){
	int *p = (int *)malloc(sizeof(int));
	*p = 10;//just test line
	free(p);
}
