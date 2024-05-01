#!/bin/bash
# change config to bits
if grep 'resRepo=tokopedia' "gradle.properties"; then
  echo "changing resRepo to bits.."
  sed -i 's/resRepo=tokopedia/resRepo=bits/g' "gradle.properties"
fi

function comment() {
  if ! grep "//apply from:\"$1\"" "$2"; then
    sed -i "s|apply from:\"$1\"|//apply from:\"$1\"|g" "$2"
    echo "disabling $1"
  fi
}

function uncomment() {
  if grep "//apply from:\"$1\"" "$2"; then
      sed -i "s|//apply from:\"$1\"|apply from:\"$1\"|g" "$2"
      echo "enabling $1"
  fi
}

if [ -f "settings.gradle" ]; then
  echo "disabling unused settings for $1.."
  unused="buildconfig/appcompile/compile-customerapp-pro.gradle"
  dependencies=""
  if [ "$1" == "customerapp" ]; then
    unused+="buildconfig/appcompile/compile-sellerapp.gradle buildconfig/appcompile/compile-testapp.gradle"
    dependencies+="buildconfig/appcompile/compile-customerapp.gradle buildconfig/appcompile/compile-libraries.gradle"
  fi
  if [ "$1" == "sellerapp" ]; then
    unused+="buildconfig/appcompile/compile-customerapp.gradle buildconfig/appcompile/compile-testapp.gradle"
    dependencies+="buildconfig/appcompile/compile-sellerapp.gradle buildconfig/appcompile/compile-libraries.gradle"
   fi
   if [ "$1" == "testapp" ]; then
     unused+="buildconfig/appcompile/compile-customerapp.gradle buildconfig/appcompile/compile-sellerapp.gradle buildconfig/appcompile/compile-libraries.gradle"
     dependencies+="buildconfig/appcompile/compile-testapp.gradle"
   fi
   for unusedDependency in $unused
   do
     comment "$unusedDependency" settings.gradle
   done
   for dependency in $dependencies
   do
     uncomment "$dependency" settings.gradle
   done
   echo "done disabling unused settings"
fi
cat settings.gradle