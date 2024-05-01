#!/bin/bash
# change config to bits
if grep 'resRepo=tokopedia' "gradle.properties"; then
  echo "changing resRepo to bits.."
  sed -i 's/resRepo=tokopedia/resRepo=bits/g' "gradle.properties"
fi

function comment() {
  if ! grep "//apply from:\"$1\"" "$2"; then
    sed -i "s|apply from:\"$1\"|//apply from:\"$1\"|g" "$2"
  fi
}

function uncomment() {
  if grep "//apply from:\"$1\"" "$2"; then
      sed -i "s|//apply from:\"$1\"|apply from:\"$1\"|g" "$2"
  fi
}

if [ -f "settings.gradle" ]; then
  echo "disabling unused settings.."
  if [ "$1" == "customerapp" ]; then
    unused="buildconfig/appcompile/compile-sellerapp.gradle buildconfig/appcompile/compile-customerapp-pro.gradle buildconfig/appcompile/compile-testapp.gradle"
    dependencies="buildconfig/appcompile/compile-customerapp.gradle buildconfig/appcompile/compile-libraries.gradle"
    for unusedDependency in $unused
    do
      comment unusedDependency settings.gradle
    done
    for dependency in $dependencies
    do
      uncomment dependency settings.gradle
    done
    echo "done disabling unused settings for customerapp.."
  fi
  if [ "$1" == "sellerapp" ]; then
      sed -i 's|apply from: "buildconfig/appcompile/compile-customerapp.gradle"||g' settings.gradle
      sed -i 's|apply from: "buildconfig/appcompile/compile-customerapp_pro.gradle"||g' settings.gradle
      sed -i 's|apply from: "buildconfig/appcompile/compile-testapp.gradle"||g' settings.gradle
      if grep '//apply from: "buildconfig/appcompile/compile-sellerapp.gradle"' "settings.gradle"; then
            sed -i 's|//apply from: "buildconfig/appcompile/compile-sellerapp.gradle"|apply from: "buildconfig/appcompile/compile-sellerapp.gradle"|g' settings.gradle
          fi
      echo "done disabling unused settings for sellerapp.."
   fi
   if [ "$1" == "testapp" ]; then
         sed -i 's|apply from: "buildconfig/appcompile/compile-sellerapp.gradle"||g' settings.gradle
         sed -i 's|apply from: "buildconfig/appcompile/compile-customerapp_pro.gradle"||g' settings.gradle
         sed -i 's|apply from: "buildconfig/appcompile/compile-customerapp.gradle"||g' settings.gradle
         if grep '//apply from: "buildconfig/appcompile/compile-testapp.gradle"' "settings.gradle"; then
               sed -i 's|//apply from: "buildconfig/appcompile/compile-testapp.gradle"|apply from: "buildconfig/appcompile/compile-testapp.gradle"|g' settings.gradle
         fi
         echo "done disabling unused settings for testapp.."
   fi
fi
cat settings.gradle