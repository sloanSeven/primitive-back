#!/bin/bash

#SCALA

SCALA_VERSION="2.11.7"

wget http://downloads.typesafe.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.tgz
tar -xzvf scala-${SCALA_VERSION}.tgz
rm -rf scala-${SCALA_VERSION}.tgz
echo "export SCALA_HOME=/home/ec2-user/scala-${SCALA_VERSION}" >> ~/.bashrc
echo "export PATH=$PATH:/home/ec2-user/scala-${SCALA_VERSION}/bin" >> ~/.bashrc
source ~/.bashrc

#SBT
curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo
sudo yum install sbt

#You also need to add /usr/share/sbt/bin to your PATH at .bashrc.