#!/bin/bash
java -jar -XX:+UseG1GC -XX:MaxRAMPercentage=70.0 -XX:+HeapDumpOnOutOfMemoryError ./target/greening-1.0.0.jar