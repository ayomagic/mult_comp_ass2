#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <omp.h>

int Sieve(int N, int threads) {
    omp_set_num_threads(threads);
    
    int* primes = (int*)malloc((N + 1) * sizeof(int));
    
    #pragma omp parallel for
    for (int i = 2; i <= N; i++) {
        primes[i] = 1;
    }
    
    int limit = (int)sqrt((double)N);
    
    for (int p = 2; p <= limit; p++) {
        if (primes[p]) {
            #pragma omp parallel for
            for (int i = p * p; i <= N; i += p) {
                primes[i] = 0;
            }
        }
    }
    
    int count = 0;
    #pragma omp parallel for reduction(+:count)
    for (int i = 2; i <= N; i++) {
        if (primes[i] == 1) {
            count++;
        }
    }
    
    free(primes);
    return count;
}

int main(int argc, char* argv[]) {
    int N = atoi(argv[1]);
    int t = atoi(argv[2]);
    
    double start = omp_get_wtime();
    int ans = Sieve(N, t);
    double end = omp_get_wtime();
    double time = (end - start) * 1000.0;
    
    printf("Primes: %d\n", ans);
    printf("Time: %.6f ms\n", time);
    
    return 0;
}
