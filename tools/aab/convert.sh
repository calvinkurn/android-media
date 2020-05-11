#!/usr/bin/env bash
# Example usage from root project
# tools/aab/convert.sh customerapp/build/outputs/bundle/liveDevDebug/customerapp-live-dev-debug.aab -i

BUNDLETOOL=./tools/aab/bundletool.jar

if [ ! -f $BUNDLETOOL ]; then
    echo "Please download and put bundletool.jar in this folder"
    echo "https://github.com/google/bundletool/releases"
    echo "Download bundletool-all-[LAST-VERSION].jar file, rename it to bundletool.jar"
    exit 1
fi

if [ -z "$1" ]; then
	echo "What's the name of your aab file?"
	echo "Example of usage:"
	echo "./convert.sh customerapp.aab"
    exit 1
fi

if [ ! -f "$1" ]; then
    echo "Your file doesn't exist: $1"
    exit 1
fi


INPUT=$1
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

if [ "$2" = "-i" ]; then
	echo "Installing: $OUTPUT_APK"
	adb install -r "$OUTPUT_APK"
fi