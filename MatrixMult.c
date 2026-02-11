#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

int main(int argc, char* argv[]) {
    int t = atoi(argv[3]);
    omp_set_num_threads(t);
    
    FILE* f1 = fopen(argv[1], "r");
    int r1, c1;
    fscanf(f1, "%d %d", &r1, &c1);
    double** a = (double**)malloc(r1 * sizeof(double*));
    for (int i = 0; i < r1; i++) {
        a[i] = (double*)malloc(c1 * sizeof(double));
        for (int j = 0; j < c1; j++) {
            fscanf(f1, "%lf", &a[i][j]);
        }
    }
    fclose(f1);
    
    FILE* f2 = fopen(argv[2], "r");
    int r2, c2;
    fscanf(f2, "%d %d", &r2, &c2);
    double** b = (double**)malloc(r2 * sizeof(double*));
    for (int i = 0; i < r2; i++) {
        b[i] = (double*)malloc(c2 * sizeof(double));
        for (int j = 0; j < c2; j++) {
            fscanf(f2, "%lf", &b[i][j]);
        }
    }
    fclose(f2);
    
    double** c = (double**)malloc(r1 * sizeof(double*));
    for (int i = 0; i < r1; i++) {
        c[i] = (double*)malloc(c2 * sizeof(double));
    }
    
    double start = omp_get_wtime();
    
    #pragma omp parallel for
    for (int i = 0; i < r1; i++) {
        for (int j = 0; j < c2; j++) {
            c[i][j] = 0.0;
            for (int k = 0; k < c1; k++) {
                c[i][j] += a[i][k] * b[k][j];
            }
        }
    }
    
    double end = omp_get_wtime();
    double time = (end - start) * 1000.0;
    
    printf("%d %d\n", r1, c2);
    for (int i = 0; i < r1; i++) {
        for (int j = 0; j < c2; j++) {
            printf("%.6f", c[i][j]);
            if (j < c2 - 1) printf(" ");
        }
        printf("\n");
    }
    fprintf(stderr, "Time: %.6f ms\n", time);
    
    return 0;
}
