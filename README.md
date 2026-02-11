# Assignment 2 Running Instructions

## Problem 1: PIncrement

Compile:
javac IdeaProjects/Assignment_2/src/*.java

Run:
java -cp IdeaProjects/Assignment_2/src Main

## Problem 3: OpenMP Programs

Compiling:

gcc -fopenmp -o MatrixMult MatrixMult.c
gcc -fopenmp -o Sieve Sieve.c -lm

Running:

MatrixMult:
./MatrixMult matrix1.txt matrix2.txt 4

Sieve:
./Sieve 100000000 4