#include <stdio.h>
#include <stdlib.h>

int main()
{
	FILE *fp = fopen("myfile.txt", "w");
	FILE *fp2 = fopen("myfile2.txt", "w");
	fprintf("fp", "Hello world!!!");
	fclose(fp2);
}