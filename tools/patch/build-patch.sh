# Sample use command
#./tools/patch build-patch.sh

APP_COMPILE=$1
if [ -z $APP_COMPILE ]; then
	echo "Error. Example of usage:"
	echo "./build-patch.sh :sellerapp:assembleLiveProdRelease"
    exit 1
fi

arr=(`echo $APP_COMPILE | tr ':' ' '`)
APP_NAME=${arr[0]}
ROBUST_FOLDER="$APP_NAME/robust"
MAPPING_FILE_PATH="$APP_NAME/build/outputs/mapping/liveProd/release/mapping.txt"
METHODSMAP_FILE_PATH="$APP_NAME/build/outputs/robust/methodsMap.robust"
PATCH_FILE_PATH="$APP_NAME/build/outputs/robust/patch.jar"

# Compile app and generate robust file
./gradlew clean $APP_COMPILE -P robust -PtkpdBuildType=productionsplit --stacktrace

if [ ! -f "$MAPPING_FILE_PATH" ]; then
	echo "$MAPPING_FILE_PATH does not exist, please try again"
    exit 1
fi

if [ ! -f "$METHODSMAP_FILE_PATH" ]; then
	echo "$METHODSMAP_FILE_PATH does not exist, please try again"
    exit 1
fi

if [ ! -d "$ROBUST_FOLDER" ]; then
    echo "Directory $ROBUST_FOLDER DOES NOT exists. create folder"
    mkdir -p $ROBUST_FOLDER
fi

mv "$MAPPING_FILE_PATH" "$ROBUST_FOLDER/mapping.txt"
mv "$METHODSMAP_FILE_PATH" "$ROBUST_FOLDER/methodsMap.robust"

# Generate robust patch file
./gradlew $APP_COMPILE -P robust -P apply-patch -PtkpdBuildType=productionsplit --stacktrace

set -e

# Version variables
export GRADLE_PATH=./$APP_NAME/build.gradle
export GRADLE_FIELD="versionName"
export VERSION_TMP=$(grep $GRADLE_FIELD $GRADLE_PATH | awk '{print $2}')
export VERSION=$(echo $VERSION_TMP | sed -e 's/^"//'  -e 's/"$//')

# Rename patch file
OUTPUT_PATCH="$ROBUST_FOLDER/$APP_NAME-patch-$VERSION.jar"
if [ -f "$PATCH_FILE_PATH" ]; then
    mv "$PATCH_FILE_PATH" "$OUTPUT_PATCH"
	echo "Generate patch is success here is your patch: $OUTPUT_PATCH"
fi