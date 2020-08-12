#!/usr/bin/env bash
# Sample Command
# ./tools/aab/universal-apk.sh :customerapp:bundleLiveDevDebug -launch=com.tokopedia.tkpd/com.tokopedia.tkpd.ConsumerSplashScreen

# Update the bundletool file version
BUNDLETOOL=./tools/aab/bundletool.jar

# Check bundle tool
if [ ! -f $BUNDLETOOL ]; then
    echo "Please download and put bundletool.jar in this folder"
    echo "https://github.com/google/bundletool/releases"
    echo "Download bundletool-all-[LAST-VERSION].jar file, rename it to bundletool.jar"
    exit 1
fi

#Check app name
APP_COMPILE=$1
if [ -z "$APP_COMPILE" ]; then
	echo "Error. Example of usage:"
	echo "./universal-apk.sh :customerapp:liveDevDebug"
    exit 1
fi

arr=(`echo $APP_COMPILE | tr ':' ' '`)
APP_NAME=${arr[0]}
COMPILE_TYPE=${arr[1]#"bundle"}
COMPILE_TYPE=$(echo "$COMPILE_TYPE" | awk '{$1=tolower(substr($1,0,1))substr($1,2)}1')
BUNDLE_NAME=$(echo "$COMPILE_TYPE"        \
     | sed 's/\(.\)\([A-Z]\)/\1-\2/g' \
     | tr '[:upper:]' '[:lower:]')

# Generate app bundle
bash ./gradlew "$APP_COMPILE" || { echo 'gradle failed' ; exit 1; }
AAB_FILE_PATH="$APP_NAME/build/outputs/bundle/$COMPILE_TYPE/$APP_NAME-$BUNDLE_NAME.aab"
if [ ! -f "$AAB_FILE_PATH" ]; then
	echo "$AAB_FILE_PATH does not exist, please try again"
    exit 1
fi

INPUT=$AAB_FILE_PATH
OUTPUT_APK="${INPUT::${#INPUT}-4}.apk"
source ./tools/aab/convert.sh "$INPUT"

# Check 2nd param
LAUNCHER_CLASS=$2
if  [[ $LAUNCHER_CLASS = "-launch="* ]]; then
    echo "Installing: $OUTPUT_APK"
    adb install -r "$OUTPUT_APK"

    LAUNCHER_CLASS=${LAUNCHER_CLASS#"-launch="}
    echo "Launching: $LAUNCHER_CLASS"
    adb shell am start -n $LAUNCHER_CLASS
elif [ "$2" = "-i" ]; then
	echo "Installing: $OUTPUT_APK"
	adb install -r "$OUTPUT_APK"
fi