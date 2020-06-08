#!/usr/bin/env bash
# Sample Command
# ./tools/aab/install.sh :customerapp:bundleLiveDevDebug -install-mode=universal-apk -launch=com.tokopedia.tkpd/com.tokopedia.tkpd.ConsumerSplashScreen
# ./tools/aab/install.sh :customerapp:bundleLiveDevDebug -install-mode=local-testing-apks -launch=com.tokopedia.tkpd/com.tokopedia.tkpd.ConsumerSplashScreen

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
	echo "./install.sh :customerapp:liveDevDebug"
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
./gradlew "$APP_COMPILE" || { echo 'gradle failed' ; exit 1; }
AAB_FILE_PATH="$APP_NAME/build/outputs/bundle/$COMPILE_TYPE/$APP_NAME-$BUNDLE_NAME.aab"
if [ ! -f "$AAB_FILE_PATH" ]; then
	echo "$AAB_FILE_PATH does not exist, please try again"
    exit 1
fi

INPUT=$AAB_FILE_PATH

# Check 2nd param
INSTALL_MODE=$2
if  [[ $INSTALL_MODE = "-install-mode="* ]]; then
    INSTALL_MODE=${INSTALL_MODE#"-install-mode="}
    if [ "$INSTALL_MODE" = "universal-apk" ]; then
      OUTPUT_APK="${INPUT::${#INPUT}-4}.apk"
      source ./tools/aab/convert.sh "$INPUT"
      adb install -r "$OUTPUT_APK"
    elif [ "$INSTALL_MODE" = "local-testing-apks" ]; then
      OUTPUT_APK="${INPUT::${#INPUT}-4}.apks"
      java -jar $BUNDLETOOL build-apks --overwrite --bundle=$INPUT --output="$OUTPUT_APK" --connected-device --local-testing
      java -jar $BUNDLETOOL install-apks --apks="$OUTPUT_APK"
      rm -rf "$OUTPUT_APK"
    fi
fi

# Check 3rd param
LAUNCHER_CLASS=$3
if  [[ $LAUNCHER_CLASS = "-launch="* ]]; then
    echo "Installing: $OUTPUT_APK"
    adb install -r "$OUTPUT_APK"

    LAUNCHER_CLASS=${LAUNCHER_CLASS#"-launch="}
    echo "Launching: $LAUNCHER_CLASS"
    adb shell am start -n $LAUNCHER_CLASS
fi