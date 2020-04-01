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
if [ -z $APP_COMPILE ]; then
	echo "Error. Example of usage:"
	echo "./universal-apk.sh :customerapp:liveDevDebug"
    exit 1
fi

arr=(`echo $APP_COMPILE | tr ':' ' '`)
APP_NAME=${arr[0]}
COMPILE_TYPE=${arr[1]#"bundle"}
COMPILE_TYPE=$(echo $COMPILE_TYPE | awk '{$1=tolower(substr($1,0,1))substr($1,2)}1')

# Generate app bundle
./gradlew $APP_COMPILE
AAB_FILE_PATH="$APP_NAME/build/outputs/bundle/$COMPILE_TYPE/$APP_NAME.aab"
if [ ! -f "$AAB_FILE_PATH" ]; then
	echo "$AAB_FILE_PATH does not exist, please try again"
    exit 1
fi

INPUT=$AAB_FILE_PATH
OUTPUT_NAME=${INPUT::${#INPUT}-4}
OUTPUT_APKS="$OUTPUT_NAME.apks"
OUTPUT_APK="$OUTPUT_NAME.apk"
OUTPUT_ZIP="$OUTPUT_NAME.zip"
OUTPUT_FOLDER="./$OUTPUT_NAME"

java -jar $BUNDLETOOL build-apks --bundle=$INPUT --output="$OUTPUT_APKS" --mode=universal
mv "$OUTPUT_APKS" "$OUTPUT_ZIP"
mkdir "$OUTPUT_FOLDER"
unzip "$OUTPUT_ZIP" -d "$OUTPUT_FOLDER"
mv "$OUTPUT_FOLDER/universal.apk" "./"
mv "universal.apk" "$OUTPUT_APK"
rm -rf "$OUTPUT_FOLDER"
rm -rf "$OUTPUT_ZIP"
echo "Success! Here's your file: $OUTPUT_APK"

# Check 2nd param
LAUNCHER_CLASS=$2
if  [[ $LAUNCHER_CLASS = "-launch="* ]]; then
    echo "Installing: $OUTPUT_APK"
    adb install -r "$OUTPUT_APK"

    LAUNCHER_CLASS=${LAUNCHER_CLASS#"-launch="}
    echo "Launching: $LAUNCHER_CLASS"
    adb shell am start -n $LAUNCHER_CLASS
fi
if [ $2 = "-i" ]; then
	echo "Installing: $OUTPUT_APK"
	adb install -r "$OUTPUT_APK"
fi