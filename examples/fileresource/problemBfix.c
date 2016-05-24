#include <stdio.h>
#include <stdlib.h>

int test_a()
{
	FILE *fp = fopen("myfile.txt", "w");
	fprintf("fp", "Hello world!!!");
fclose(fp);
}

int main(){
	test_a();
}
