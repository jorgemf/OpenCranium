#!/bin/bash
find "$1" -name '*.class' | xargs "$4/jlint" +all -source "$2" -history "$3"

