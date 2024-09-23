FROM bde2020/spark-python-template:3.3.0-hadoop3.3

RUN echo $'#!/bin/bash\n\
\n\
export SPARK_MASTER_URL=spark://${SPARK_MASTER_NAME}:${SPARK_MASTER_PORT} \n\
export SPARK_HOME=/spark \n\
\n\
/wait-for-step.sh \n\
/execute-step.sh \n\
\n\
\n\
echo "Submit application ${SPARK_APPLICATION_PYTHON_LOCATION} to Spark master ${SPARK_MASTER_URL}" \n\
echo "Passing arguments ${SPARK_APPLICATION_ARGS}" \n\
PYSPARK_PYTHON=python3  /spark/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.0 --master ${SPARK_MASTER_URL} ${SPARK_SUBMIT_ARGS} ${SPARK_APPLICATION_PYTHON_LOCATION} ${SPARK_APPLICATION_ARGS} \n\
\n\
/finish-step.sh' >> /submit.sh

RUN cat /submit.sh

COPY job.py /app/

ENV SPARK_APPLICATION_PYTHON_LOCATION /app/job.py
ENV SPARK_APPLICATION_ARGS ""
ENV SPARK_MASTER_NAME "spark-master"