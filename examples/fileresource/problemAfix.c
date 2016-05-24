#include <stdio.h>
#include <stdlib.h>

int main()
{
	FILE *fp = fopen("myfile.txt", "w");
	fprintf("fp", "Hello world!!!");
fclose(fp);
}
