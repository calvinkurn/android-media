#!/usr/bin/env bash
set -e

buildPatch(){
  APP_NAME=$1
  ROBUST_FOLDER=robust
  MAPPING_FILE_PATH=robust/mapping.txt
  METHODSMAP_FILE_PATH=robust/methodsMap.txt

  if [ ! -d "$ROBUST_FOLDER" ]; then
      echo "Directory $ROBUST_FOLDER DOES NOT exists. create folder"
      mkdir -p $ROBUST_FOLDER
  fi

  if [ ! -f "$MAPPING_FILE_PATH" ]; then
    echo "$MAPPING_FILE_PATH does not exist, please copy file from robust crumbfile"
      exit 1
  fi

  if [ ! -f "$METHODSMAP_FILE_PATH" ]; then
    echo "$METHODSMAP_FILE_PATH does not exist, please copy file from robust crumbfile"
      exit 1
  fi

  rm -rf ./build/
  if [ "$APP_NAME" = 'customerapp' ]; then
    echo "Creating Robust patch for customerapp..."
    ./gradlew clean buildLiveProdReleasePreBundle -p customerapp -Pcom.robust.mode=patch -Pandroid.enableR8=true --no-daemon --stacktrace
  elif [ "$APP_NAME" = "sellerapp" ]; then
    echo "Creating Robust patch for sellerapp..."
    ./gradlew clean buildLiveProdReleasePreBundle -p sellerapp -Pcom.robust.mode=patch -Pandroid.enableR8=true --no-daemon --stacktrace
  else
    echo "No applicable selected app"
    exit 1
  fi

  echo "SUCCESS creating patch output file : build/outputs/robust"
}

if [ "$1" != "" ]
then
    APP_NAME=$1
else
    echo "\nEnter app name [customerapp or sellerapp] > "
    read APP_NAME
fi

buildPatch "$APP_NAME"