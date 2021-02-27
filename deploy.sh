#!/bin/bash
echo "moving file"
mv -f  /var/lib/jenkins/jobs/TTB_backend/workspace/build/libs/TrackThatBarbell-0.0.1-SNAPSHOT.jar /var/www/jenkins

echo "kill process"
kill -9 $(cat /var/www/jenkins/TTB_backend/pid.file)

echo "run application production"
sudo nohup java -jar /var/www/jenkins/TTB_backend/TrackThatBarbell-0.0.1-SNAPSHOT.jar >  /var/www/TTB_backend/jenkins/server-dev.log 2>&1 &
echo $! > /var/www/jenkins/pid.file
echo "deploy finish"



