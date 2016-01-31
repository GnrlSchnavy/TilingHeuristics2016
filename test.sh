#!/bin/bash
FILES=configurations/*
for f in configurations/*
do
  java src.TilingProblem $(basename "$f") minVertex >> result2.txt
done